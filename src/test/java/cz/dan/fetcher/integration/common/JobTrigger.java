package cz.dan.fetcher.integration.common;

import cz.dan.fetcher.domain.football.request.player.outbox.job.FootballPlayerJob;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

@RequiredArgsConstructor
public class JobTrigger {

    private final ExecutorService executorService = newSingleThreadExecutor();

    private final FootballPlayerJob footballPlayerJob;

    @When("{} job is triggered")
    public void jobIsTriggered(String jobName) {
        if ("footballPlayer".equals(jobName)) {
            footballPlayerJob.run();
//            executeJobInDifferentThread(footballPlayerJob::run);
        }
    }

    private void executeJobInDifferentThread(Runnable job) {
        executorService.execute(job);
    }

}
