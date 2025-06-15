package cz.dan.fetcher.domain.person.outbox.job;

import cz.dan.fetcher.domain.outbox.job.request.RequestOutboxJob;
import cz.dan.fetcher.domain.outbox.sender.Sender;
import cz.dan.fetcher.domain.outbox.service.request.OutboxRequestService;
import cz.dan.fetcher.domain.person.entity.PersonOutbox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
public class PersonOutboxJob extends RequestOutboxJob<PersonOutbox> {

    public PersonOutboxJob(PersonOutboxJobProperties properties,
                           OutboxRequestService<PersonOutbox> outboxRequestService,
                           Set<Sender<PersonOutbox>> senders) {
        super(properties, outboxRequestService, senders);
    }

}
