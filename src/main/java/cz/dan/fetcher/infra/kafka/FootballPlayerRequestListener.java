package cz.dan.fetcher.infra.kafka;

import cz.dan.avro.fetcher.Source;
import cz.dan.avro.fetcher.request.FootballPlayerRequest;
import cz.dan.fetcher.domain.inbox.service.request.InboxRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class FootballPlayerRequestListener {

    private final InboxRequestService<cz.dan.fetcher.domain.football.request.player.inbox.entity.FootballPlayerRequest> service;

    @KafkaListener(topics = "fetcher.request.football.player")
    public void handle(@Payload FootballPlayerRequest request) {
        List<Long> playerIds = request.getIds();
        Source requestSource = request.getSource();
        log.info("Received request for football players with IDs {} from {}.", playerIds, requestSource);
        service.saveRequests(playerIds, cz.dan.fetcher.domain.source.Source.valueOf(requestSource.name()));
        log.info("Request for football players with IDs {} from {} handled.", playerIds, requestSource);
    }

}
