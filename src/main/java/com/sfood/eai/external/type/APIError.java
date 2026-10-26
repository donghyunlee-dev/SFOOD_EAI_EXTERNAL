package com.sfood.eai.external.type;

import org.springframework.http.HttpStatus;

public enum APIError {

    NONE("0000"),
    INVALID_REQUEST("1000"),
    CREDENTAIL_FAILED("1001"),
    INVALID_TOKEN("1002"),
    REQUEST_NOT_ALLOWED("1003"),
    NO_CONTENT("1004"),
    NO_COMPANY_INFO("1005"),
    SPRINGDOC_REQUEST("1006"),
    UNKNOWN_TENANT("1007"),
    EXPIRED_TOKEN("1008"),
    DATABASE_ERROR("2000"),
    UNKNOWN_ERROR("9999");

    private final String value;

    APIError(String value) {
        this.value = value;
    }

    public String getCode() {
        return value;
    }

    public String getMessage() {
        return switch (this) {
            case UNKNOWN_ERROR -> "알수없는 오류 발생";
            case INVALID_REQUEST -> "잘못된 요청";
            case CREDENTAIL_FAILED -> "자격 확인 불가";
            case INVALID_TOKEN -> "유효하지 않은 토큰";
            case REQUEST_NOT_ALLOWED -> "권한이 없는 요청";
            case NO_CONTENT -> "요청 데이터 없음";
            case DATABASE_ERROR -> "Database 오류";
            case NO_COMPANY_INFO -> "회사 정보가 없음";
            case SPRINGDOC_REQUEST -> "Swagger에서 실행";
            case UNKNOWN_TENANT -> "유효하지 않은 회사ID";
            case EXPIRED_TOKEN -> "만료된 토큰";
            default -> "";
        };
    }

    public HttpStatus getStatus() {
        switch (this) {
            case UNKNOWN_ERROR:
            case DATABASE_ERROR:
                return HttpStatus.INTERNAL_SERVER_ERROR;
            case INVALID_REQUEST:
            case NO_COMPANY_INFO:
            case SPRINGDOC_REQUEST:
                return HttpStatus.BAD_REQUEST;
            case EXPIRED_TOKEN:
            case CREDENTAIL_FAILED:
                return HttpStatus.UNAUTHORIZED;
            case INVALID_TOKEN:
            case REQUEST_NOT_ALLOWED:
                return HttpStatus.FORBIDDEN;
            case NO_CONTENT:
                return HttpStatus.NO_CONTENT;
            case UNKNOWN_TENANT:
                return HttpStatus.NOT_FOUND;
            default:
                return HttpStatus.OK;
        }
    }
}
