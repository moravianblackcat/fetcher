package cz.dan.fetcher.domain.football.request.coach.outbox.job;

import cz.dan.fetcher.domain.football.request.coach.inbox.entity.FootballCoachRequest;
import cz.dan.fetcher.domain.inbox.service.request.InboxRequestService;
import cz.dan.fetcher.domain.outbox.fetcher.Fetcher;
import cz.dan.fetcher.domain.outbox.job.request.RequestJob;
import cz.dan.fetcher.domain.outbox.job.request.RequestJobProcessor;
import cz.dan.fetcher.domain.outbox.service.request.OutboxRequestService;
import cz.dan.fetcher.domain.person.entity.PersonOutbox;
import cz.dan.fetcher.domain.person.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
public class FootballCoachJob extends RequestJob<PersonOutbox, FootballCoachRequest> {

    public FootballCoachJob(Set<Fetcher<PersonOutbox, FootballCoachRequest>> fetchers,
                            InboxRequestService<FootballCoachRequest> inboxRequestService,
                            OutboxRequestService<PersonOutbox> outboxRequestService,
                            PersonService personService,
                            FootballCoachRequestJobProperties properties,
                            RequestJobProcessor requestJobProcessor) {
        super(fetchers, inboxRequestService, outboxRequestService, personService, properties, requestJobProcessor);
    }

}
