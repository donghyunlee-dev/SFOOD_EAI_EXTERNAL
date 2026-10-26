package com.sfood.eai.external.exception;

import com.sfood.eai.external.type.APIError;
import org.springframework.http.HttpStatus;

public class APIException extends Exception {

    protected APIError error;

    public APIException(APIError error) {
        this.error = error;
    }

    public String getMessage() {
        return this.error.getMessage();
    }

    public String getCode() {
        return this.error.getCode();
    }

    public HttpStatus getStatus() {
        return this.error.getStatus();
    }
}
