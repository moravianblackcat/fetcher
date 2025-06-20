package cz.dan.fetcher.infra.fetcher.api.sportmonks.football;

import cz.dan.fetcher.domain.football.request.team.entity.FootballTeamRequestOutbox;
import cz.dan.fetcher.infra.fetcher.api.sportmonks.football.client.SportmonksFootballApiClient;
import cz.dan.fetcher.infra.fetcher.api.sportmonks.football.dto.SportmonksFootballTeamProfileDto;
import cz.dan.fetcher.infra.fetcher.api.sportmonks.football.dto.SportmonksFootballTeamProfileDto.Data;
import cz.dan.fetcher.infra.fetcher.api.sportmonks.football.dto.SportmonksFootballTeamProfileDto.Data.Country;
import cz.dan.fetcher.infra.fetcher.api.sportmonks.football.dto.SportmonksFootballTeamProfileDto.Data.Venue;
import cz.dan.fetcher.infra.fetcher.api.sportmonks.football.dto.SportmonksFootballTeamProfileDtoMapper;
import cz.dan.fetcher.infra.fetcher.api.sportmonks.football.dto.SportmonksFootballTeamProfileDtoMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static cz.dan.fetcher.domain.source.Source.Sportmonks;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SportmonksFootballTeamFetcherTest {

    @Mock
    private SportmonksFootballApiClient sportmonksFootballApiClient;

    @Spy
    private final SportmonksFootballTeamProfileDtoMapper sportmonksFootballTeamProfileDtoMapper =
            new SportmonksFootballTeamProfileDtoMapperImpl();

    @InjectMocks
    private SportmonksFootballTeamFetcher sut;

    @Test
    void supportsSportmonksSource() {
        assertThat(sut.supports(Sportmonks)).isTrue();
    }

    @Test
    void mapsFetchedTeamProfileToOutbox() throws Exception {
        when(sportmonksFootballApiClient.getTeamProfile(1L)).thenReturn(
                new SportmonksFootballTeamProfileDto(Data.builder()
                        .id(451)
                        .country(new Country("CZE"))
                        .founded(1913)
                        .name("FC Zbrojovka Brno")
                        .venue(new Venue("Městský fotbalový stadion Srbská", "Brno"))
                        .build())
        );

        FootballTeamRequestOutbox result = sut.get(1L);

        assertThat(result)
                .isNotNull()
                .hasNoNullFieldsOrPropertiesExcept("id")
                .extracting(
                        FootballTeamRequestOutbox::getName,
                        FootballTeamRequestOutbox::getCity,
                        FootballTeamRequestOutbox::getFounded,
                        FootballTeamRequestOutbox::getStadium,
                        FootballTeamRequestOutbox::getCountry
                )
                .containsExactly(
                        "FC Zbrojovka Brno",
                        "Brno",
                        1913,
                        "Městský fotbalový stadion Srbská",
                        "CZE"
                );
    }

}