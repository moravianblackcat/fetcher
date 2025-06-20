package cz.dan.fetcher.domain.football.request.coach.outbox.job;

import cz.dan.fetcher.domain.football.request.coach.inbox.entity.FootballCoachRequest;
import cz.dan.fetcher.domain.inbox.service.request.InboxRequestService;
import cz.dan.fetcher.domain.outbox.fetcher.Fetcher;
import cz.dan.fetcher.domain.outbox.job.request.RequestJob;
import cz.dan.fetcher.domain.outbox.job.request.RequestJobProcessor;
import cz.dan.fetcher.domain.outbox.service.request.OutboxRequestService;
import cz.dan.fetcher.domain.person.entity.Person;
import cz.dan.fetcher.domain.person.entity.PersonOutbox;
import cz.dan.fetcher.domain.person.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
public class FootballCoachJob extends RequestJob<PersonOutbox, FootballCoachRequest> {

    private final PersonService personService;

    public FootballCoachJob(Set<Fetcher<PersonOutbox, FootballCoachRequest>> fetchers,
                            InboxRequestService<FootballCoachRequest> inboxRequestService,
                            OutboxRequestService<PersonOutbox> outboxRequestService,
                            FootballCoachRequestJobProperties properties,
                            RequestJobProcessor requestJobProcessor,
                            PersonService personService) {
        super(fetchers, inboxRequestService, outboxRequestService, properties, requestJobProcessor);
        this.personService = personService;
    }

    @Override
    protected Long saveInternally(FootballCoachRequest request) {
        return personService.save(Person.builder().source(request.getSource()).sourceId(request.getId()).build());
    }

}
