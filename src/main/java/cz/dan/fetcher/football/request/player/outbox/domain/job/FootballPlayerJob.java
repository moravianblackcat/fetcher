package cz.dan.fetcher.football.request.player.outbox.domain.job;

import cz.dan.fetcher.football.request.player.inbox.domain.entity.FootballPlayerRequest;
import cz.dan.fetcher.football.request.player.inbox.domain.service.FootballPlayerRequestService;
import cz.dan.fetcher.football.request.player.outbox.domain.entity.FootballPlayerRequestOutbox;
import cz.dan.fetcher.football.request.player.outbox.domain.service.FootballPlayerRequestOutboxService;
import cz.dan.fetcher.outbox.domain.fetcher.FootballPlayerFetcher;
import cz.dan.fetcher.outbox.domain.job.RequestJob;
import cz.dan.fetcher.outbox.domain.job.RequestJobProcessor;
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
