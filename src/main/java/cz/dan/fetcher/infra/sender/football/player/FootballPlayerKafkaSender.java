package cz.dan.fetcher.infra.sender.football.player;

import cz.dan.fetcher.domain.football.request.player.outbox.entity.FootballPlayerRequestOutbox;
import cz.dan.fetcher.domain.outbox.sender.Sender;
import cz.dan.fetcher.infra.sender.football.player.payload.FootballPlayerOutboxPayloadMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FootballPlayerKafkaSender extends Sender<FootballPlayerRequestOutbox> {

    private final FootballPlayerOutboxPayloadMapper footballPlayerOutboxPayloadMapper;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendOutbox(FootballPlayerRequestOutbox outbox) {
        kafkaTemplate.send("fetcher.response.football.player", footballPlayerOutboxPayloadMapper.from(outbox));
    }
}
