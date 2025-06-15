package cz.dan.fetcher.infra.kafka;

import cz.dan.avro.fetcher.Source;
import cz.dan.avro.fetcher.request.FootballCoachRequest;
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
public class FootballCoachRequestListener {

    private final InboxRequestService<cz.dan.fetcher.domain.football.request.coach.inbox.entity.FootballCoachRequest> service;

    @KafkaListener(topics = "fetcher.request.football.coach")
    public void handle(@Payload FootballCoachRequest request) {
        List<Long> coachIds = request.getIds();
        Source requestSource = request.getSource();
        log.info("Received request for football coaches with IDs {} from {}.", coachIds, requestSource);
        service.saveRequests(coachIds, cz.dan.fetcher.domain.source.Source.valueOf(requestSource.name()));
        log.info("Request for football players with IDs {} from {} handled.", coachIds, requestSource);
    }

}
