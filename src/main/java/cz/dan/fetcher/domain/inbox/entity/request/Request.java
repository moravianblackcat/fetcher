package cz.dan.fetcher.domain.inbox.entity.request;

import cz.dan.fetcher.domain.source.Source;

public interface Request {

    void addFailureDetail(String reason);

    boolean isScheduled();

    Long getId();

    int getNumberOfFailures();

    Source getSource();

}
