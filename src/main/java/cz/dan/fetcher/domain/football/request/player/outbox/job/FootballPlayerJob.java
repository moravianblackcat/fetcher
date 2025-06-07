package cz.dan.fetcher.domain.football.request.player.outbox.job;

import cz.dan.fetcher.domain.football.request.player.inbox.entity.FootballPlayerRequest;
import cz.dan.fetcher.domain.football.request.player.inbox.service.FootballPlayerRequestService;
import cz.dan.fetcher.domain.football.request.player.outbox.entity.FootballPlayerRequestOutbox;
import cz.dan.fetcher.domain.football.request.player.outbox.service.FootballPlayerRequestOutboxService;
import cz.dan.fetcher.domain.outbox.fetcher.FootballPlayerFetcher;
import cz.dan.fetcher.domain.outbox.job.request.RequestJob;
import cz.dan.fetcher.domain.outbox.job.request.RequestJobProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
public class FootballPlayerJob extends RequestJob<FootballPlayerRequestOutbox, FootballPlayerRequest> {

    private final Set<FootballPlayerFetcher> fetchers;

    private final FootballPlayerRequestOutboxService footballPlayerRequestOutboxService;

    @Autowired
    public FootballPlayerJob(FootballPlayerRequestService footballPlayerRequestService,
                             FootballPlayerRequestJobProperties properties,
                             RequestJobProcessor requestJobProcessor,
                             Set<FootballPlayerFetcher> fetchers,
                             FootballPlayerRequestOutboxService footballPlayerRequestOutboxService) {
        super(footballPlayerRequestService, properties, requestJobProcessor);
        this.fetchers = fetchers;
        this.footballPlayerRequestOutboxService = footballPlayerRequestOutboxService;
    }

    @Override
    protected FootballPlayerRequestOutbox fetch(FootballPlayerRequest request) throws Exception {
        return getFetcherForRequest(request).getFootballPlayer(request.getId());
    }

    @Override
    protected String getJobIdentifier() {
        return "Football Player";
    }

    @Override
    protected void save(FootballPlayerRequestOutbox outbox) {
        footballPlayerRequestOutboxService.save(outbox);
    }

    private FootballPlayerFetcher getFetcherForRequest(FootballPlayerRequest request) {
        return fetchers.stream()
                .filter(fetcher -> fetcher.supports(request.getSource()))
                .findFirst()
                .orElseThrow();
    }

}
