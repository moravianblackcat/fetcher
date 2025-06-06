package cz.dan.fetcher.outbox.domain.fetcher;

import cz.dan.fetcher.football.request.player.outbox.domain.entity.FootballPlayerRequestOutbox;
import cz.dan.fetcher.outbox.domain.entity.Source;

public interface FootballPlayerFetcher {

    boolean supports(Source source);

    FootballPlayerRequestOutbox getFootballPlayer(long id) throws Exception;

}
