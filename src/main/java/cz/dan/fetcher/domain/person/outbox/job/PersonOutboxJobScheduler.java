package cz.dan.fetcher.domain.person.outbox.job;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Profile("!integration-tests")
public class PersonOutboxJobScheduler {

    private final PersonOutboxJob job;

    @Scheduled(fixedDelayString = "${cz.dan.fetcher.job.fixed-delay}")
    @SchedulerLock(name = "personOutboxJob", lockAtMostFor = "10m")
    public void schedule() {
        job.run();
    }

}
