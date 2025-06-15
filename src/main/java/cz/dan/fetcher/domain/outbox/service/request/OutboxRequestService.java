package cz.dan.fetcher.domain.outbox.service.request;

import org.springframework.data.repository.CrudRepository;

public abstract class OutboxRequestService<T> {

    private final CrudRepository<T, Long> repository;

    protected OutboxRequestService(CrudRepository<T, Long> repository) {
        this.repository = repository;
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

    public Iterable<T> getAll() {
        return repository.findAll();
    }
    
    public void save(T outbox) {
        repository.save(outbox);
    }
    
}
