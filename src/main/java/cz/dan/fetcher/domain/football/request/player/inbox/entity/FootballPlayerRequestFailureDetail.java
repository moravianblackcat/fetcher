package cz.dan.fetcher.domain.football.request.player.inbox.entity;

import cz.dan.fetcher.domain.football.request.entity.FailureDetail;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FootballPlayerRequestFailureDetail implements FailureDetail<FootballPlayerRequest> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    @Default
    private OffsetDateTime timestamp = ZonedDateTime.now(Clock.systemUTC()).toOffsetDateTime();

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "player_id", referencedColumnName = "id"),
            @JoinColumn(name = "source", referencedColumnName = "source")
    })
    private FootballPlayerRequest footballPlayerRequest;

    @Override
    public FootballPlayerRequest getRequest() {
        return footballPlayerRequest;
    }

}