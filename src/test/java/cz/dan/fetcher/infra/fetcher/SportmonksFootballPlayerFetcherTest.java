package cz.dan.fetcher.infra.fetcher;

import cz.dan.fetcher.football.request.player.outbox.domain.entity.FootballPlayerRequestOutbox;
import cz.dan.fetcher.infra.fetcher.api.football.SportmonksFootballApiClient;
import cz.dan.fetcher.infra.fetcher.api.football.dto.SportmonksFootballPlayerProfileDto;
import cz.dan.fetcher.infra.fetcher.api.football.dto.SportmonksFootballPlayerProfileDto.Data;
import cz.dan.fetcher.infra.fetcher.api.football.dto.SportmonksFootballPlayerProfileDto.Data.Nationality;
import cz.dan.fetcher.infra.fetcher.api.football.dto.SportmonksFootballPlayerProfileDto.Data.Position;
import cz.dan.fetcher.infra.fetcher.api.football.dto.SportmonksFootballPlayerProfileDtoMapper;
import cz.dan.fetcher.infra.fetcher.api.football.dto.SportmonksFootballPlayerProfileDtoMapperImpl;
import cz.dan.fetcher.outbox.domain.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.stream.Stream;

import static cz.dan.fetcher.infra.fetcher.api.football.dto.SportmonksFootballPlayerProfileDto.Data.PositionCode.midfielder;
import static cz.dan.fetcher.outbox.domain.entity.Source.Sportmonks;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SportmonksFootballPlayerFetcherTest {

    @Mock
    private SportmonksFootballApiClient sportmonksFootballApiClient;

    @Spy
    private final SportmonksFootballPlayerProfileDtoMapper sportmonksFootballPlayerProfileDtoMapper
            = new SportmonksFootballPlayerProfileDtoMapperImpl();

    @InjectMocks
    private SportmonksFootballPlayerFetcher sut;

    @Test
    void supportsSportmonksSource() {
        assertThat(sut.supports(Sportmonks)).isTrue();
    }

    @Test
    void mapsFetchedPlayerProfileToOutbox() throws Exception {
        when(sportmonksFootballApiClient.getPlayerProfile(1L)).thenReturn(
                new SportmonksFootballPlayerProfileDto(Data.builder()
                        .id(1L)
                        .firstName("Karel")
                        .lastName("Poborský")
                        .name("Karel Poborský")
                        .displayName("Karel Poborsky")
                        .dateOfBirth(LocalDate.of(1972, 3, 30))
                        .nationality(Nationality.builder().iso3("CZE").build())
                        .position(Position.builder().code(midfielder).build())
                        .build())
        );

        FootballPlayerRequestOutbox result = sut.getFootballPlayer(1L);

        assertThat(result)
                .isNotNull()
                .hasNoNullFieldsOrPropertiesExcept("id")
                .extracting(
                        FootballPlayerRequestOutbox::getSourceId,
                        FootballPlayerRequestOutbox::getNationality,
                        FootballPlayerRequestOutbox::getPosition,
                        FootballPlayerRequestOutbox::getFirstName,
                        FootballPlayerRequestOutbox::getLastName,
                        FootballPlayerRequestOutbox::getName,
                        FootballPlayerRequestOutbox::getDisplayName,
                        FootballPlayerRequestOutbox::getDateOfBirth,
                        FootballPlayerRequestOutbox::getSource
                ).containsExactly(
                        1L,
                        "CZE",
                        FootballPlayerRequestOutbox.Position.midfielder,
                        "Karel",
                        "Poborský",
                        "Karel Poborský",
                        "Karel Poborsky",
                        LocalDate.of(1972, 3, 30),
                        FootballPlayerRequestOutbox.Source.Sportmonks
                );
    }

    @ParameterizedTest
    @MethodSource
    void propagatesExceptionIfClientThrowsSome(Exception exception, Class<Exception> type) throws Exception {
        doThrow(exception).when(sportmonksFootballApiClient).getPlayerProfile(1L);

        assertThatThrownBy(() -> sut.getFootballPlayer(1L)).isInstanceOf(type);
    }

    private static Stream<Arguments> propagatesExceptionIfClientThrowsSome() {
        return Stream.of(
                Arguments.of(new ResourceNotFoundException(), ResourceNotFoundException.class),
                Arguments.of(new Exception(), Exception.class)
        );
    }

}