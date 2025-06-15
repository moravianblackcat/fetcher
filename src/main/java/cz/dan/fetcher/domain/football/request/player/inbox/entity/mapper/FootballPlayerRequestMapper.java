package cz.dan.fetcher.domain.football.request.player.inbox.entity.mapper;

import cz.dan.fetcher.domain.football.request.entity.RequestMapper;
import cz.dan.fetcher.domain.football.request.player.inbox.entity.FootballPlayerRequest;
import cz.dan.fetcher.domain.source.Source;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE)
public interface FootballPlayerRequestMapper extends RequestMapper<FootballPlayerRequest> {

    @Mapping(source = "playerId", target = "id")
    FootballPlayerRequest from(long playerId, Source source);

}
