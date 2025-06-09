package cz.dan.fetcher.domain.football.request.player.inbox.entity;

import cz.dan.fetcher.domain.inbox.entity.request.Request;
import cz.dan.fetcher.domain.source.Source;
import jakarta.persistence.*;
import lombok.*;
import lombok.Builder.Default;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static cz.dan.fetcher.domain.football.request.player.inbox.entity.FootballPlayerRequest.State.RETRY;
import static cz.dan.fetcher.domain.football.request.player.inbox.entity.FootballPlayerRequest.State.SCHEDULED;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.EAGER;

@Slf4j
@Entity
@IdClass(FootballPlayerRequest.FootballPlayerRequestId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FootballPlayerRequest implements Request {

    @Id
    @Column(nullable = false)
    private Long id;

    @Id
    @Column(nullable = false)
    @Enumerated(STRING)
    private Source source;

    @Column(nullable = false)
    @Enumerated(STRING)
    @Default
    private State state = SCHEDULED;

    @Column(nullable = false)
    @Default
    private OffsetDateTime createdAt = ZonedDateTime.now(Clock.systemUTC()).toOffsetDateTime();

    @OneToMany(mappedBy = "footballPlayerRequest", cascade = CascadeType.ALL, orphanRemoval = true, fetch = EAGER)
    @Default
    private List<FootballPlayerRequestFailureDetail> failureDetails = new ArrayList<>();

    public void addFailureDetail(String reason) {
        failureDetails.add(FootballPlayerRequestFailureDetail.builder()
                .footballPlayerRequest(this)
                .reason(reason)
                .timestamp(OffsetDateTime.now(ZoneId.of("UTC")))
                .build());
    }

    public boolean isInRetryState() {
        return RETRY == this.state;
    }

    public boolean isScheduled() {
        return SCHEDULED == this.state;
    }

    public int getNumberOfFailures() {
        return failureDetails.size();
    }

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class FootballPlayerRequestId implements Serializable {
        private Long id;

        @Enumerated(STRING)
        private Source source;
    }

    public enum State {
        SCHEDULED {
            @Override
            public State toError() {
                return ERROR;
            }

            @Override
            public State toRetry() {
                return RETRY;
            }

            @Override
            public State toResourceNotFound() {
                return RESOURCE_NOT_FOUND;
            }

            @Override
            public State toCompleted() {
                return COMPLETED;
            }
        },
        RETRY {
            @Override
            public State toError() {
                return ERROR;
            }

            @Override
            public State toResourceNotFound() {
                return RESOURCE_NOT_FOUND;
            }

            @Override
            public State toCompleted() {
                return COMPLETED;
            }
        },
        ERROR,
        RESOURCE_NOT_FOUND,
        COMPLETED;

        public State toRetry() {
            throw new IllegalStateException("Not possible to move FootballPlayerRequest to RETRY.");
        }

        public State toError() {
            throw new IllegalStateException("Not possible to move FootballPlayerRequest to ERROR.");
        }

        public State toResourceNotFound() {
            throw new IllegalStateException("Not possible to move FootballPlayerRequest to RESOURCE_NOT_FOUND.");
        }

        public State toCompleted() {
            throw new IllegalStateException("Not possible to move FootballPlayerRequest to COMPLETED.");
        }

    }

    public void toRetry() {
        setToState(() -> state.toRetry());
    }

    public void toError() {
        setToState(() -> state.toError());
    }

    public void toResourceNotFound() {
        setToState(() -> state.toResourceNotFound());
    }

    public void toCompleted() {
        setToState(() -> state.toCompleted());
    }

    private void setToState(Supplier<State> stateSupplier) {
        State newState = stateSupplier.get();
        log.info("Translating FootballPlayerRequest {} from {} to {}.", this.getId(), this.getState(), newState);
        this.state = newState;
    }

}
