package cz.dan.fetcher.integration.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ClassPathResourceUtil {

    private final ObjectMapper objectMapper;

    public <T> List<T> getListFromJsonPath(String jsonPath, TypeReference<List<T>> typeRef) throws IOException {
        return objectMapper.readValue(new ClassPathResource(jsonPath).getFile(), typeRef);
    }

}
