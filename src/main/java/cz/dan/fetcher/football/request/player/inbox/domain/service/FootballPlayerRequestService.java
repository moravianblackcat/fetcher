package cz.dan.fetcher.football.request.player.inbox.domain.service;

import cz.dan.fetcher.football.request.player.inbox.domain.source.FootballPlayerRequestSource;

import java.util.List;

public interface FootballPlayerRequestService {

    void saveRequests(List<Long> playerIds, FootballPlayerRequestSource source);

}
