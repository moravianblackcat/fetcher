package cz.dan.fetcher.domain.football.request.coach.inbox.service;

import cz.dan.fetcher.domain.football.request.coach.inbox.entity.FootballCoachRequest;
import cz.dan.fetcher.domain.football.request.coach.inbox.entity.FootballCoachRequestRepository;
import cz.dan.fetcher.domain.football.request.coach.inbox.entity.mapper.FootballCoachRequestMapper;
import cz.dan.fetcher.domain.football.request.coach.inbox.entity.mapper.FootballCoachRequestMapperImpl;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static cz.dan.fetcher.domain.football.request.entity.RequestState.SCHEDULED;
import static cz.dan.fetcher.domain.source.Source.Sportmonks;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FootballCoachInboxRequestServiceTest {

    @Spy
    private final FootballCoachRequestMapper mapper = new FootballCoachRequestMapperImpl();

    @Mock
    private FootballCoachRequestRepository repository;

    @InjectMocks
    private FootballCoachInboxRequestService sut;

    @Captor
    private ArgumentCaptor<List<FootballCoachRequest>> footballCoachRequestCaptor;

    @Test
    void savesOnlyNotAlreadySavedCoachIdsInStandaloneRequests() {
        when(repository.findAlreadyExistingRequestIds(List.of(22L, 44L, 35L), "Sportmonks")).thenReturn(Set.of(44L));

        sut.saveRequests(List.of(22L, 44L, 35L), Sportmonks);

        verify(repository, times(1)).saveAll(footballCoachRequestCaptor.capture());
        assertThat(footballCoachRequestCaptor.getValue())
                .isNotNull()
                .extracting(
                        FootballCoachRequest::getId,
                        FootballCoachRequest::getSource,
                        FootballCoachRequest::getState
                ).containsExactly(
                        Tuple.tuple(22L, Sportmonks, SCHEDULED),
                        Tuple.tuple(35L, Sportmonks, SCHEDULED)
                );
    }

}