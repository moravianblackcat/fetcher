package cz.dan.fetcher.integration.fixture;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import cz.dan.fetcher.integration.wiremock.common.WireMockFixtures;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SportmonksRestApiFixture {
    
    private final WireMock sportmonkRestApiWireMock;

    public void stubResponseFromClasspathJsonForEndpoint(String classpathJsonPath, String endpoint,
                                                         Map<String, StringValuePattern> queryParameters) {
        WireMockFixtures.stubResponseFromClasspathJsonForEndpoint(sportmonkRestApiWireMock, classpathJsonPath, endpoint,
                queryParameters);
    }

    public void stubStatusCodeForEndpoint(int statusCode, String endpoint,
                                          Map<String, StringValuePattern> queryParameters) {
        WireMockFixtures.stubStatusCodeForEndpoint(sportmonkRestApiWireMock, statusCode, endpoint, queryParameters);
    }
    
}
