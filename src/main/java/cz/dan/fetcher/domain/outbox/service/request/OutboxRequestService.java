package cz.dan.fetcher.domain.outbox.service.request;

public interface OutboxRequestService<T> {

    void delete(long id);

    Iterable<T> getAll();

    void save(T outbox);
}
