package cz.dan.fetcher.integration.glue.outbox.football.player;

import com.fasterxml.jackson.core.type.TypeReference;
import cz.dan.avro.fetcher.outbox.FootballPlayerOutboxPayload;
import cz.dan.fetcher.integration.fixture.request.football.player.FootballPlayerRequestOutboxFixture;
import cz.dan.integrationtests.kafka.TestKafkaConsumer;
import cz.dan.integrationtests.util.ClassPathResourceUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static cz.dan.integrationtests.util.AwaitHelper.assertNoRows;
import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class FootballPlayerRequestOutboxSteps {

    private static final String FOOTBALL_PLAYER_RESPONSE_OUTBOX_TOPIC_NAME = "fetcher.response.football.player";

    private final ClassPathResourceUtil classPathResourceUtil;

    private final FootballPlayerRequestOutboxFixture fixture;

    private final JdbcTemplate jdbcTemplate;

    private final TestKafkaConsumer testKafkaConsumer;

    @Given("Those football players are prepared for dispatching:")
    public void thoseFootballPlayersArePreparedForDispatching(List<Map<String, Object>> playersToDispatch) {
        fixture.saveFootballPlayerRequestOutboxes(playersToDispatch);
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

}
