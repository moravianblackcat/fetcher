package cz.dan.fetcher.football.request.player.infra;

import cz.dan.avro.fetcher.request.FootballPlayerRequest;
import cz.dan.avro.fetcher.request.Source;
import cz.dan.fetcher.football.request.player.inbox.domain.service.FootballPlayerRequestService;
import cz.dan.fetcher.football.request.player.inbox.domain.source.FootballPlayerRequestSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log4j2
@RequiredArgsConstructor
public class FootballPlayerRequestListener {

    private final FootballPlayerRequestService service;

    @KafkaListener(topics = "fetcher.request.football.player")
    public void handle(@Payload FootballPlayerRequest request) {
        List<Long> playerIds = request.getIds();
        Source requestSource = request.getSource();
        log.info("Received request for football players with IDs {} from {}.", playerIds, requestSource);
        service.saveRequests(playerIds, FootballPlayerRequestSource.valueOf(requestSource.name()));
        log.info("Request for football players with IDs {} from {} handled.", playerIds, requestSource);
    }

}
