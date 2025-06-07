package cz.dan.fetcher.domain.football.request.player.inbox.entity.mapper;

import cz.dan.fetcher.domain.football.request.player.inbox.entity.FootballPlayerRequest;
import cz.dan.fetcher.domain.football.request.player.inbox.source.FootballPlayerRequestSource;
import cz.dan.fetcher.domain.inbox.entity.request.Source;
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
                        FootballPlayerRequest::getId,
                        FootballPlayerRequest::getSource,
                        FootballPlayerRequest::getState
                ).containsExactly(
                        1L, Source.Sportmonks, FootballPlayerRequest.State.SCHEDULED
                );
    }

}