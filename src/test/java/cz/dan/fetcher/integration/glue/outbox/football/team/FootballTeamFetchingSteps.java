package cz.dan.fetcher.integration.glue.outbox.football.team;

import cz.dan.fetcher.integration.glue.AssertUtil;
import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static cz.dan.fetcher.integration.await.AwaitHelper.*;

@RequiredArgsConstructor
public class FootballTeamFetchingSteps {

    private final AssertUtil assertUtil;

    private final JdbcTemplate jdbcTemplate;

    @Then("{int}s Those football teams are persisted to outbox:")
    public void thoseFootballTeamsArePersistedToOutbox(int timeoutInSeconds, List<Map<String, Object>> expectedTeams) {
        assertRows(timeoutInSeconds, this::getPersistedTeams, expectedTeams);
    }

    @Then("{int}s Those football team requests are marked as {}:")
    public void thoseFootballTeamRequestsAreMarkedAs(int timeoutInSeconds, String expectedRequestState,
                                                     List<Map<String, String>> teamIds) {
        teamIds.forEach(request -> {
            assertValue(timeoutInSeconds,
                    assertUtil.getActualState("football_team_request",
                            Long.valueOf(request.get("id")),
                            request.get("source")),
                    expectedRequestState);
        });
    }

    @Then("{int}s The football team request for {} has {string} in failure reason")
    public void theFootballTeamRequestForHasFailureReason(int timeoutInSeconds, int requestId, String failureReason) {
        assertValueContains(timeoutInSeconds, getFailureReason(requestId), failureReason);
    }

    private List<Map<String, Object>> getPersistedTeams() {
        return jdbcTemplate.query("SELECT id, name, founded, country, stadium, city FROM football_team_request_outbox",
                (rs, rowNum) -> Map.of(
                        "id", rs.getLong(1),
                        "name", rs.getString(2),
                        "founded", rs.getLong(3),
                        "country", rs.getString(4),
                        "stadium", rs.getString(5),
                        "city", rs.getString(6)
                ));
    }

    private Supplier<String> getFailureReason(int teamId) {
        return () ->
                jdbcTemplate.queryForObject(
                        "SELECT reason FROM football_team_request_failure_detail "
                                + "WHERE team_id = ? AND source = 'Sportmonks';",
                        String.class,
                        teamId);
    }

}
