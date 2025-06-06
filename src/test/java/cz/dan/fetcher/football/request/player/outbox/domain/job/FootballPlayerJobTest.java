package cz.dan.fetcher.football.request.player.outbox.domain.job;

import cz.dan.fetcher.football.request.player.inbox.domain.entity.FootballPlayerRequest;
import cz.dan.fetcher.football.request.player.inbox.domain.entity.FootballPlayerRequest.State;
import cz.dan.fetcher.football.request.player.inbox.domain.entity.FootballPlayerRequestFailureDetail;
import cz.dan.fetcher.football.request.player.inbox.domain.service.FootballPlayerRequestService;
import cz.dan.fetcher.football.request.player.outbox.domain.entity.FootballPlayerRequestOutbox;
import cz.dan.fetcher.football.request.player.outbox.domain.service.FootballPlayerRequestOutboxService;
import cz.dan.fetcher.outbox.domain.exception.ResourceNotFoundException;
import cz.dan.fetcher.outbox.domain.fetcher.FootballPlayerFetcher;
import cz.dan.fetcher.outbox.domain.job.RequestJobProcessor;
import feign.FeignException;
import feign.FeignException.BadRequest;
import feign.FeignException.InternalServerError;
import feign.FeignException.TooManyRequests;
import feign.Request;
import org.junit.jupiter.api.BeforeEach;
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

import static cz.dan.fetcher.football.request.player.inbox.domain.entity.FootballPlayerRequest.State.RETRY;
import static cz.dan.fetcher.football.request.player.inbox.domain.entity.FootballPlayerRequest.State.SCHEDULED;
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
    private FootballPlayerFetcher footballPlayerFetcher;

    @Mock
    private FootballPlayerRequestService footballPlayerRequestService;

    @Mock
    private FootballPlayerRequestOutboxService footballPlayerRequestOutboxService;

    @Captor
    private ArgumentCaptor<FootballPlayerRequest> requestCaptor;

    @BeforeEach
    void setUp() {
        when(footballPlayerFetcher.supports(any())).thenReturn(true);
    }

    @ParameterizedTest
    @EnumSource(value = State.class, names = {"SCHEDULED", "RETRY"})
    void setsRequestToCompletedAndSavesToOutbox(State state) throws Exception {
        stubFootballPlayerRequestForProcessing(0L, state, new ArrayList<>());
        when(footballPlayerFetcher.getFootballPlayer(0L)).thenReturn(FootballPlayerRequestOutbox.builder().build());

        FootballPlayerJob sut = getSut(0);
        sut.run();

        verify(footballPlayerRequestService, times(1)).setToCompletedAndSave(requestCaptor.capture());
        verify(footballPlayerRequestOutboxService, times(1)).save(any());
        FootballPlayerRequest savedRequest = requestCaptor.getValue();
        assertState(savedRequest, state);
    }

    @ParameterizedTest
    @EnumSource(value = State.class, names = {"SCHEDULED", "RETRY"})
    void setsRequestToResourceNotFoundIfResourceIsNotFound(State state) throws Exception {
        stubFootballPlayerRequestForProcessing(0L, state, new ArrayList<>());
        doThrow(new ResourceNotFoundException()).when(footballPlayerFetcher).getFootballPlayer(0L);

        FootballPlayerJob sut = getSut(0);
        sut.run();

        verify(footballPlayerRequestService, times(1)).setToResourceNotFoundAndSave(requestCaptor.capture());
        FootballPlayerRequest savedRequest = requestCaptor.getValue();
        assertState(savedRequest, state);
    }

    @ParameterizedTest
    @MethodSource
    void setsRequestToErrorForNonRepeatableErrorRegardlessOfPossibleRetries(int maxRetries, State state)
            throws Exception {
        stubFootballPlayerRequestForProcessing(1L, state, new ArrayList<>());
        throwHttpErrorWhenObtainingPlayer(1L,
                new BadRequest("Bad Request", getRequest(), null, null));

        FootballPlayerJob sut = getSut(maxRetries);
        sut.run();

        verify(footballPlayerRequestService, times(1)).setToErrorAndSave(requestCaptor.capture());
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

        verify(footballPlayerRequestService, times(1)).setToRetryAndSave(requestCaptor.capture());
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

        verify(footballPlayerRequestService, times(1)).save(requestCaptor.capture());
        FootballPlayerRequest savedRequest = requestCaptor.getValue();
        assertState(savedRequest, RETRY);
        assertReasonOfFirstFailure(savedRequest, "firstReason");
        assertReasonOfLastFailure(savedRequest, expectedReason);
    }

    @ParameterizedTest
    @MethodSource
    void setsRequestToErrorForRepeatableErrorIfNotEnoughRetriesLeft(State state, int maxRetries,
                                                                    List<FootballPlayerRequestFailureDetail> failureDetails)
            throws Exception {
        stubFootballPlayerRequestForProcessing(2L, state, failureDetails);
        throwHttpErrorWhenObtainingPlayer(2L,
                new InternalServerError("Internal Server Error", getRequest(), null, null));

        FootballPlayerJob sut = getSut(maxRetries);
        sut.run();

        verify(footballPlayerRequestService, times(1)).setToErrorAndSave(requestCaptor.capture());
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
    void setsRequestToErrorForGenericErrorRegardlessOfPossibleRetries(int maxRetries, State state)
            throws Exception {
        stubFootballPlayerRequestForProcessing(7L, state, new ArrayList<>());
        doThrow(new Exception("Who knows")).when(footballPlayerFetcher).getFootballPlayer(7L);

        FootballPlayerJob sut = getSut(maxRetries);
        sut.run();

        verify(footballPlayerRequestService, times(1)).setToErrorAndSave(requestCaptor.capture());
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

    private void stubFootballPlayerRequestForProcessing(long playerId, State state,
                                                        List<FootballPlayerRequestFailureDetail> failureDetails) {
        FootballPlayerRequest request = FootballPlayerRequest.builder()
                .id(playerId)
                .state(state)
                .failureDetails(failureDetails)
                .build();
        when(footballPlayerRequestService.getOldestScheduled(CHUNK_VALUE)).thenReturn(List.of(request));
    }

    private void throwHttpErrorWhenObtainingPlayer(long playerId, FeignException error) throws Exception {
        doThrow(error)
                .when(footballPlayerFetcher)
                .getFootballPlayer(playerId);
    }

    private FootballPlayerJob getSut(int maxRetriesProperty) {
        FootballPlayerRequestJobProperties properties = new FootballPlayerRequestJobProperties();
        properties.setChunk(CHUNK_VALUE);
        properties.setMaxRetries(maxRetriesProperty);
        return new FootballPlayerJob(footballPlayerRequestService, properties, new RequestJobProcessor(),
                Set.of(footballPlayerFetcher), footballPlayerRequestOutboxService);
    }

    private void assertState(FootballPlayerRequest savedRequest, State state) {
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