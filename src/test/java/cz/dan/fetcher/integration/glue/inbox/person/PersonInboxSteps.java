package cz.dan.fetcher.integration.glue.inbox.person;

import cz.dan.avro.fetcher.outbox.PersonRequest;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class PersonInboxSteps {

    private static final String PERSON_REQUEST_INBOX_TOPIC_NAME = "fetcher.request.person";

    private final KafkaTemplate<String, Object> kafkaTemplate;


    @When("The following persons are requested to be registered:")
    public void theFollowingPersonsAreRequestedToBeRegistered(List<Map<String, Object>> persons) {
        persons.stream().map(this::getPersonsRequest).forEach(this::sendPersonRequest);
    }

    private PersonRequest getPersonsRequest(Map<String, Object> person) {
        return PersonRequest.newBuilder()
                .setFirstName((String) person.get("firstName"))
                .setLastName((String) person.get("lastName"))
                .setName((String) person.get("name"))
                .setDisplayName((String) person.get("displayName"))
                .setNationality((String) person.get("nationality"))
                .setDateOfBirth((LocalDate) person.get("dateOfBirth"))
                .build();
    }

    private void sendPersonRequest(PersonRequest personRequest) {
        kafkaTemplate.send(new ProducerRecord<>(PERSON_REQUEST_INBOX_TOPIC_NAME, personRequest));
    }

}
