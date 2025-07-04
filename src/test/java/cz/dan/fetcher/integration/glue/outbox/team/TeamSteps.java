package cz.dan.fetcher.integration.glue.outbox.team;

import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cz.dan.integrationtests.util.AwaitHelper.assertNoRows;
import static cz.dan.integrationtests.util.AwaitHelper.assertRows;

@RequiredArgsConstructor
public class TeamSteps {

    private final JdbcTemplate jdbcTemplate;

    @Then("{int}s Those teams are saved:")
    public void thoseTeamsAreSaved(int timeoutInSeconds, List<Map<String, Object>> expectedTeams) {
        assertRows(timeoutInSeconds, this::getPersistedTeams, expectedTeams);
    }

    @Then("{int}s No teams are saved")
    public void noTeamsAreSaved(int timeoutInSeconds) {
        assertNoRows(timeoutInSeconds, () -> jdbcTemplate.queryForObject("SELECT COUNT(*) FROM team", Integer.class));
    }

    private List<Map<String, Object>> getPersistedTeams() {
        return jdbcTemplate.query("SELECT * FROM team",
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", rs.getLong(1));
                    row.put("source_id", rs.getObject(2, Long.class));
                    row.put("source", rs.getString(3));

                    return row;
                });
    }
}
