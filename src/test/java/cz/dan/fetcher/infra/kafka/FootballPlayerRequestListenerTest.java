package cz.dan.fetcher.infra.kafka;

import cz.dan.avro.fetcher.request.FootballPlayerRequest;
import cz.dan.avro.fetcher.request.Source;
import cz.dan.fetcher.domain.football.request.player.inbox.service.FootballPlayerInboxRequestService;
import cz.dan.fetcher.domain.football.request.player.inbox.source.FootballPlayerRequestSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FootballPlayerRequestListenerTest {

    @Mock
    private FootballPlayerInboxRequestService service;

    @InjectMocks
    private FootballPlayerRequestListener sut;

    @Test
    void handlesRequestBySavingThem() {
        FootballPlayerRequest request = FootballPlayerRequest.newBuilder()
                .setIds(List.of(1L, 2L))
                .setSource(Source.Sportmonks)
                .build();

        sut.handle(request);

        verify(service, times(1))
                .saveRequests(List.of(1L, 2L), FootballPlayerRequestSource.Sportmonks);
    }

}