package cz.dan.fetcher.integration.common;

import com.github.tomakehurst.wiremock.matching.EqualToPattern;
import cz.dan.fetcher.integration.fixture.SportmonksRestApiFixture;
import io.cucumber.java.en.Given;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class SportmonksCommonSteps {

    private final SportmonksRestApiFixture sportmonksRestApiFixture;

    @Given("Sportmonks API returns response defined in {} for the {} endpoint with those inclusions:")
    public void sportmonksApiReturnsResponse(String responseJsonPath, String endpoint, List<String> inclusions) {
        sportmonksRestApiFixture.stubResponseFromClasspathJsonForEndpoint(responseJsonPath, endpoint,
                Map.of("include", new EqualToPattern(String.join(";", inclusions))));
    }

    @Given("Sportmonks API returns {} status code for the {} endpoint with those inclusions:")
    public void sportmonksApiReturnsStatusCode(int statusCode, String endpoint, List<String> inclusions) {
        sportmonksRestApiFixture.stubStatusCodeForEndpoint(statusCode, endpoint, Map.of("include", new EqualToPattern(String.join(";", inclusions))));
    }

}
