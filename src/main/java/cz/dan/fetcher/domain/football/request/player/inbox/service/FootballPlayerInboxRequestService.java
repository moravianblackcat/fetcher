package cz.dan.fetcher.domain.football.request.player.inbox.service;

import cz.dan.fetcher.domain.football.request.player.inbox.entity.FootballPlayerRequest;
import cz.dan.fetcher.domain.football.request.player.inbox.entity.FootballPlayerRequestRepository;
import cz.dan.fetcher.domain.football.request.player.inbox.entity.mapper.FootballPlayerRequestMapper;
import cz.dan.fetcher.domain.inbox.service.request.InboxRequestService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class FootballPlayerInboxRequestService extends InboxRequestService<FootballPlayerRequest> {

    public FootballPlayerInboxRequestService(FootballPlayerRequestMapper mapper,
                                             FootballPlayerRequestRepository repository) {
        super(mapper, repository);
    }

}
