package cz.dan.fetcher.domain.football.request.player.outbox.job;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FootballPlayerJobScheduler {

    private final FootballPlayerJob job;

    @Scheduled(fixedDelayString = "${cz.dan.fetcher.job.fixed-delay}")
    @SchedulerLock(name = "footballPlayerJob", lockAtMostFor = "10m")
    public void schedule() {
        job.run();
    }

}

