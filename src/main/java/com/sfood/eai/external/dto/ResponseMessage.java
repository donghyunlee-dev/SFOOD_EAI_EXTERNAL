package com.sfood.eai.external.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Schema(description = "response")
@Data
public class ResponseMessage<T> {

    @Schema(description = "응답코드", example = "0000")
    protected String code = "0000";

    @Schema(description = "응답상태", example = "OK")
    protected HttpStatus status = HttpStatus.OK;

    @Schema(description = "응답메세지", example = "")
    protected String message = "";

    @Schema(description = "응답데이터")
    protected T data = null;

    public ResponseMessage() {
    }

    public ResponseMessage(String code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }
}
