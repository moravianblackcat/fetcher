package cz.dan.fetcher.outbox.domain.job;

import cz.dan.fetcher.inbox.domain.RequestService;
import cz.dan.fetcher.outbox.domain.entity.Request;
import cz.dan.fetcher.outbox.domain.exception.ResourceNotFoundException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public abstract class RequestJob<E, R extends Request> {

    protected final RequestService<R> requestService;

    protected final RequestJobProperties requestJobProperties;

    protected final RequestJobProcessor requestJobProcessor;

    protected RequestJob(RequestService<R> requestService, RequestJobProperties requestJobProperties,
                         RequestJobProcessor requestJobProcessor) {
        this.requestService = requestService;
        this.requestJobProperties = requestJobProperties;
        this.requestJobProcessor = requestJobProcessor;
    }

    public final void run() {
        List<R> requests = getRequests();
        requests.forEach(request -> requestJobProcessor.process(() -> process(request)));
    }

    protected abstract E fetch(R request) throws Exception;

    private List<R> getRequests() {
        List<R> oldestScheduled = requestService.getOldestScheduled(requestJobProperties.getChunk());
        log.info("Found {} requests for job {}.", oldestScheduled.size(), getJobIdentifier());

        return oldestScheduled;
    };

    protected abstract String getJobIdentifier();

    protected void handleNon2xxStatusCode(R request, int httpStatusCode, String message) {
        log.warn("{} status code returned for request ID {} in job {}.",
                httpStatusCode, request.getId(), getJobIdentifier());
        if (httpStatusCode == 429) {
            handleRepeatableError(request, message);
        } else if (httpStatusCode >= 400 && httpStatusCode < 500) {
            handleNonRepeatableError(request, message);
        } else {
            handleRepeatableError(request, message);
        }
    }

    protected void handleRequestForNotExistingResource(R request) {
        requestService.setToResourceNotFoundAndSave(request);
    }

    protected void handleRequestForError(R request, String message) {
        request.addFailureDetail(message);
        requestService.setToErrorAndSave(request);
    }

    protected void handleSuccessfulRequest(R request) {
        requestService.setToCompletedAndSave(request);
    }

    protected abstract void save(E outbox);

    protected void process(R request) {
        try {
            E outbox = fetch(request);
            save(outbox);
            handleSuccessfulRequest(request);
        } catch (ResourceNotFoundException e) {
            handleRequestForNotExistingResource(request);
        } catch (FeignException e) {
            handleNon2xxStatusCode(request, e.status(), e.getMessage());
        } catch (Exception e) {
            handleRequestForError(request, e.getMessage());
        }
    }

    private void handleRepeatableError(R request, String message) {
        request.addFailureDetail(message);
        if (scheduledWithEnoughRetriesLeft(request)) {
            requestService.setToRetryAndSave(request);
        }
        else if (enoughRetriesLeft(request)) {
            requestService.save(request);
        } else if (noRetriesLeft(request)) {
            requestService.setToErrorAndSave(request);
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
        requestService.setToErrorAndSave(request);
    }

}
