package cz.dan.fetcher.domain.football.request.coach.inbox.entity.mapper;

import cz.dan.fetcher.domain.football.request.coach.inbox.entity.FootballCoachRequest;
import cz.dan.fetcher.domain.football.request.entity.RequestMapper;
import cz.dan.fetcher.domain.source.Source;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE)
public interface FootballCoachRequestMapper extends RequestMapper<FootballCoachRequest> {

    @Mapping(source = "coachId", target = "id")
    FootballCoachRequest from(long coachId, Source source);

}
