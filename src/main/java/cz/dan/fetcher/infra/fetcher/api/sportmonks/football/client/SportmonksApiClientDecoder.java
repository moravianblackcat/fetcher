package cz.dan.fetcher.infra.fetcher.api.sportmonks.football.client;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

    @Override
    public Object decode(Response response, Type type) {
        try {
            return decoder.decode(response, type);
        } catch (IOException e) {
            return null;
        }
    }

}
