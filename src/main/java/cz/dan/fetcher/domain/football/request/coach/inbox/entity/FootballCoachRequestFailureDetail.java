package cz.dan.fetcher.domain.football.request.coach.inbox.entity;

import cz.dan.fetcher.domain.football.request.entity.FailureDetail;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
public class FootballCoachRequestFailureDetail implements FailureDetail<FootballCoachRequest> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    @Builder.Default
    private OffsetDateTime timestamp = ZonedDateTime.now(Clock.systemUTC()).toOffsetDateTime();

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "coach_id", referencedColumnName = "id"),
            @JoinColumn(name = "source", referencedColumnName = "source")
    })
    private FootballCoachRequest footballCoachRequest;

    @Override
    public FootballCoachRequest getRequest() {
        return footballCoachRequest;
    }
}
