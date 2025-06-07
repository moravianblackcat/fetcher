package cz.dan.fetcher.domain.outbox.fetcher;

import cz.dan.fetcher.domain.football.request.player.outbox.entity.FootballPlayerRequestOutbox;
import cz.dan.fetcher.domain.inbox.entity.request.Source;

public interface FootballPlayerFetcher {

    boolean supports(Source source);

    FootballPlayerRequestOutbox getFootballPlayer(long id) throws Exception;

}
