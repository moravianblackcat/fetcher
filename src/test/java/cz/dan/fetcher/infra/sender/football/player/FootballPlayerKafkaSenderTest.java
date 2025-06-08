package cz.dan.fetcher.infra.sender.football.player;

import cz.dan.avro.fetcher.FootballPosition;
import cz.dan.avro.fetcher.Source;
import cz.dan.avro.fetcher.outbox.FootballPlayerOutboxPayload;
import cz.dan.fetcher.domain.football.request.player.outbox.entity.FootballPlayerRequestOutbox;
import cz.dan.fetcher.infra.sender.football.player.payload.FootballPlayerOutboxPayloadMapper;
import cz.dan.fetcher.infra.sender.football.player.payload.FootballPlayerOutboxPayloadMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;

import static cz.dan.fetcher.domain.football.request.player.outbox.entity.FootballPlayerRequestOutbox.Position.goalkeeper;
import static cz.dan.fetcher.domain.football.request.player.outbox.entity.FootballPlayerRequestOutbox.Source.Sportmonks;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FootballPlayerKafkaSenderTest {

    @Spy
    private final FootballPlayerOutboxPayloadMapper footballPlayerOutboxPayloadMapper =
            new FootballPlayerOutboxPayloadMapperImpl();

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private FootballPlayerKafkaSender sut;

    @Captor
    private ArgumentCaptor<FootballPlayerOutboxPayload> outboxPayloadCaptor;

    @Test
    void sendsCorrectlyMappedPayload() {
        sut.sendOutbox(FootballPlayerRequestOutbox.builder()
                .id(1L)
                .sourceId(2L)
                .nationality("ENG")
                .position(goalkeeper)
                .firstName("Jordan")
                .lastName("Pickford")
                .name("Jordan Pickford")
                .displayName("Jordan Pickford")
                .dateOfBirth(LocalDate.of(1994, 3, 7))
                .source(Sportmonks)
                .build());

        verify(kafkaTemplate, times(1)).
                send(eq("fetcher.response.football.player"),
                        outboxPayloadCaptor.capture());
        assertThat(outboxPayloadCaptor.getValue())
                .isNotNull()
                .extracting(
                        FootballPlayerOutboxPayload::getId,
                        FootballPlayerOutboxPayload::getSourceId,
                        FootballPlayerOutboxPayload::getNationality,
                        FootballPlayerOutboxPayload::getPosition,
                        FootballPlayerOutboxPayload::getFirstName,
                        FootballPlayerOutboxPayload::getLastName,
                        FootballPlayerOutboxPayload::getName,
                        FootballPlayerOutboxPayload::getDisplayName,
                        FootballPlayerOutboxPayload::getDateOfBirth,
                        FootballPlayerOutboxPayload::getSource
                ).containsExactly(
                        1L,
                        2L,
                        "ENG",
                        FootballPosition.goalkeeper,
                        "Jordan",
                        "Pickford",
                        "Jordan Pickford",
                        "Jordan Pickford",
                        LocalDate.of(1994, 3, 7),
                        Source.Sportmonks
                );
    }

}