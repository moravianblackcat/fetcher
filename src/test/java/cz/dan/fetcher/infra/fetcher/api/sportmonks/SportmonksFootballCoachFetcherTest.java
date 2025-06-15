package cz.dan.fetcher.infra.fetcher.api.sportmonks;

import cz.dan.fetcher.domain.outbox.exception.resource.ResourceNotFoundException;
import cz.dan.fetcher.domain.person.entity.PersonOutbox;
import cz.dan.fetcher.infra.fetcher.api.sportmonks.football.client.SportmonksFootballApiClient;
import cz.dan.fetcher.infra.fetcher.api.sportmonks.football.dto.SportmonksFootballCoachProfileDto;
import cz.dan.fetcher.infra.fetcher.api.sportmonks.football.dto.SportmonksFootballCoachProfileDto.Data;
import cz.dan.fetcher.infra.fetcher.api.sportmonks.football.dto.SportmonksFootballCoachProfileDto.Data.Nationality;
import cz.dan.fetcher.infra.fetcher.api.sportmonks.football.dto.SportmonksFootballCoachProfileDtoMapper;
import cz.dan.fetcher.infra.fetcher.api.sportmonks.football.dto.SportmonksFootballCoachProfileDtoMapperImpl;
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

import static cz.dan.fetcher.domain.source.Source.Sportmonks;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SportmonksFootballCoachFetcherTest {

    @Mock
    private SportmonksFootballApiClient sportmonksFootballApiClient;

    @Spy
    private final SportmonksFootballCoachProfileDtoMapper sportmonksFootballCoachProfileDtoMapper
            = new SportmonksFootballCoachProfileDtoMapperImpl();

    @InjectMocks
    private SportmonksFootballCoachFetcher sut;

    @Test
    void supportsSportmonksSource() {
        assertThat(sut.supports(Sportmonks)).isTrue();
    }

    @Test
    void mapsFetcherCoachToPersonOutbox() throws Exception {
        when(sportmonksFootballApiClient.getCoachProfile(1L)).thenReturn(
                SportmonksFootballCoachProfileDto.builder()
                        .data(Data.builder()
                                .id(1L)
                                .firstName("Brian")
                                .lastName("Clough")
                                .name("Brian Howard Clough")
                                .displayName("Brian Clough")
                                .dateOfBirth(LocalDate.of(1935, 3, 21))
                                .nationality(Nationality.builder().iso3("ENG").build())
                                .build())
                        .build()
        );

        PersonOutbox result = sut.get(1L);

        assertThat(result)
                .isNotNull()
                .hasNoNullFieldsOrPropertiesExcept("id")
                .extracting(
                        PersonOutbox::getNationality,
                        PersonOutbox::getFirstName,
                        PersonOutbox::getLastName,
                        PersonOutbox::getName,
                        PersonOutbox::getDisplayName,
                        PersonOutbox::getDateOfBirth
                ).containsExactly(
                        "ENG",
                        "Brian",
                        "Clough",
                        "Brian Howard Clough",
                        "Brian Clough",
                        LocalDate.of(1935, 3, 21)
                );
    }

    @ParameterizedTest
    @MethodSource
    void propagatesExceptionIfClientThrowsSome(Exception exception, Class<Exception> type) throws Exception {
        doThrow(exception).when(sportmonksFootballApiClient).getCoachProfile(1L);

        assertThatThrownBy(() -> sut.get(1L)).isInstanceOf(type);
    }

    private static Stream<Arguments> propagatesExceptionIfClientThrowsSome() {
        return Stream.of(
                Arguments.of(new ResourceNotFoundException(), ResourceNotFoundException.class),
                Arguments.of(new Exception(), Exception.class)
        );
    }

}