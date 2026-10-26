package com.sfood.eai.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "키정보")
@Setter
@Getter
public class KeyNameValue {
	
	@Schema(description = "키")
    @JsonProperty("key_name")
    public String keyName;
	
	@Schema(description = "내용")
    @JsonProperty("key_value")
    public String keyValue;

    public KeyNameValue() {}
    
    public KeyNameValue(String keyName, String keyValue) {
        this.keyName = keyName;
        this.keyValue = keyValue;
    }
}
