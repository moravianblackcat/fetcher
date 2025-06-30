package cz.dan.fetcher.domain.football.request.coach.outbox.job;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FootballCoachJobScheduler {

    private final FootballCoachJob job;

    @Scheduled(fixedDelayString = "${cz.dan.fetcher.job.fixed-delay}")
    @SchedulerLock(name = "footballCoachJob", lockAtMostFor = "10m")
    public void schedule() {
        job.run();
    }

}
