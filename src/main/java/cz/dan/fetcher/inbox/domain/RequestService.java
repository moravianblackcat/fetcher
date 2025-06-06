package cz.dan.fetcher.inbox.domain;

import cz.dan.fetcher.outbox.domain.entity.Request;

import java.util.List;

public interface RequestService<T extends Request> {

    List<T> getOldestScheduled(int number);

    void save(T request);

    void setToRetryAndSave(T request);

    void setToErrorAndSave(T request);

    void setToResourceNotFoundAndSave(T request);

    void setToCompletedAndSave(T request);

}
