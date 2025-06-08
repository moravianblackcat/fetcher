package cz.dan.fetcher.domain.football.request.player.outbox.job;

import cz.dan.fetcher.domain.football.request.player.inbox.entity.FootballPlayerRequest;
import cz.dan.fetcher.domain.football.request.player.inbox.service.FootballPlayerInboxRequestService;
import cz.dan.fetcher.domain.football.request.player.outbox.entity.FootballPlayerRequestOutbox;
import cz.dan.fetcher.domain.football.request.player.outbox.service.FootballPlayerRequestOutboxService;
import cz.dan.fetcher.domain.outbox.fetcher.Fetcher;
import cz.dan.fetcher.domain.outbox.job.request.RequestJob;
import cz.dan.fetcher.domain.outbox.job.request.RequestJobProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
public class FootballPlayerJob extends RequestJob<FootballPlayerRequestOutbox, FootballPlayerRequest> {

    public FootballPlayerJob(Set<Fetcher<FootballPlayerRequestOutbox>> fetchers,
                             FootballPlayerInboxRequestService footballPlayerInboxRequestService,
                             FootballPlayerRequestOutboxService footballPlayerRequestOutboxService,
                             FootballPlayerRequestJobProperties properties,
                             RequestJobProcessor requestJobProcessor) {
        super(fetchers, footballPlayerInboxRequestService, footballPlayerRequestOutboxService,
                properties, requestJobProcessor);
    }

}
