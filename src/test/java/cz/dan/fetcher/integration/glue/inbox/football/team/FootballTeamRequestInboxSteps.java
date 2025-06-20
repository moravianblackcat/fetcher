package cz.dan.fetcher.integration.glue.inbox.football.team;

import cz.dan.avro.fetcher.Source;
import cz.dan.avro.fetcher.request.FootballTeamRequest;
import cz.dan.fetcher.integration.fixture.request.football.team.FootballTeamRequestFixture;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Map;

import static cz.dan.fetcher.integration.await.AwaitHelper.assertRows;

@RequiredArgsConstructor
public class FootballTeamRequestInboxSteps {

    private static final String FOOTBALL_TEAM_REQUEST_INBOX_TOPIC_NAME = "fetcher.request.football.team";

    private final FootballTeamRequestFixture fixture;

    private final JdbcTemplate jdbcTemplate;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Given("Football team request for team ID {} from {} exists as {}")
    public void footballTeamRequestForTeamIdFromAlreadyExistsAs(long teamId, String source, String state) {
        fixture.saveFootballTeamRequest(teamId, source, state);
    }

    @When("The following list of football teams is requested to be fetched from {}:")
    public void theFollowingListOfFootballTeamsIsRequestedToBeFetchedFrom(String source, List<Long> teamIds) {
        FootballTeamRequest request = FootballTeamRequest.newBuilder()
                .setIds(teamIds)
                .setSource(Source.valueOf(source))
                .build();

        sendFootballTeamRequest(request);
    }

    @Then("{int}s Those football team requests are persisted:")
    public void thoseFootballTeamRequestsArePersistedAs(int timeoutInSeconds,
                                                          List<Map<String, Object>> expectedRequests) {
        assertRows(timeoutInSeconds, this::getActualRequests, expectedRequests);
    }

    private List<Map<String, Object>> getActualRequests() {
        return jdbcTemplate.query("SELECT id, source, state FROM football_team_request;",
                (rs, rowNum) -> Map.of(
                        "id", rs.getLong(1),
                        "source", rs.getString(2),
                        "state", rs.getString(3)
                ));
    }

    private void sendFootballTeamRequest(FootballTeamRequest request) {
        kafkaTemplate.send(new ProducerRecord<>(FOOTBALL_TEAM_REQUEST_INBOX_TOPIC_NAME, request));
    }

}
