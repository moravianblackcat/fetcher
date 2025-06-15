package cz.dan.fetcher.domain.outbox.job.request;

import cz.dan.fetcher.domain.inbox.entity.request.Request;
import cz.dan.fetcher.domain.inbox.service.request.InboxRequestService;
import cz.dan.fetcher.domain.outbox.entity.request.Outbox;
import cz.dan.fetcher.domain.outbox.exception.resource.ResourceNotFoundException;
import cz.dan.fetcher.domain.outbox.fetcher.Fetcher;
import cz.dan.fetcher.domain.outbox.service.request.OutboxRequestService;
import cz.dan.fetcher.domain.person.entity.Person;
import cz.dan.fetcher.domain.person.service.PersonService;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Slf4j
public abstract class RequestJob<E extends Outbox, R extends Request> {

    private final Set<Fetcher<E, R>> fetchers;

    private final InboxRequestService<R> inboxRequestService;

    private final OutboxRequestService<E> outboxRequestService;

    private final PersonService personService;

    private final RequestJobProperties requestJobProperties;

    private final RequestJobProcessor requestJobProcessor;

    protected RequestJob(Set<Fetcher<E, R>> fetchers,
                         InboxRequestService<R> requestService,
                         OutboxRequestService<E> outboxRequestService, PersonService personService,
                         RequestJobProperties requestJobProperties,
                         RequestJobProcessor requestJobProcessor) {
        this.fetchers = fetchers;
        this.inboxRequestService = requestService;
        this.outboxRequestService = outboxRequestService;
        this.personService = personService;
        this.requestJobProperties = requestJobProperties;
        this.requestJobProcessor = requestJobProcessor;
    }

    public final void run() {
        List<R> requests = getRequests();
        requests.forEach(request -> requestJobProcessor.process(() -> process(request)));
    }

    private List<R> getRequests() {
        List<R> oldestScheduled = inboxRequestService.getOldestForProcessing(requestJobProperties.getChunk());
        log.info("Found {} requests for job {}.", oldestScheduled.size(), requestJobProperties.getJobIdentifier());

        return oldestScheduled;
    }

    private void process(R request) {
        try {
            E outbox = fetch(request);
            Long savedPersonId = savePerson(request);
            save(outbox, savedPersonId);
            handleSuccessfulRequest(request);
        } catch (ResourceNotFoundException e) {
            handleRequestForNotExistingResource(request);
        } catch (FeignException e) {
            handleNon2xxStatusCode(request, e.status(), e.getMessage());
        } catch (Exception e) {
            handleRequestForError(request, e.getMessage());
        }
    }

    private E fetch(R request) throws Exception {
        return getFetcherForRequest(request).get(request.getId());
    }

    private Fetcher<E, R> getFetcherForRequest(R request) {
        return fetchers.stream()
                .filter(fetcher -> fetcher.supports(request.getSource()))
                .findFirst()
                .orElseThrow();
    }

    private Long savePerson(R request) {
        return personService.save(Person.builder().source(request.getSource()).sourceId(request.getId()).build());
    }

    private void save(E outbox, Long personId) {
        outbox.setId(personId);
        outboxRequestService.save(outbox);
    }

    private void handleSuccessfulRequest(R request) {
        inboxRequestService.setToCompletedAndSave(request);
    }

    private void handleRequestForNotExistingResource(R request) {
        inboxRequestService.setToResourceNotFoundAndSave(request);
    }

    private void handleNon2xxStatusCode(R request, int httpStatusCode, String message) {
        log.warn("{} status code returned for request ID {} in job {}.",
                httpStatusCode, request.getId(), requestJobProperties.getJobIdentifier());
        if (httpStatusCode == 429) {
            handleRepeatableError(request, message);
        } else if (httpStatusCode >= 400 && httpStatusCode < 500) {
            handleNonRepeatableError(request, message);
        } else {
            handleRepeatableError(request, message);
        }
    }

    private void handleRepeatableError(R request, String message) {
        request.addFailureDetail(message);
        if (scheduledWithEnoughRetriesLeft(request)) {
            inboxRequestService.setToRetryAndSave(request);
        }
        else if (enoughRetriesLeft(request)) {
            inboxRequestService.save(request);
        } else if (noRetriesLeft(request)) {
            inboxRequestService.setToErrorAndSave(request);
        }
    }

    private boolean scheduledWithEnoughRetriesLeft(R request) {
        return request.isScheduled() && request.getNumberOfFailures() < requestJobProperties.getMaxRetries();
    }

    private boolean enoughRetriesLeft(R request) {
        return request.getNumberOfFailures() < requestJobProperties.getMaxRetries();
    }

    private boolean noRetriesLeft(R request) {
        return request.getNumberOfFailures() >= requestJobProperties.getMaxRetries();
    }

    private void handleNonRepeatableError(R request, String message) {
        request.addFailureDetail(message);
        inboxRequestService.setToErrorAndSave(request);
    }

    private void handleRequestForError(R request, String message) {
        request.addFailureDetail(message);
        inboxRequestService.setToErrorAndSave(request);
    }

}
