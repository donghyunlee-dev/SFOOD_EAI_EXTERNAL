package com.sfood.eai.external.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString
@Getter
@Builder
public class ApiCallLogRes<T> {

    public String logId;
    private T data;
    private Integer status;
    private String message;

    public Long getResTime() {
        return System.currentTimeMillis();
    }

    public String getData() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this.data);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
