package cz.dan.fetcher.integration.glue.outbox.person;

import com.fasterxml.jackson.core.type.TypeReference;
import cz.dan.avro.fetcher.outbox.PersonOutboxPayload;
import cz.dan.fetcher.integration.fixture.person.PersonFixture;
import cz.dan.fetcher.integration.util.ClassPathResourceUtil;
import cz.dan.fetcher.integration.util.kafka.TestKafkaConsumer;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cz.dan.await.AwaitHelper.assertNoRows;
import static cz.dan.await.AwaitHelper.assertRows;
import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class PersonOutboxSteps {

    private static final String PERSON_OUTBOX_TOPIC_NAME = "fetcher.person";

    private final ClassPathResourceUtil classPathResourceUtil;

    private final JdbcTemplate jdbcTemplate;


    private final TestKafkaConsumer testKafkaConsumer;
    private final PersonFixture personFixture;

    @Given("Those persons are prepared for dispatching:")
    public void thosePersonsArePreparedForDispatching(List<Map<String, Object>> personsToDispatch) {
        personFixture.savePersonOutboxes(personsToDispatch);
    }

    @Then("{int}s Persons defined in {} are sent")
    public void personsDefinedInAreSent(int timeoutInSeconds, String messagesJsonPath) throws IOException {
        List<PersonOutboxPayload> expected = classPathResourceUtil.getListFromJsonPath(messagesJsonPath,
                new TypeReference<>() {});
        List<PersonOutboxPayload> actual = testKafkaConsumer
                .getNextMessagesFromTopicWithTimeout(expected.size(), PERSON_OUTBOX_TOPIC_NAME, timeoutInSeconds);

        assertThat(actual)
                .isNotNull()
                .isEqualTo(expected);
    }

    @Then("{int}s There are no persons prepared for dispatching")
    public void thereAreNoPersonsPreparedForDispatching(int timeoutInSeconds) {
        assertNoRows(timeoutInSeconds, () ->
                jdbcTemplate.queryForObject("SELECT COUNT(*) FROM person_outbox", Integer.class));
    }

    @Then("{int}s Those persons are persisted to outbox:")
    public void thosePersonsArePersistedToOutbox(int timeoutInSeconds,
                                                         List<Map<String, Object>> expectedPersons) {
        assertRows(timeoutInSeconds, this::getPersistedPersons, expectedPersons);
    }

    private List<Map<String, Object>> getPersistedPersons() {
        return jdbcTemplate.query("SELECT id, nationality, first_name, last_name, "
                        + "name, display_name, date_of_birth FROM person_outbox",
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", rs.getLong(1));
                    row.put("nationality", rs.getString(2));
                    row.put("first_name", rs.getString(3));
                    row.put("last_name", rs.getString(4));
                    row.put("name", rs.getString(5));
                    row.put("display_name", rs.getString(6));
                    row.put("date_of_birth", rs.getDate(7) != null ? rs.getDate(7).toLocalDate() : null);

                    return row;
                });
    }
}
