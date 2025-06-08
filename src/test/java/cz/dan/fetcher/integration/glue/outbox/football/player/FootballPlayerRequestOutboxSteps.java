package cz.dan.fetcher.integration.glue.outbox.football.player;

import com.fasterxml.jackson.core.type.TypeReference;
import cz.dan.avro.fetcher.outbox.FootballPlayerOutboxPayload;
import cz.dan.fetcher.integration.util.ClassPathResourceUtil;
import cz.dan.fetcher.integration.util.kafka.TestKafkaConsumer;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static cz.dan.fetcher.integration.await.AwaitHelper.assertNoRows;
import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class FootballPlayerRequestOutboxSteps {

    private static final String FOOTBALL_PLAYER_RESPONSE_OUTBOX_TOPIC_NAME = "fetcher.response.football.player";

    private final ClassPathResourceUtil classPathResourceUtil;

    private final JdbcTemplate jdbcTemplate;

    private final TestKafkaConsumer testKafkaConsumer;

    @Given("Those football players are prepared for dispatching:")
    public void thoseFootballPlayersArePreparedForDispatching(List<Map<String, Object>> playersToDispatch) {
        String statement = "INSERT INTO football_player_request_outbox VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        jdbcTemplate.batchUpdate(statement, new FootballPlayerOutboxBatchPreparedStatementSetter(playersToDispatch));
    }

    @Then("{int}s Football players defined in {} are sent")
    public void footballPlayersDefinedInAreSent(int timeoutInSeconds, String messagesJsonPath) throws IOException {
        List<FootballPlayerOutboxPayload> expected = classPathResourceUtil.getListFromJsonPath(messagesJsonPath,
                new TypeReference<>() {});
        List<FootballPlayerOutboxPayload> actual = testKafkaConsumer
                .getNextMessagesFromTopicWithTimeout(expected.size(), FOOTBALL_PLAYER_RESPONSE_OUTBOX_TOPIC_NAME, timeoutInSeconds);

        assertThat(actual)
                .isNotNull()
                .isEqualTo(expected);
    }

    @Then("{int}s There are no football players prepared for dispatching")
    public void thereAreNoFootballPlayersPreparedForDispatching(int timeoutInSeconds) {
        assertNoRows(timeoutInSeconds, () ->
                jdbcTemplate.queryForObject("SELECT COUNT(*) FROM football_player_request_outbox", Integer.class));
    }

    private record FootballPlayerOutboxBatchPreparedStatementSetter(List<Map<String, Object>> playersToDispatch)
            implements BatchPreparedStatementSetter {

        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {
            Map<String, Object> player = playersToDispatch.get(i);
            ps.setLong(1, (Long) player.get("id"));
            ps.setLong(2, (Long) player.get("sourceId"));
            ps.setString(3, (String) player.get("nationality"));
            ps.setString(4, (String) player.get("position"));
            ps.setString(5, (String) player.get("firstName"));
            ps.setString(6, (String) player.get("lastName"));
            ps.setString(7, (String) player.get("name"));
            ps.setString(8, (String) player.get("displayName"));
            ps.setDate(9, Date.valueOf((LocalDate) player.get("dateOfBirth")));
            ps.setString(10, (String) player.get("source"));
        }

        @Override
        public int getBatchSize() {
            return playersToDispatch.size();
        }
    }
}
