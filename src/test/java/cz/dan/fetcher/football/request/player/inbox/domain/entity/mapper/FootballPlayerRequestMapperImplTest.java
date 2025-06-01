package cz.dan.fetcher.football.request.player.inbox.domain.entity.mapper;

import cz.dan.fetcher.football.request.player.inbox.domain.entity.FootballPlayerRequest;
import cz.dan.fetcher.football.request.player.inbox.domain.source.FootballPlayerRequestSource;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FootballPlayerRequestMapperImplTest {

    private final FootballPlayerRequestMapper sut = new FootballPlayerRequestMapperImpl();

    @Test
    void correctlyMapsFootballPlayerRequestFromPlayerIdAndSource() {
        FootballPlayerRequest result = sut.from(1L, FootballPlayerRequestSource.Sportmonks);

        assertThat(result)
                .isNotNull()
                .extracting(
                        FootballPlayerRequest::getPlayerId,
                        FootballPlayerRequest::getSource,
                        FootballPlayerRequest::getState
                ).containsExactly(
                        1L, FootballPlayerRequest.Source.Sportmonks, FootballPlayerRequest.State.SCHEDULED
                );
    }

}