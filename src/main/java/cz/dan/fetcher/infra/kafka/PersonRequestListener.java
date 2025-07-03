package cz.dan.fetcher.infra.kafka;

import cz.dan.avro.fetcher.request.PersonRequest;
import cz.dan.fetcher.person.service.CustomPersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PersonRequestListener {

    private final CustomPersonService customPersonService;

    @KafkaListener(topics = "fetcher.request.person")
    public void handle(@Payload PersonRequest request) {
        log.info("Received request for {} {} custom person registration.",
                request.getFirstName(), request.getLastName());
        customPersonService.save(request);
        log.info("Request for {} {} custom person registration handled.",
                request.getFirstName(), request.getLastName());
    }

}
