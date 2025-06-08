package cz.dan.fetcher.domain.outbox.job.request;

import cz.dan.fetcher.domain.job.JobProperties;
import cz.dan.fetcher.domain.outbox.entity.request.Outbox;
import cz.dan.fetcher.domain.outbox.sender.Sender;
import cz.dan.fetcher.domain.outbox.service.request.OutboxRequestService;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public abstract class RequestOutboxJob<T extends Outbox> {

    private final JobProperties jobProperties;

    private final OutboxRequestService<T> outboxRequestService;

    private final Set<Sender<T>> senders;

    protected RequestOutboxJob(JobProperties jobProperties, OutboxRequestService<T> outboxRequestService,
                               Set<Sender<T>> senders) {
        this.jobProperties = jobProperties;
        this.outboxRequestService = outboxRequestService;
        this.senders = senders;
    }

    public void run() {
        Iterable<T> messages = fetchMessages();
        messages.forEach(this::sendAndDeleteMessage);
    }

    private Iterable<T> fetchMessages() {
        return outboxRequestService.getAll();
    }

    private void sendAndDeleteMessage(T outbox) {
        long id = outbox.getId();
        try {
            log.info("Sending outbox ID {} in {} job.", id, jobProperties.getJobIdentifier());
            senders.forEach(sender -> sender.sendOutbox(outbox));
            outboxRequestService.delete(id);
            log.info("Outbox ID {} in {} job successfully sent.", id, jobProperties.getJobIdentifier());
        } catch (RuntimeException e) {
            log.error("Encountered error when sending outbox ID %s in job %s."
                    .formatted(id, jobProperties.getJobIdentifier()),
                    e);
        }
    }

}
