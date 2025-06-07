package cz.dan.fetcher.domain.outbox.service.request;

public interface OutboxRequestService<T> {

    void save(T outboxRequest);

}
