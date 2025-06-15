package cz.dan.fetcher.domain.inbox.entity.request;

import cz.dan.fetcher.domain.football.request.entity.RequestState;
import cz.dan.fetcher.domain.source.Source;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

import static cz.dan.fetcher.domain.football.request.entity.RequestState.SCHEDULED;
import static jakarta.persistence.EnumType.STRING;

@MappedSuperclass
@Slf4j
@SuperBuilder
public abstract class Request {

    @Column(nullable = false)
    @Enumerated(STRING)
    @Default
    @Getter
    @Setter
    protected RequestState state = SCHEDULED;

    protected Request() {
    }

    public abstract void addFailureDetail(String reason);

    public abstract Long getId();

    public abstract int getNumberOfFailures();

    public abstract Source getSource();

    public boolean isScheduled() {
        return SCHEDULED == this.state;
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

    private void setToState(Supplier<RequestState> stateSupplier) {
        RequestState newState = stateSupplier.get();
        log.info("Translating {} {} from {} to {}.", this.getClass().getSimpleName(), this.getId(), this.state, newState);
        this.state = newState;
    }

}
