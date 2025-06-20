package cz.dan.fetcher.infra.sender.football.team;

import cz.dan.fetcher.domain.football.request.team.entity.FootballTeamRequestOutbox;
import cz.dan.fetcher.domain.outbox.sender.Sender;
import cz.dan.fetcher.infra.sender.football.team.payload.FootballTeamOutboxPayloadMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FootballTeamKafkaSender extends Sender<FootballTeamRequestOutbox> {

    private final FootballTeamOutboxPayloadMapper mapper;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendOutbox(FootballTeamRequestOutbox outbox) {
        kafkaTemplate.send("fetcher.response.football.team", mapper.from(outbox));
    }

}
