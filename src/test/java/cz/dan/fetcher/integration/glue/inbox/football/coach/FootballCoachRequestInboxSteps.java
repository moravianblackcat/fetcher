package cz.dan.fetcher.integration.glue.inbox.football.coach;

import cz.dan.avro.fetcher.Source;
import cz.dan.avro.fetcher.request.FootballCoachRequest;
import cz.dan.fetcher.integration.fixture.request.football.coach.FootballCoachRequestFixture;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Map;

import static cz.dan.integrationtests.util.AwaitHelper.assertRows;

@RequiredArgsConstructor
public class FootballCoachRequestInboxSteps {

    private static final String FOOTBALL_COACH_REQUEST_INBOX_TOPIC_NAME = "fetcher.request.football.coach";

    private final FootballCoachRequestFixture fixture;

    private final JdbcTemplate jdbcTemplate;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Given("Football coach request for coach ID {} from {} exists as {}")
    public void footballCoachRequestForCoachIdFromExistsAs(long coachId, String source, String state) {
        fixture.saveFootballCoachRequest(coachId, source, state);
    }

    @When("The following list of football coaches is requested to be fetched from {}:")
    public void theFollowingListOfFootballCoachesIsRequestedToBeFetcherFrom(String source, List<Long> coachIds) {
        FootballCoachRequest request = FootballCoachRequest.newBuilder()
                .setIds(coachIds)
                .setSource(Source.valueOf(source))
                .build();

        sendFootballCoachRequest(request);
    }

    @Then("{int}s Those football coach requests are persisted:")
    public void thoseFootballCoachRequestsArePersistedAs(int timeoutInSeconds,
                                                         List<Map<String, Object>> expectedRequests) {
        assertRows(timeoutInSeconds, this::getActualRequests, expectedRequests);
    }

    private List<Map<String, Object>> getActualRequests() {
        return jdbcTemplate.query("SELECT id, source, state FROM football_coach_request;",
                (rs, rowNum) -> Map.of(
                        "id", rs.getLong(1),
                        "source", rs.getString(2),
                        "state", rs.getString(3)
                ));
    }

    private void sendFootballCoachRequest(FootballCoachRequest request) {
        kafkaTemplate.send(new ProducerRecord<>(FOOTBALL_COACH_REQUEST_INBOX_TOPIC_NAME, request));
    }

}
