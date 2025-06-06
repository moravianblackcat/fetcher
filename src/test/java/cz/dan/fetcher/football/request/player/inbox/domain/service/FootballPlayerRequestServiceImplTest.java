package cz.dan.fetcher.football.request.player.inbox.domain.service;

import cz.dan.fetcher.football.request.player.inbox.domain.entity.FootballPlayerRequest;
import cz.dan.fetcher.football.request.player.inbox.domain.entity.FootballPlayerRequestRepository;
import cz.dan.fetcher.football.request.player.inbox.domain.entity.mapper.FootballPlayerRequestMapper;
import cz.dan.fetcher.football.request.player.inbox.domain.entity.mapper.FootballPlayerRequestMapperImpl;
import cz.dan.fetcher.football.request.player.inbox.domain.source.FootballPlayerRequestSource;
import cz.dan.fetcher.outbox.domain.entity.Source;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static cz.dan.fetcher.football.request.player.inbox.domain.entity.FootballPlayerRequest.State.SCHEDULED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FootballPlayerRequestServiceImplTest {

    @Spy
    private final FootballPlayerRequestMapper mapper = new FootballPlayerRequestMapperImpl();

    @Mock
    private FootballPlayerRequestRepository repository;

    @InjectMocks
    private FootballPlayerRequestServiceImpl sut;

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