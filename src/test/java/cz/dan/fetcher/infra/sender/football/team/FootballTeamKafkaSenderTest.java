package cz.dan.fetcher.infra.sender.football.team;

import cz.dan.avro.fetcher.outbox.FootballTeamOutboxPayload;
import cz.dan.fetcher.domain.football.request.team.entity.FootballTeamRequestOutbox;
import cz.dan.fetcher.infra.sender.football.team.payload.FootballTeamOutboxPayloadMapper;
import cz.dan.fetcher.infra.sender.football.team.payload.FootballTeamOutboxPayloadMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FootballTeamKafkaSenderTest {

    @Spy
    private final FootballTeamOutboxPayloadMapper footballTeamOutboxPayloadMapper =
            new FootballTeamOutboxPayloadMapperImpl();

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private FootballTeamKafkaSender sut;

    @Captor
    private ArgumentCaptor<FootballTeamOutboxPayload> outboxPayloadCaptor;

    @Test
    void sendsCorrectlyMappedPayload() {
        sut.sendOutbox(FootballTeamRequestOutbox.builder()
                .id(15L)
                .name("Botafogo")
                .country("BRA")
                .city("Rio de Janeiro")
                .stadium("Estádio Olímpico Nilton Santos")
                .build());

        verify(kafkaTemplate, times(1)).
                send(eq("fetcher.response.football.team"),
                        outboxPayloadCaptor.capture());
        assertThat(outboxPayloadCaptor.getValue())
                .isNotNull()
                .extracting(
                        FootballTeamOutboxPayload::getId,
                        FootballTeamOutboxPayload::getName,
                        FootballTeamOutboxPayload::getCountry,
                        FootballTeamOutboxPayload::getCity,
                        FootballTeamOutboxPayload::getStadium
                ).containsExactly(
                        15L,
                        "Botafogo",
                        "BRA",
                        "Rio de Janeiro",
                        "Estádio Olímpico Nilton Santos"
                );
    }

}