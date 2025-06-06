package cz.dan.fetcher.integration.wiremock.common;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public final class WireMockFixtures {

    private WireMockFixtures() {
        throw new UnsupportedOperationException();
    }

    public static void stubResponseFromClasspathJsonForEndpoint(WireMock wireMock, String classpathJsonPath,
                                                                String endpoint,
                                                                Map<String, StringValuePattern> queryParameters) {
        try {
            wireMock
                    .register((get(urlPathEqualTo(endpoint))
                            .withQueryParams(queryParameters)
                            .willReturn(aResponse()
                                    .withHeader("Content-Type", "application/json")
                                    .withBody(StreamUtils.copyToString(new ClassPathResource(classpathJsonPath)
                                            .getInputStream(), StandardCharsets.UTF_8)))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void stubResponseFromClasspathJsonForEndpoint(WireMock wireMock, String classpathJsonPath,
                                                                String endpoint) {
        try {
            wireMock
                    .register((get(urlPathEqualTo(endpoint))
                            .willReturn(aResponse()
                                    .withHeader("Content-Type", "application/json")
                                    .withBody(StreamUtils.copyToString(new ClassPathResource(classpathJsonPath)
                                            .getInputStream(), StandardCharsets.UTF_8)))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void stubStatusCodeForEndpoint(WireMock wireMock, int statusCode, String endpoint,
                                                 Map<String, StringValuePattern> queryParameters) {
        wireMock
                .register((get(urlPathEqualTo(endpoint))
                        .withQueryParams(queryParameters)
                        .willReturn(aResponse().withStatus(statusCode))));
    }
}
