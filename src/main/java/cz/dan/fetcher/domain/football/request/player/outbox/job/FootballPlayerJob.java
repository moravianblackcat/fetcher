package cz.dan.fetcher.domain.football.request.player.outbox.job;

import cz.dan.fetcher.domain.football.request.player.inbox.entity.FootballPlayerRequest;
import cz.dan.fetcher.domain.football.request.player.outbox.entity.FootballPlayerRequestOutbox;
import cz.dan.fetcher.domain.inbox.service.request.InboxRequestService;
import cz.dan.fetcher.domain.outbox.fetcher.Fetcher;
import cz.dan.fetcher.domain.outbox.job.request.RequestJob;
import cz.dan.fetcher.domain.outbox.job.request.RequestJobProcessor;
import cz.dan.fetcher.domain.outbox.service.request.OutboxRequestService;
import cz.dan.fetcher.domain.person.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
public class FootballPlayerJob extends RequestJob<FootballPlayerRequestOutbox, FootballPlayerRequest> {

    public FootballPlayerJob(Set<Fetcher<FootballPlayerRequestOutbox, FootballPlayerRequest>> fetchers,
                             InboxRequestService<FootballPlayerRequest> inboxRequestService,
                             OutboxRequestService<FootballPlayerRequestOutbox> outboxRequestService,
                             PersonService personService,
                             FootballPlayerRequestJobProperties properties,
                             RequestJobProcessor requestJobProcessor) {
        super(fetchers, inboxRequestService, outboxRequestService, personService, properties, requestJobProcessor);
    }

}
