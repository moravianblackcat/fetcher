package cz.dan.fetcher.infra.sender.person;

import cz.dan.fetcher.domain.outbox.sender.Sender;
import cz.dan.fetcher.domain.person.entity.PersonOutbox;
import cz.dan.fetcher.infra.sender.person.payload.PersonOutboxPayloadMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PersonKafkaSender extends Sender<PersonOutbox> {

    private final PersonOutboxPayloadMapper payloadMapper;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendOutbox(PersonOutbox outbox) {
        kafkaTemplate.send("fetcher.person", payloadMapper.from(outbox));
    }

}
