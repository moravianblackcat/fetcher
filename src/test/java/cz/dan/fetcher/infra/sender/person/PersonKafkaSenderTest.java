package cz.dan.fetcher.infra.sender.person;

import cz.dan.avro.fetcher.outbox.PersonOutboxPayload;
import cz.dan.fetcher.domain.person.entity.PersonOutbox;
import cz.dan.fetcher.infra.sender.person.payload.PersonOutboxPayloadMapper;
import cz.dan.fetcher.infra.sender.person.payload.PersonOutboxPayloadMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PersonKafkaSenderTest {

    @Spy
    private final PersonOutboxPayloadMapper payloadMapper = new PersonOutboxPayloadMapperImpl();

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private PersonKafkaSender sut;

    @Captor
    private ArgumentCaptor<PersonOutboxPayload> outboxPayloadCaptor;

    @Test
    void sendsCorrectlyMappedPayload() {
        sut.sendOutbox(PersonOutbox.builder()
                .id(2L)
                .nationality("GER")
                .firstName("Thomas")
                .lastName("Tuchel")
                .name("Thomas Tuchel")
                .displayName("Thomas Tuchel")
                .dateOfBirth(LocalDate.of(1973, 8, 29))
                .build());

        verify(kafkaTemplate, times(1)).send(eq("fetcher.person"), outboxPayloadCaptor.capture());
        assertThat(outboxPayloadCaptor.getValue())
                .isNotNull()
                .extracting(
                        PersonOutboxPayload::getId,
                        PersonOutboxPayload::getNationality,
                        PersonOutboxPayload::getFirstName,
                        PersonOutboxPayload::getLastName,
                        PersonOutboxPayload::getName,
                        PersonOutboxPayload::getDisplayName,
                        PersonOutboxPayload::getDateOfBirth
                )
                .containsExactly(
                        2L,
                        "GER",
                        "Thomas",
                        "Tuchel",
                        "Thomas Tuchel",
                        "Thomas Tuchel",
                        LocalDate.of(1973, 8, 29)
                );
    }

}