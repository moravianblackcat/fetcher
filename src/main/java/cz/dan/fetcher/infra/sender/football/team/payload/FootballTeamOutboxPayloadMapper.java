package cz.dan.fetcher.infra.sender.football.team.payload;

import cz.dan.avro.fetcher.outbox.FootballTeamOutboxPayload;
import cz.dan.fetcher.domain.football.request.team.entity.FootballTeamRequestOutbox;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE)
public interface FootballTeamOutboxPayloadMapper {

    FootballTeamOutboxPayload from(FootballTeamRequestOutbox outbox);

}
