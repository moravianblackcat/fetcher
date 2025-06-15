package cz.dan.fetcher.infra.sender.football.player.payload;

import cz.dan.avro.fetcher.outbox.FootballPlayerOutboxPayload;
import cz.dan.fetcher.domain.football.request.player.outbox.entity.FootballPlayerRequestOutbox;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE)
public interface FootballPlayerOutboxPayloadMapper {

    FootballPlayerOutboxPayload from(FootballPlayerRequestOutbox requestOutbox);

}
