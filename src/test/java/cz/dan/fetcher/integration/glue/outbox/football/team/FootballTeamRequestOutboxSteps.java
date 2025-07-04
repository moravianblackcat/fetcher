package cz.dan.fetcher.integration.glue.outbox.football.team;

import com.fasterxml.jackson.core.type.TypeReference;
import cz.dan.avro.fetcher.outbox.FootballTeamOutboxPayload;
import cz.dan.fetcher.integration.fixture.request.football.team.FootballTeamRequestOutboxFixture;
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
public class FootballTeamRequestOutboxSteps {

    private static final String FOOTBALL_TEAM_RESPONSE_OUTBOX_TOPIC_NAME = "fetcher.response.football.team";

    private final ClassPathResourceUtil classPathResourceUtil;

    private final FootballTeamRequestOutboxFixture fixture;

    private final JdbcTemplate jdbcTemplate;

    private final TestKafkaConsumer testKafkaConsumer;

    @Given("Those football teams are prepared for dispatching:")
    public void thoseFootballTeamsArePreparedForDispatching(List<Map<String, Object>> teamsToDispatch) {
        fixture.saveFootballTeamRequestOutboxes(teamsToDispatch);
    }

    @Then("{int}s Football teams defined in {} are sent")
    public void footballTeamsDefinedInAreSent(int timeoutInSeconds, String messagesJsonPath) throws IOException {
        List<FootballTeamOutboxPayload> expected = classPathResourceUtil.getListFromJsonPath(messagesJsonPath,
                new TypeReference<>() {});
        List<FootballTeamOutboxPayload> actual = testKafkaConsumer
                .getNextMessagesFromTopicWithTimeout(expected.size(), FOOTBALL_TEAM_RESPONSE_OUTBOX_TOPIC_NAME, timeoutInSeconds);

        assertThat(actual)
                .isNotNull()
                .isEqualTo(expected);
    }

    @Then("{int}s There are no football teams prepared for dispatching")
    public void thereAreNoFootballTeamsPreparedForDispatching(int timeoutInSeconds) {
        assertNoRows(timeoutInSeconds, () ->
                jdbcTemplate.queryForObject("SELECT COUNT(*) FROM football_team_request_outbox", Integer.class));
    }

}
