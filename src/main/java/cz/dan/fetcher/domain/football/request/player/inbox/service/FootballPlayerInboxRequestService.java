package cz.dan.fetcher.domain.football.request.player.inbox.service;

import cz.dan.fetcher.domain.football.request.entity.RequestMapper;
import cz.dan.fetcher.domain.football.request.entity.RequestRepository;
import cz.dan.fetcher.domain.football.request.player.inbox.entity.FootballPlayerRequest;
import cz.dan.fetcher.domain.inbox.service.request.InboxRequestService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class FootballPlayerInboxRequestService extends InboxRequestService<FootballPlayerRequest> {

    public FootballPlayerInboxRequestService(RequestMapper<FootballPlayerRequest> mapper,
                                             RequestRepository<FootballPlayerRequest> repository) {
        super(mapper, repository);
    }

}
