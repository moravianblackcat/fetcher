package cz.dan.fetcher.domain.team.service;

import cz.dan.fetcher.domain.team.entity.Team;
import cz.dan.fetcher.domain.team.entity.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TeamServiceImpl implements TeamService {

    private final TeamRepository repository;

    @Override
    public Long save(Team team) {
        return repository.save(team).getId();
    }

}
