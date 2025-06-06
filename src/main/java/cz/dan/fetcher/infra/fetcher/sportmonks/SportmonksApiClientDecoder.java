package cz.dan.fetcher.infra.fetcher.sportmonks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cz.dan.fetcher.infra.fetcher.api.football.dto.SportmonksFootballPlayerProfileDto;
import feign.Response;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SportmonksApiClientDecoder implements Decoder {

    private final Decoder decoder = new JacksonDecoder(List.of(new JavaTimeModule()));

    private final ObjectMapper objectMapper;

    @Override
    public Object decode(Response response, Type type) {
        try {
            return decoder.decode(response, type);
        } catch (IOException e) {
            return SportmonksFootballPlayerProfileDto.builder().build();
        }
    }

}
