package cz.dan.fetcher.infra.kafka;

import cz.dan.avro.fetcher.request.FootballPlayerRequest;
import cz.dan.avro.fetcher.request.Source;
import cz.dan.fetcher.domain.football.request.player.inbox.service.FootballPlayerInboxRequestService;
import cz.dan.fetcher.domain.football.request.player.inbox.source.FootballPlayerRequestSource;
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

    private final FootballPlayerInboxRequestService service;

    @KafkaListener(topics = "fetcher.request.football.player")
    public void handle(@Payload FootballPlayerRequest request) {
        List<Long> playerIds = request.getIds();
        Source requestSource = request.getSource();
        log.info("Received request for football players with IDs {} from {}.", playerIds, requestSource);
        service.saveRequests(playerIds, FootballPlayerRequestSource.valueOf(requestSource.name()));
        log.info("Request for football players with IDs {} from {} handled.", playerIds, requestSource);
    }

}
