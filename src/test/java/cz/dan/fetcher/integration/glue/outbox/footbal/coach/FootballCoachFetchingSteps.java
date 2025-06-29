package cz.dan.fetcher.integration.glue.outbox.footbal.coach;

import cz.dan.fetcher.integration.glue.AssertUtil;
import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static cz.dan.await.AwaitHelper.assertValue;
import static cz.dan.await.AwaitHelper.assertValueContains;

@RequiredArgsConstructor
public class FootballCoachFetchingSteps {

    private final AssertUtil assertUtil;

    private final JdbcTemplate jdbcTemplate;

    @Then("{int}s Those football coach requests are marked as {}:")
    public void thoseFootballCoachRequestsAreMarkedAs(int timeoutInSeconds, String expectedRequestState,
                                                      List<Map<String, String>> coachIds) {
        coachIds.forEach(request -> {
            assertValue(timeoutInSeconds,
                    assertUtil.getActualState("football_coach_request",
                            Long.valueOf(request.get("id")),
                            request.get("source")),
                    expectedRequestState);
        });
    }

    @Then("{int}s The football coach request for {} has {string} in failure reason")
    public void theFootballCoachRequestForHasFailureReason(int timeoutInSeconds, int requestId, String failureReason) {
        assertValueContains(timeoutInSeconds, getFailureReason(requestId), failureReason);
    }

    private Supplier<String> getFailureReason(int coachId) {
        return () ->
                jdbcTemplate.queryForObject(
                        "SELECT reason FROM football_coach_request_failure_detail "
                                + "WHERE coach_id = ? AND source = 'Sportmonks';",
                        String.class,
                        coachId);
    }

}
