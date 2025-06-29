package cz.dan.fetcher.integration.glue.outbox.football.player;

import cz.dan.fetcher.integration.glue.AssertUtil;
import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static cz.dan.await.AwaitHelper.*;

@RequiredArgsConstructor
public class FootballPlayerFetchingSteps {

    private final AssertUtil assertUtil;

    private final JdbcTemplate jdbcTemplate;

    @Then("{int}s Those football players are persisted to outbox:")
    public void thoseFootballPlayersArePersistedToOutbox(int timeoutInSeconds, List<Map<String, Object>> expectedPlayers) {
        assertRows(timeoutInSeconds, this::getPersistedPlayers, expectedPlayers);
    }

    @Then("{int}s Those football player requests are marked as {}:")
    public void thoseFootballPlayerRequestsAreMarkedAs(int timeoutInSeconds, String expectedRequestState,
                                                       List<Map<String, String>> playerIds) {
        playerIds.forEach(request -> {
            assertValue(timeoutInSeconds,
                    assertUtil.getActualState("football_player_request",
                            Long.valueOf(request.get("id")),
                            request.get("source")),
                    expectedRequestState);
        });
    }

    @Then("{int}s The football player request for {} has {string} in failure reason")
    public void theFootballPlayerRequestForHasFailureReason(int timeoutInSeconds, int requestId, String failureReason) {
        assertValueContains(timeoutInSeconds, getFailureReason(requestId), failureReason);
    }

    private List<Map<String, Object>> getPersistedPlayers() {
        return jdbcTemplate.query("SELECT id, nationality, position, first_name, last_name, "
                        + "name, display_name, date_of_birth FROM football_player_request_outbox",
                (rs, rowNum) -> Map.of(
                        "id", rs.getLong(1),
                        "nationality", rs.getString(2),
                        "position", rs.getString(3),
                        "first_name", rs.getString(4),
                        "last_name", rs.getString(5),
                        "name", rs.getString(6),
                        "display_name", rs.getString(7),
                        "date_of_birth", rs.getDate(8).toLocalDate()
                ));
    }

    private Supplier<String> getFailureReason(int playerId) {
        return () ->
                jdbcTemplate.queryForObject(
                        "SELECT reason FROM football_player_request_failure_detail "
                                + "WHERE player_id = ? AND source = 'Sportmonks';",
                        String.class,
                        playerId);
    }

}
