package com.sfood.eai.external.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * Object Utils
 */
public final class CustomObjectUtils {

    private CustomObjectUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static <T> T deepCopy(T source, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            return mapper.readValue(mapper.writeValueAsString(source), clazz);
        } catch (IOException e) {
            throw new RuntimeException("복사 실패", e);
        }
    }

    public static <T> T readToFile(String resourcePath, Class<T> type) {
        Objects.requireNonNull(type, "type must not be null");

        ObjectMapper mapper = new ObjectMapper();

        try (InputStream stream = getResourceAsStream(resourcePath)) {
            return mapper.readValue(stream, type);
        } catch (IOException e) {
            throw new IllegalStateException("Json 파일 읽기 실패: " + resourcePath, e);
        }
    }

    public static <T> T readToFile(String resourcePath, TypeReference<T> typeReference) {
        Objects.requireNonNull(typeReference, "typeReference must not be null");

        ObjectMapper mapper = new ObjectMapper();

        try (InputStream stream = getResourceAsStream(resourcePath)) {
            return mapper.readValue(stream, typeReference);
        } catch (IOException e) {
            throw new IllegalStateException("리소스 파일 읽기 실패 : " + resourcePath, e);
        }
    }

    private static InputStream getResourceAsStream(String resourcePath) throws IOException {

        Resource resource = new ClassPathResource(resourcePath);
        InputStream inputStream = resource.getInputStream();

        if (inputStream == null) {
            throw new IllegalArgumentException("리소스 파일 검색 실패 : " + resourcePath);
        }
        return inputStream;
    }
}
