package cz.dan.fetcher.domain.football.request.player.inbox.service;

import cz.dan.fetcher.domain.football.request.player.inbox.entity.FootballPlayerRequest;
import cz.dan.fetcher.domain.football.request.player.inbox.entity.FootballPlayerRequestRepository;
import cz.dan.fetcher.domain.football.request.player.inbox.entity.mapper.FootballPlayerRequestMapper;
import cz.dan.fetcher.domain.football.request.player.inbox.entity.mapper.FootballPlayerRequestMapperImpl;
import cz.dan.fetcher.domain.football.request.player.inbox.source.FootballPlayerRequestSource;
import cz.dan.fetcher.domain.source.Source;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static cz.dan.fetcher.domain.football.request.player.inbox.entity.FootballPlayerRequest.State.SCHEDULED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FootballPlayerInboxRequestServiceImplTest {

    @Spy
    private final FootballPlayerRequestMapper mapper = new FootballPlayerRequestMapperImpl();

    @Mock
    private FootballPlayerRequestRepository repository;

    @InjectMocks
    private FootballPlayerInboxRequestServiceImpl sut;

    @Captor
    private ArgumentCaptor<List<FootballPlayerRequest>> footballPlayerRequestsCaptor;

    @Test
    void savesAllPlayerIdsInStandaloneRequests() {
        sut.saveRequests(List.of(15L, 22L), FootballPlayerRequestSource.Sportmonks);

        verify(repository, times(1)).saveAll(footballPlayerRequestsCaptor.capture());
        assertThat(footballPlayerRequestsCaptor.getValue())
                .isNotNull()
                .extracting(FootballPlayerRequest::getId,
                        FootballPlayerRequest::getSource,
                        FootballPlayerRequest::getState)
                .containsExactly(
                        Tuple.tuple(15L, Source.Sportmonks, SCHEDULED),
                        Tuple.tuple(22L, Source.Sportmonks, SCHEDULED)
                );
    }

}