package cz.dan.fetcher.domain.football.request.team.inbox.service;

import cz.dan.fetcher.domain.football.request.entity.RequestMapper;
import cz.dan.fetcher.domain.football.request.entity.RequestRepository;
import cz.dan.fetcher.domain.football.request.team.inbox.entity.FootballTeamRequest;
import cz.dan.fetcher.domain.inbox.service.request.InboxRequestService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class FootballTeamInboxRequestService extends InboxRequestService<FootballTeamRequest> {

    public FootballTeamInboxRequestService(RequestMapper<FootballTeamRequest> mapper,
                                           RequestRepository<FootballTeamRequest> repository) {
        super(mapper, repository);
    }

}
