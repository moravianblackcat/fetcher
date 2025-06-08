package cz.dan.fetcher.integration.util.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.time.temporal.ChronoUnit.SECONDS;

@Component
public class TestKafkaConsumer {

    private final Map<String, Integer> topics = new HashMap<>();

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public TestKafkaConsumer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public <T> List<T> getNextMessagesFromTopicWithTimeout(int numberOfMessages, String topic,
                                                           int timeoutInSeconds) {
        Integer nextTopicOffset = topics.getOrDefault(topic, 0);

        List<T> result = IntStream.range(0, numberOfMessages)
                .mapToObj(offset ->
                        kafkaTemplate.receive(topic, 0, offset, Duration.of(timeoutInSeconds, SECONDS))
                                .value())
                .map(message -> (T) message)
                .toList();

        topics.put(topic, nextTopicOffset + numberOfMessages);

        return result;
    }
}
