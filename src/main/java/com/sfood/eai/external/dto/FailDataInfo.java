package com.sfood.eai.external.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "실패데이터정보")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FailDataInfo {
	
	@Schema(description = "키정보", nullable = true)
    @JsonProperty("key_info")
    private List<KeyNameValue> keyInfo;
	
	@Schema(description = "에러메세지", nullable = true)
    @JsonProperty("error_message")
    private String errorMessage;
}
