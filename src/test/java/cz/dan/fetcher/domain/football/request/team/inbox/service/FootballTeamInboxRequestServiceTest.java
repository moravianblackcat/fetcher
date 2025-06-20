package cz.dan.fetcher.domain.football.request.team.inbox.service;

import cz.dan.fetcher.domain.football.request.team.inbox.entity.FootballTeamRequest;
import cz.dan.fetcher.domain.football.request.team.inbox.entity.FootballTeamRequestMapper;
import cz.dan.fetcher.domain.football.request.team.inbox.entity.FootballTeamRequestMapperImpl;
import cz.dan.fetcher.domain.football.request.team.inbox.entity.FootballTeamRequestRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static cz.dan.fetcher.domain.football.request.entity.RequestState.SCHEDULED;
import static cz.dan.fetcher.domain.source.Source.Sportmonks;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FootballTeamInboxRequestServiceTest {

    @Spy
    private final FootballTeamRequestMapper mapper = new FootballTeamRequestMapperImpl();

    @Mock
    private FootballTeamRequestRepository repository;

    @InjectMocks
    private FootballTeamInboxRequestService sut;

    @Captor
    private ArgumentCaptor<List<FootballTeamRequest>> footballTeamRequestCaptor;

    @Test
    void savesAllTeamIdsInStandaloneRequests() {
        sut.saveRequests(List.of(122L, 535L), Sportmonks);

        verify(repository, times(1)).saveAll(footballTeamRequestCaptor.capture());
        assertThat(footballTeamRequestCaptor.getValue())
                .isNotNull()
                .extracting(
                        FootballTeamRequest::getId,
                        FootballTeamRequest::getSource,
                        FootballTeamRequest::getState
                ).containsExactly(
                        Tuple.tuple(122L, Sportmonks, SCHEDULED),
                        Tuple.tuple(535L, Sportmonks, SCHEDULED)
                );
    }

}