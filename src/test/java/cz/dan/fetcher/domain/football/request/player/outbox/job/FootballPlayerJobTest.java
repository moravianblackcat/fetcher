package cz.dan.fetcher.domain.football.request.player.outbox.job;

import cz.dan.fetcher.domain.football.request.entity.RequestState;
import cz.dan.fetcher.domain.football.request.player.inbox.entity.FootballPlayerRequest;
import cz.dan.fetcher.domain.football.request.player.inbox.entity.FootballPlayerRequestFailureDetail;
import cz.dan.fetcher.domain.football.request.player.outbox.entity.FootballPlayerRequestOutbox;
import cz.dan.fetcher.domain.inbox.service.request.InboxRequestService;
import cz.dan.fetcher.domain.outbox.exception.resource.ResourceNotFoundException;
import cz.dan.fetcher.domain.outbox.fetcher.Fetcher;
import cz.dan.fetcher.domain.outbox.job.request.RequestJobProcessor;
import cz.dan.fetcher.domain.outbox.service.request.OutboxRequestService;
import cz.dan.fetcher.domain.person.service.PersonService;
import feign.FeignException;
import feign.FeignException.BadRequest;
import feign.FeignException.InternalServerError;
import feign.FeignException.TooManyRequests;
import feign.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static cz.dan.fetcher.domain.football.request.entity.RequestState.RETRY;
import static cz.dan.fetcher.domain.football.request.entity.RequestState.SCHEDULED;
import static feign.Request.HttpMethod.GET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FootballPlayerJobTest {

    private static final int CHUNK_VALUE = 5;

    private static Request getRequest() {
        return Request.create(GET, "/", Map.of(), null, null, null);
    }

    @Mock
    private Fetcher<FootballPlayerRequestOutbox, FootballPlayerRequest> fetcher;

    @Mock
    private InboxRequestService<FootballPlayerRequest> inboxRequestService;

    @Mock
    private OutboxRequestService<FootballPlayerRequestOutbox> outboxRequestService;

    @Mock
    private PersonService personService;

    @Captor
    private ArgumentCaptor<FootballPlayerRequest> requestCaptor;

    @Captor
    private ArgumentCaptor<FootballPlayerRequestOutbox> outboxCaptor;

    @BeforeEach
    void setUp() {
        when(fetcher.supports(any())).thenReturn(true);
    }

    @ParameterizedTest
    @EnumSource(value = RequestState.class, names = {"SCHEDULED", "RETRY"})
    void setsRequestToCompletedAndSavesToOutbox(RequestState state) throws Exception {
        stubFootballPlayerRequestForProcessing(0L, state, new ArrayList<>());
        when(fetcher.get(0L)).thenReturn(FootballPlayerRequestOutbox.builder().build());

        FootballPlayerJob sut = getSut(0);
        sut.run();

        verify(inboxRequestService, times(1)).setToCompletedAndSave(requestCaptor.capture());
        verify(outboxRequestService, times(1)).save(any());
        FootballPlayerRequest savedRequest = requestCaptor.getValue();
        assertState(savedRequest, state);
    }

    @Test
    void outboxIsSavedWithIdForPerson() throws Exception {
        stubFootballPlayerRequestForProcessing(0L, SCHEDULED, new ArrayList<>());
        when(fetcher.get(0L)).thenReturn(FootballPlayerRequestOutbox.builder().build());
        when(personService.save(any())).thenReturn(15L);

        FootballPlayerJob sut = getSut(0);
        sut.run();

        verify(outboxRequestService, times(1)).save(outboxCaptor.capture());
        assertThat(outboxCaptor.getValue())
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 15L);
    }

    @ParameterizedTest
    @EnumSource(value = RequestState.class, names = {"SCHEDULED", "RETRY"})
    void setsRequestToResourceNotFoundIfResourceIsNotFound(RequestState state) throws Exception {
        stubFootballPlayerRequestForProcessing(0L, state, new ArrayList<>());
        doThrow(new ResourceNotFoundException()).when(fetcher).get(0L);

        FootballPlayerJob sut = getSut(0);
        sut.run();

        verify(inboxRequestService, times(1)).setToResourceNotFoundAndSave(requestCaptor.capture());
        FootballPlayerRequest savedRequest = requestCaptor.getValue();
        assertState(savedRequest, state);
    }

    @ParameterizedTest
    @MethodSource
    void setsRequestToErrorForNonRepeatableErrorRegardlessOfPossibleRetries(int maxRetries, RequestState state)
            throws Exception {
        stubFootballPlayerRequestForProcessing(1L, state, new ArrayList<>());
        throwHttpErrorWhenObtainingPlayer(1L,
                new BadRequest("Bad Request", getRequest(), null, null));

        FootballPlayerJob sut = getSut(maxRetries);
        sut.run();

        verify(inboxRequestService, times(1)).setToErrorAndSave(requestCaptor.capture());
        FootballPlayerRequest savedRequest = requestCaptor.getValue();
        assertState(savedRequest, state);
        assertReasonOfFirstFailure(savedRequest, "Bad Request");
    }

    private static Stream<Arguments> setsRequestToErrorForNonRepeatableErrorRegardlessOfPossibleRetries() {
        return Stream.of(
                Arguments.of(0, SCHEDULED),
                Arguments.of(0, RETRY),
                Arguments.of(1, SCHEDULED),
                Arguments.of(1, RETRY)
        );
    }

    @ParameterizedTest
    @MethodSource("repeatableErrors")
    void setsScheduledRequestToRetryForRepeatableErrorIfEnoughRetriesLeft(FeignException error, String expectedReason)
            throws Exception {
        stubFootballPlayerRequestForProcessing(2L, SCHEDULED, new ArrayList<>());
        throwHttpErrorWhenObtainingPlayer(2L, error);

        FootballPlayerJob sut = getSut(2);
        sut.run();

        verify(inboxRequestService, times(1)).setToRetryAndSave(requestCaptor.capture());
        FootballPlayerRequest savedRequest = requestCaptor.getValue();
        assertState(savedRequest, SCHEDULED);
        assertReasonOfFirstFailure(savedRequest, expectedReason);
    }

    @ParameterizedTest
    @MethodSource("repeatableErrors")
    void addsFailureReasonForRetryRequestAndDoesNotChangeStateIfEnoughRetriesLeft(FeignException error,
                                                                                  String expectedReason)
            throws Exception {
        stubFootballPlayerRequestForProcessing(3L, RETRY,
                new ArrayList<>(List.of(FootballPlayerRequestFailureDetail.builder()
                        .reason("firstReason")
                        .build())));
        throwHttpErrorWhenObtainingPlayer(3L, error);

        FootballPlayerJob sut = getSut(3);
        sut.run();

        verify(inboxRequestService, times(1)).save(requestCaptor.capture());
        FootballPlayerRequest savedRequest = requestCaptor.getValue();
        assertState(savedRequest, RETRY);
        assertReasonOfFirstFailure(savedRequest, "firstReason");
        assertReasonOfLastFailure(savedRequest, expectedReason);
    }

    @ParameterizedTest
    @MethodSource
    void setsRequestToErrorForRepeatableErrorIfNotEnoughRetriesLeft(RequestState state, int maxRetries,
                                                                    List<FootballPlayerRequestFailureDetail> failureDetails)
            throws Exception {
        stubFootballPlayerRequestForProcessing(2L, state, failureDetails);
        throwHttpErrorWhenObtainingPlayer(2L,
                new InternalServerError("Internal Server Error", getRequest(), null, null));

        FootballPlayerJob sut = getSut(maxRetries);
        sut.run();

        verify(inboxRequestService, times(1)).setToErrorAndSave(requestCaptor.capture());
        FootballPlayerRequest savedRequest = requestCaptor.getValue();
        assertState(savedRequest, state);
    }

    private static Stream<Arguments> setsRequestToErrorForRepeatableErrorIfNotEnoughRetriesLeft() {
        return Stream.of(
                Arguments.of(SCHEDULED, 0, new ArrayList<>()),
                Arguments.of(SCHEDULED, 1, new ArrayList<>()),
                Arguments.of(SCHEDULED, 2,
                        new ArrayList<>(List.of(FootballPlayerRequestFailureDetail.builder().build()))),
                Arguments.of(RETRY, 2,
                        new ArrayList<>(List.of(FootballPlayerRequestFailureDetail.builder().build())))
        );
    }

    @ParameterizedTest
    @MethodSource
    void setsRequestToErrorForGenericErrorRegardlessOfPossibleRetries(int maxRetries, RequestState state)
            throws Exception {
        stubFootballPlayerRequestForProcessing(7L, state, new ArrayList<>());
        doThrow(new Exception("Who knows")).when(fetcher).get(7L);

        FootballPlayerJob sut = getSut(maxRetries);
        sut.run();

        verify(inboxRequestService, times(1)).setToErrorAndSave(requestCaptor.capture());
        FootballPlayerRequest savedRequest = requestCaptor.getValue();
        assertState(savedRequest, state);
        assertReasonOfFirstFailure(savedRequest, "Who knows");
    }

    private static Stream<Arguments> setsRequestToErrorForGenericErrorRegardlessOfPossibleRetries() {
        return Stream.of(
                Arguments.of(0, SCHEDULED),
                Arguments.of(0, RETRY),
                Arguments.of(1, SCHEDULED),
                Arguments.of(1, RETRY)
        );
    }

    private static Stream<Arguments> repeatableErrors() {
        return Stream.of(
                Arguments.of(
                        new TooManyRequests("Too Many Requests", getRequest(), null, null), "Too Many Requests"),
                Arguments.of(
                        new InternalServerError("Internal Server Error", getRequest(), null, null), "Internal Server Error")
        );
    }

    private void stubFootballPlayerRequestForProcessing(long playerId, RequestState state,
                                                        List<FootballPlayerRequestFailureDetail> failureDetails) {
        FootballPlayerRequest request = FootballPlayerRequest.builder()
                .id(playerId)
                .state(state)
                .failureDetails(failureDetails)
                .build();
        when(inboxRequestService.getOldestForProcessing(CHUNK_VALUE)).thenReturn(List.of(request));
    }

    private void throwHttpErrorWhenObtainingPlayer(long playerId, FeignException error) throws Exception {
        doThrow(error)
                .when(fetcher)
                .get(playerId);
    }

    private FootballPlayerJob getSut(int maxRetriesProperty) {
        FootballPlayerRequestJobProperties properties = new FootballPlayerRequestJobProperties();
        properties.setChunk(CHUNK_VALUE);
        properties.setMaxRetries(maxRetriesProperty);

        return new FootballPlayerJob(Set.of(fetcher), inboxRequestService,
                outboxRequestService, personService, properties, new RequestJobProcessor());
    }

    private void assertState(FootballPlayerRequest savedRequest, RequestState state) {
        assertThat(savedRequest)
                .isNotNull()
                .hasFieldOrPropertyWithValue("state", state);
    }

    private void assertReasonOfFirstFailure(FootballPlayerRequest savedRequest, String reason) {
        assertThat(savedRequest.getFailureDetails())
                .isNotEmpty()
                .first()
                .hasFieldOrPropertyWithValue("reason", reason);
    }

    private void assertReasonOfLastFailure(FootballPlayerRequest savedRequest, String reason) {
        assertThat(savedRequest.getFailureDetails())
                .isNotEmpty()
                .last()
                .hasFieldOrPropertyWithValue("reason", reason);
    }

}