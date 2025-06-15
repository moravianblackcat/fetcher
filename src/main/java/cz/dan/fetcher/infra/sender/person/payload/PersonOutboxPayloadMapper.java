package cz.dan.fetcher.infra.sender.person.payload;

import cz.dan.avro.fetcher.outbox.PersonOutboxPayload;
import cz.dan.fetcher.domain.person.entity.PersonOutbox;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE)
public interface PersonOutboxPayloadMapper {

    PersonOutboxPayload from(PersonOutbox outbox);

}
