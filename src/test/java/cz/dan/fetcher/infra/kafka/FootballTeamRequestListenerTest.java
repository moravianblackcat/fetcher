package cz.dan.fetcher.infra.kafka;

import cz.dan.avro.fetcher.Source;
import cz.dan.fetcher.domain.football.request.team.inbox.entity.FootballTeamRequest;
import cz.dan.fetcher.domain.inbox.service.request.InboxRequestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static cz.dan.fetcher.domain.source.Source.Sportmonks;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FootballTeamRequestListenerTest {

    @Mock
    private InboxRequestService<FootballTeamRequest> service;

    @InjectMocks
    private FootballTeamRequestListener sut;

    @Test
    void handlesRequestBySavingThem() {
        cz.dan.avro.fetcher.request.FootballTeamRequest request =
                cz.dan.avro.fetcher.request.FootballTeamRequest.newBuilder()
                        .setIds(List.of(1L, 2L))
                        .setSource(Source.Sportmonks)
                        .build();

        sut.handle(request);

        verify(service, times(1))
                .saveRequests(List.of(1L, 2L), Sportmonks);
    }

}