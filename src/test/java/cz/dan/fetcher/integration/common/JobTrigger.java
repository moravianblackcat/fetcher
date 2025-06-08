package cz.dan.fetcher.integration.common;

import cz.dan.fetcher.domain.football.request.player.outbox.job.FootballPlayerJob;
import cz.dan.fetcher.domain.football.request.player.outbox.job.FootballPlayerOutboxJob;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

@RequiredArgsConstructor
public class JobTrigger {

    private final ExecutorService executorService = newSingleThreadExecutor();

    private final FootballPlayerJob footballPlayerJob;

    private final FootballPlayerOutboxJob footballPlayerOutboxJob;

    @When("{} job is triggered")
    public void jobIsTriggered(String jobName) {
        if ("footballPlayer".equals(jobName)) {
            executeJobInDifferentThread(footballPlayerJob::run);
        } else if ("footballPlayerOutbox".equals(jobName)) {
            executeJobInDifferentThread(footballPlayerOutboxJob::run);
        }
    }

    private void executeJobInDifferentThread(Runnable job) {
        executorService.execute(job);
    }

}