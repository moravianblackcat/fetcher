package cz.dan.fetcher.domain.football.request.coach.outbox.job;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Profile("!integration-tests")
public class FootballCoachJobScheduler {

    private final FootballCoachJob job;

    @Scheduled(fixedDelayString = "${cz.dan.fetcher.job.fixed-delay}")
    @SchedulerLock(name = "footballCoachJob", lockAtMostFor = "10m")
    public void schedule() {
        job.run();
    }

}
