package cz.dan.fetcher.integration.glue.inbox.football.player;

import cz.dan.avro.fetcher.Source;
import cz.dan.avro.fetcher.request.FootballPlayerRequest;
import cz.dan.fetcher.integration.fixture.request.football.player.FootballPlayerRequestFixture;
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
public class FootballPlayerRequestInboxSteps {

    private static final String FOOTBALL_PLAYER_REQUEST_INBOX_TOPIC_NAME = "fetcher.request.football.player";

    private final FootballPlayerRequestFixture fixture;

    private final JdbcTemplate jdbcTemplate;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Given("Football player request for player ID {} from {} exists as {}")
    public void footballPlayerRequestForPlayerIdFromAlreadyExistsAs(long playerId, String source, String state) {
        fixture.saveFootballPlayerRequest(playerId, source, state);
    }

    @When("The following list of football players is requested to be fetched from {}:")
    public void theFollowingListOfFootballPlayersIsRequestedToBeFetchedFrom(String source, List<Long> playerIds) {
        FootballPlayerRequest request = FootballPlayerRequest.newBuilder()
                .setIds(playerIds)
                .setSource(Source.valueOf(source))
                .build();

        sendFootballPlayerRequest(request);
    }

    @Then("{int}s Those football player requests are persisted:")
    public void thoseFootballPlayerRequestsArePersistedAs(int timeoutInSeconds,
                                                          List<Map<String, Object>> expectedRequests) {
        assertRows(timeoutInSeconds, this::getActualRequests, expectedRequests);
    }

    private List<Map<String, Object>> getActualRequests() {
        return jdbcTemplate.query("SELECT id, source, state FROM football_player_request;",
                (rs, rowNum) -> Map.of(
                        "id", rs.getLong(1),
                        "source", rs.getString(2),
                        "state", rs.getString(3)
                ));
    }

    private void sendFootballPlayerRequest(FootballPlayerRequest request) {
        kafkaTemplate.send(new ProducerRecord<>(FOOTBALL_PLAYER_REQUEST_INBOX_TOPIC_NAME, request));
    }

}
