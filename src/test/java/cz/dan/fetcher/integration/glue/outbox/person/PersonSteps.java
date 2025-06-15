package cz.dan.fetcher.integration.glue.outbox.person;

import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cz.dan.fetcher.integration.await.AwaitHelper.assertNoRows;
import static cz.dan.fetcher.integration.await.AwaitHelper.assertRows;

@RequiredArgsConstructor
public class PersonSteps {

    private final JdbcTemplate jdbcTemplate;

    @Then("{int}s Those persons are saved:")
    public void thosePersonsAreSaved(int timeoutInSeconds, List<Map<String, Object>> expectedPersons) {
        assertRows(timeoutInSeconds, this::getPersistedPersons, expectedPersons);
    }

    @Then("{int}s No persons are saved")
    public void noPersonsAreSaved(int timeoutInSeconds) {
        assertNoRows(timeoutInSeconds, () -> jdbcTemplate.queryForObject("SELECT COUNT(*) FROM person", Integer.class));
    }

    private List<Map<String, Object>> getPersistedPersons() {
        return jdbcTemplate.query("SELECT * FROM person",
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", rs.getLong(1));
                    row.put("source_id", rs.getObject(2, Long.class));
                    row.put("source", rs.getString(3));

                    return row;
                });
    }

}
