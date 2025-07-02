package cz.dan.fetcher.domain.football.request.player.outbox.job;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Profile("!integration-tests")
public class FootballPlayerOutboxJobScheduler {

    private final FootballPlayerOutboxJob job;

    @Scheduled(fixedDelayString = "${cz.dan.fetcher.job.fixed-delay}")
    @SchedulerLock(name = "footballPlayerOutboxJob", lockAtMostFor = "10m")
    public void schedule() {
        job.run();
    }

}
