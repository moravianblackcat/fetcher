package cz.dan.fetcher.domain.football.request.coach.inbox.entity.mapper;

import cz.dan.fetcher.domain.football.request.coach.inbox.entity.FootballCoachRequest;
import org.junit.jupiter.api.Test;

import static cz.dan.fetcher.domain.football.request.entity.RequestState.SCHEDULED;
import static cz.dan.fetcher.domain.source.Source.Sportmonks;
import static org.assertj.core.api.Assertions.assertThat;

class FootballCoachRequestMapperImplTest {

    private final FootballCoachRequestMapper sut = new FootballCoachRequestMapperImpl();

    @Test
    void correctlyMapsFootballCoachRequestFromPlayerIdAndSource() {
        FootballCoachRequest result = sut.from(1L, Sportmonks);

        assertThat(result)
                .isNotNull()
                .extracting(
                        FootballCoachRequest::getId,
                        FootballCoachRequest::getSource,
                        FootballCoachRequest::getState
                ).containsExactly(
                        1L, Sportmonks, SCHEDULED
                );
    }

}