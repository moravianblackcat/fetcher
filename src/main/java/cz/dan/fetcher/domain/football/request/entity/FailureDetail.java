package cz.dan.fetcher.domain.football.request.entity;

import java.time.OffsetDateTime;

public interface FailureDetail<T> {

    Long getId();

    String getReason();

    OffsetDateTime getTimestamp();

    T getRequest();

}
