package cz.dan.fetcher.domain.inbox.service.request;

import cz.dan.fetcher.domain.football.request.entity.RequestMapper;
import cz.dan.fetcher.domain.football.request.entity.RequestRepository;
import cz.dan.fetcher.domain.inbox.entity.request.Request;
import cz.dan.fetcher.domain.source.Source;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@Slf4j
public abstract class InboxRequestService<T extends Request> {
    
    private final RequestMapper<T> mapper;
    
    protected final RequestRepository<T> repository;

    protected InboxRequestService(RequestMapper<T> mapper, RequestRepository<T> repository) {
        this.mapper = mapper;
        this.repository = repository;
    }
    
    public void save(T request) {
        repository.save(request);
    }
    
    public void saveRequests(List<Long> ids, Source source) {
        List<T> requests = getRequestsToSave(ids, source);

        repository.saveAll(requests);
    }

    public List<T> getOldestForProcessing(int number) {
        return repository.findOldestForProcessing(number);
    }
    public void setToRetryAndSave(T request) {
        setToStateAndSave(T::toRetry, request);
    }
    
    public void setToErrorAndSave(T request) {
        setToStateAndSave(T::toError, request);
    }
    
    public void setToResourceNotFoundAndSave(T request) {
        setToStateAndSave(T::toResourceNotFound, request);
    }
    
    public void setToCompletedAndSave(T request) {
        setToStateAndSave(T::toCompleted, request);
    }

    private List<T> getRequestsToSave(List<Long> ids, Source source) {
        Set<Long> alreadyExistingRequestIds = repository.findAlreadyExistingRequestIds(ids, source.toString());

        return ids.stream()
                .filter(id -> !alreadyExistingRequestIds.contains(id))
                .map(id -> mapper.from(id, source))
                .toList();
    }

    private void setToStateAndSave(Consumer<T> setToState, T request) {
        setToState.accept(request);
        repository.save(request);
    }
    
}
