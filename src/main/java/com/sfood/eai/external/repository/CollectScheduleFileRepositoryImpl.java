package com.sfood.eai.external.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.sfood.eai.external.entity.CollectSchedule;
import com.sfood.eai.external.entity.id.CollectScheduleId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class CollectScheduleFileRepositoryImpl implements CollectScheduleFileRepository {
    @Override
    public List<CollectSchedule> findAll() {

        List<CollectSchedule> loadSchedules;

        try {
            loadSchedules = loadFromFile();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return List.of();
        }

        return loadSchedules;
    }

    @Override
    public Optional<CollectSchedule> findById(CollectScheduleId id) {
        try {
            return Optional.ofNullable(loadFromFile().stream()
                    .filter(schedule -> schedule.getId().equals(id))
                    .findFirst()
                    .orElse(null));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private List<CollectSchedule> loadFromFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        Resource resource = new ClassPathResource("static/schedules.json");
        InputStream inputStream = resource.getInputStream();

        return objectMapper.readValue(inputStream, new TypeReference<>() {});
    }
}
