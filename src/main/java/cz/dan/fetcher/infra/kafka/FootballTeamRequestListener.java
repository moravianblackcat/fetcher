package cz.dan.fetcher.infra.kafka;

import cz.dan.avro.fetcher.Source;
import cz.dan.avro.fetcher.request.FootballTeamRequest;
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
public class FootballTeamRequestListener {

    private final InboxRequestService<cz.dan.fetcher.domain.football.request.team.inbox.entity.FootballTeamRequest> service;

    @KafkaListener(topics = "fetcher.request.football.team")
    public void handle(@Payload FootballTeamRequest request) {
        List<Long> teamIds = request.getIds();
        Source requestSource = request.getSource();
        log.info("Received request for football teams with IDs {} from {}.", teamIds, requestSource);
        service.saveRequests(teamIds, cz.dan.fetcher.domain.source.Source.valueOf(requestSource.name()));
        log.info("Request for football teams with IDs {} from {} handled.", teamIds, requestSource);
    }

}
