package cz.dan.fetcher.integration.config.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory,
                                                       ConsumerFactory<String, Object> consumerFactory) {
        KafkaTemplate<String, Object> result = new KafkaTemplate<>(producerFactory);
        result.setConsumerFactory(consumerFactory);

        return result;
    }

}
