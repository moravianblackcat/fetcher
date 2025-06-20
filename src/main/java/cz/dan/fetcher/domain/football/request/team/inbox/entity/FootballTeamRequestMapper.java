package cz.dan.fetcher.domain.football.request.team.inbox.entity;

import cz.dan.fetcher.domain.football.request.entity.RequestMapper;
import cz.dan.fetcher.domain.source.Source;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE)
public interface FootballTeamRequestMapper extends RequestMapper<FootballTeamRequest> {

    @Mapping(source = "teamId", target = "id")
    FootballTeamRequest from(long teamId, Source source);

}
