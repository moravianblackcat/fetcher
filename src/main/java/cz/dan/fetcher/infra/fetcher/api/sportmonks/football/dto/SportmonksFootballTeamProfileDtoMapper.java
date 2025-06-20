package cz.dan.fetcher.infra.fetcher.api.sportmonks.football.dto;

import cz.dan.fetcher.domain.football.request.team.entity.FootballTeamRequestOutbox;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE)
public interface SportmonksFootballTeamProfileDtoMapper {

    @Mapping(source = "data.name", target = "name")
    @Mapping(source = "data.founded", target = "founded")
    @Mapping(source = "data.country.iso3", target = "country")
    @Mapping(source = "data.venue.name", target = "stadium")
    @Mapping(source = "data.venue.cityName", target = "city")
    FootballTeamRequestOutbox from(SportmonksFootballTeamProfileDto dto);

}
