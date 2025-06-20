package cz.dan.fetcher.domain.football.request.coach.inbox.service;

import cz.dan.fetcher.domain.football.request.coach.inbox.entity.FootballCoachRequest;
import cz.dan.fetcher.domain.football.request.entity.RequestMapper;
import cz.dan.fetcher.domain.football.request.entity.RequestRepository;
import cz.dan.fetcher.domain.inbox.service.request.InboxRequestService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class FootballCoachInboxRequestService extends InboxRequestService<FootballCoachRequest> {

    public FootballCoachInboxRequestService(RequestMapper<FootballCoachRequest> mapper,
                                            RequestRepository<FootballCoachRequest> repository) {
        super(mapper, repository);
    }

}
