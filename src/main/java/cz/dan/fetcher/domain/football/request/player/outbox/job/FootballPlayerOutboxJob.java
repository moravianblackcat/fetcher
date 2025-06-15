package cz.dan.fetcher.domain.football.request.player.outbox.job;

import cz.dan.fetcher.domain.football.request.player.outbox.entity.FootballPlayerRequestOutbox;
import cz.dan.fetcher.domain.outbox.job.request.RequestOutboxJob;
import cz.dan.fetcher.domain.outbox.sender.Sender;
import cz.dan.fetcher.domain.outbox.service.request.OutboxRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
public class FootballPlayerOutboxJob extends RequestOutboxJob<FootballPlayerRequestOutbox> {

    public FootballPlayerOutboxJob(FootballPlayerOutboxJobProperties jobProperties,
                                   OutboxRequestService<FootballPlayerRequestOutbox> outboxRequestService,
                                   Set<Sender<FootballPlayerRequestOutbox>> senders) {
        super(jobProperties, outboxRequestService, senders);
    }

}
