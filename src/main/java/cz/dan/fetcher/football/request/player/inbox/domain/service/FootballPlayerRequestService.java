package cz.dan.fetcher.football.request.player.inbox.domain.service;

import cz.dan.fetcher.football.request.player.inbox.domain.entity.FootballPlayerRequest;
import cz.dan.fetcher.football.request.player.inbox.domain.source.FootballPlayerRequestSource;
import cz.dan.fetcher.inbox.domain.RequestService;

import java.util.List;

public interface FootballPlayerRequestService extends RequestService<FootballPlayerRequest> {

    void saveRequests(List<Long> playerIds, FootballPlayerRequestSource source);

}
