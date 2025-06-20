package cz.dan.fetcher.infra.fetcher.api.sportmonks.football.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record SportmonksFootballTeamProfileDto(Data data) {

    @Builder
    public record Data(long id, String name, int founded, Venue venue, Country country) {

        @Builder
        public record Venue(String name, @JsonProperty("city_name") String cityName) {
        }

        @Builder
        public record Country(String iso3) {}
    }
}
