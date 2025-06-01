package cz.dan.fetcher.football.request.player.inbox.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

import static cz.dan.fetcher.football.request.player.inbox.domain.entity.FootballPlayerRequest.State.SCHEDULED;
import static jakarta.persistence.EnumType.STRING;

@Entity
@IdClass(FootballPlayerRequest.FootballPlayerRequestId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FootballPlayerRequest {

    @Id
    @Column(name = "player_id", nullable = false)
    private Long playerId;

    @Id
    @Column(name = "source", nullable = false)
    @Enumerated(STRING)
    private Source source;

    @Column(name = "state", nullable = false)
    @Enumerated(STRING)
    @Default
    private State state = SCHEDULED;

    @Column(nullable = false)
    @Default
    private OffsetDateTime createdAt = ZonedDateTime.now(Clock.systemUTC()).toOffsetDateTime();

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FootballPlayerRequestId implements Serializable {
        @Column(name = "player_id")
        private Long playerId;

        @Column(name = "source")
        @Enumerated(STRING)
        private Source source;
    }

    public enum State {
        SCHEDULED
    }

    public enum Source {
        Sportmonks
    }

}
