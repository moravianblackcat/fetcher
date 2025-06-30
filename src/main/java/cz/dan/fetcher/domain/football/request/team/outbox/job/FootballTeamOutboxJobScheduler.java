package cz.dan.fetcher.domain.football.request.team.outbox.job;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FootballTeamOutboxJobScheduler {

    private final FootballTeamOutboxJob job;

    @Scheduled(fixedDelayString = "${cz.dan.fetcher.job.fixed-delay}")
    @SchedulerLock(name = "footballTeamOutboxJob", lockAtMostFor = "10m")
    public void schedule() {
        job.run();
    }

}

