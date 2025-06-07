package cz.dan.fetcher.domain.football.request.player.inbox.service;

import cz.dan.fetcher.domain.football.request.player.inbox.entity.FootballPlayerRequest;
import cz.dan.fetcher.domain.football.request.player.inbox.source.FootballPlayerRequestSource;
import cz.dan.fetcher.domain.inbox.service.request.RequestService;

import java.util.List;

public interface FootballPlayerRequestService extends RequestService<FootballPlayerRequest> {

    void saveRequests(List<Long> playerIds, FootballPlayerRequestSource source);

}
