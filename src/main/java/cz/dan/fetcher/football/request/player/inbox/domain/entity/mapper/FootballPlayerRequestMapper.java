package cz.dan.fetcher.football.request.player.inbox.domain.entity.mapper;

import cz.dan.fetcher.football.request.player.inbox.domain.entity.FootballPlayerRequest;
import cz.dan.fetcher.football.request.player.inbox.domain.source.FootballPlayerRequestSource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE)
public interface FootballPlayerRequestMapper {

    @Mapping(source = "playerId", target = "id")
    FootballPlayerRequest from(long playerId, FootballPlayerRequestSource source);

}
