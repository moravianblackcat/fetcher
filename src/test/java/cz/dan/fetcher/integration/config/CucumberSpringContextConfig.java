package cz.dan.fetcher.integration.config;

import cz.dan.integrationtests.kafka.KafkaAutoConfiguration;
import io.cucumber.java.DataTableType;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@CucumberContextConfiguration
@SpringBootTest(classes = SpringTestConfig.class, webEnvironment = DEFINED_PORT)
@ActiveProfiles("integration-tests")
@ImportAutoConfiguration({KafkaAutoConfiguration.class})
public class CucumberSpringContextConfig {

    @DataTableType
    public Map<String, Object> transform(Map<String, String> entry) {
        Map<String, Object> transformedEntry = new HashMap<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

        entry.forEach((key, value) -> {
            if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
                transformedEntry.put(key, Boolean.parseBoolean(value));
            } else if (value == null) {
                transformedEntry.put(key, null);
            } else if (value.matches("-?\\d+")) {
                transformedEntry.put(key, Long.parseLong(value));
            } else if (value.matches("-?\\d+\\.\\d+")) {
                transformedEntry.put(key, Double.parseDouble(value));
            } else if (value.matches("\\d{4}-\\d{2}-\\d{2}")) {
                transformedEntry.put(key, LocalDate.parse(value, dateFormatter));
            } else {
                transformedEntry.put(key, value);
            }
        });
        return transformedEntry;
    }

}
