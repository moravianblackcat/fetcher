package cz.dan.fetcher.domain.football.request.player.inbox.entity.mapper;

import cz.dan.fetcher.domain.football.request.player.inbox.entity.FootballPlayerRequest;
import org.junit.jupiter.api.Test;

import static cz.dan.fetcher.domain.football.request.entity.RequestState.SCHEDULED;
import static cz.dan.fetcher.domain.source.Source.Sportmonks;
import static org.assertj.core.api.Assertions.assertThat;

class FootballPlayerRequestMapperImplTest {

    private final FootballPlayerRequestMapper sut = new FootballPlayerRequestMapperImpl();

    @Test
    void correctlyMapsFootballPlayerRequestFromPlayerIdAndSource() {
        FootballPlayerRequest result = sut.from(1L, Sportmonks);

        assertThat(result)
                .isNotNull()
                .extracting(
                        FootballPlayerRequest::getId,
                        FootballPlayerRequest::getSource,
                        FootballPlayerRequest::getState
                ).containsExactly(
                        1L, Sportmonks, SCHEDULED
                );
    }

}