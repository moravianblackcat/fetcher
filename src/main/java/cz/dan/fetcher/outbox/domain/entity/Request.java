package cz.dan.fetcher.outbox.domain.entity;

public interface Request {

    void addFailureDetail(String reason);

    boolean isScheduled();

    Long getId();

    int getNumberOfFailures();

    Source getSource();

}
