package cz.dan.fetcher.infra.kafka;

import cz.dan.avro.fetcher.Source;
import cz.dan.avro.fetcher.request.FootballCoachRequest;
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
class FootballCoachRequestListenerTest {

    @Mock
    private InboxRequestService<cz.dan.fetcher.domain.football.request.coach.inbox.entity.FootballCoachRequest> service;

    @InjectMocks
    private FootballCoachRequestListener sut;

    @Test
    void handlesRequestBySavingThem() {
        FootballCoachRequest request = FootballCoachRequest.newBuilder()
                .setIds(List.of(1L, 2L))
                .setSource(Source.Sportmonks)
                .build();

        sut.handle(request);

        verify(service, times(1))
                .saveRequests(List.of(1L, 2L), Sportmonks);
    }

}