package cz.dan.fetcher.domain.inbox.entity.request;

public interface Request {

    void addFailureDetail(String reason);

    boolean isScheduled();

    Long getId();

    int getNumberOfFailures();

    Source getSource();

}
