package cz.dan.fetcher.infra.fetcher.api.football.sportmonks.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record SportmonksFootballPlayerProfileDto(Data data) {

    @Builder
    public record Data(long id, @JsonProperty("firstname") String firstName,
                       @JsonProperty("lastname") String lastName, String name,
                       @JsonProperty("display_name") String displayName,
                       @JsonProperty("date_of_birth") LocalDate dateOfBirth,
                       Nationality nationality, Position position) {

        @Builder
        public record Nationality(String iso3) {}

        @Builder
        public record Position(PositionCode code) {}

        public enum PositionCode {
            goalkeeper,
            defender,
            midfielder,
            attacker
        }
    }

}
