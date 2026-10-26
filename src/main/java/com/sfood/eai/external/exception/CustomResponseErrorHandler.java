package com.sfood.eai.external.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * RestTemplate용 커스텀 에러 핸들러
 * HTTP 응답 상태코드에 따른 적절한 예외 처리 및 로깅
 */
@Slf4j
public class CustomResponseErrorHandler implements ResponseErrorHandler {

    /**
     * 응답에 에러가 있는지 확인
     *
     * @param response HTTP 응답
     * @return 에러가 있으면 true, 없으면 false
     * @throws IOException I/O 예외
     */
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        HttpStatus.Series series = HttpStatus.Series.resolve(response.getStatusCode().value());
        return series == HttpStatus.Series.CLIENT_ERROR || series == HttpStatus.Series.SERVER_ERROR;
    }

    /**
     * 에러 응답 처리
     *
     * @param response HTTP 응답
     * @throws IOException I/O 예외
     */
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {

        HttpStatus statusCode = HttpStatus.resolve(response.getStatusCode().value());

        if (statusCode == null) {
            log.error("Unknown HTTP status code: {}", response.getStatusCode().value());
            throw new UnknownHttpStatusCodeException(response.getStatusCode().value(),
                    response.getStatusText(), response.getHeaders(), getResponseBodyAsBytes(response), null);
        }

        switch (statusCode.series()) {
            case CLIENT_ERROR:
                handleClientError(statusCode, response);
                break;
            case SERVER_ERROR:
                handleServerError(statusCode, response);
                break;
            default:
                log.warn("Unexpected HTTP status: {}", statusCode);
                break;
        }
    }

    /**
     * 4xx 클라이언트 에러 처리
     */
    private void handleClientError(HttpStatus statusCode, ClientHttpResponse response) throws IOException {

        String responseBody = getResponseBody(response);

        switch (statusCode) {
            case BAD_REQUEST:
                log.warn("Bad Request (400): {}", responseBody);
                break;
            case UNAUTHORIZED:
                log.warn("Unauthorized (401): Authentication failed");
                break;
            case FORBIDDEN:
                log.warn("Forbidden (403): Access denied");
                break;
            case NOT_FOUND:
                log.warn("Not Found (404): Resource not found");
                break;
            case REQUEST_TIMEOUT:
                log.warn("Request Timeout (408): {}", responseBody);
                break;
            case TOO_MANY_REQUESTS:
                log.warn("Too Many Requests (429): Rate limit exceeded");
                break;
            default:
                log.warn("Client Error ({}): {}", statusCode.value(), responseBody);
                break;
        }

        throw new HttpClientErrorException(statusCode, response.getStatusText(), response.getHeaders(), getResponseBodyAsBytes(response), null);
    }

    /**
     * 5xx 서버 에러 처리
     */
    private void handleServerError(HttpStatus statusCode, ClientHttpResponse response) throws IOException {

        String responseBody = getResponseBody(response);

        switch (statusCode) {
            case INTERNAL_SERVER_ERROR:
                log.error("Internal Server Error (500): {}", responseBody);
                break;
            case BAD_GATEWAY:
                log.error("Bad Gateway (502): {}", responseBody);
                break;
            case SERVICE_UNAVAILABLE:
                log.error("Service Unavailable (503): {}", responseBody);
                break;
            case GATEWAY_TIMEOUT:
                log.error("Gateway Timeout (504): {}", responseBody);
                break;
            default:
                log.error("Server Error ({}): {}", statusCode.value(), responseBody);
                break;
        }

        throw new HttpServerErrorException(statusCode, response.getStatusText(), response.getHeaders(), getResponseBodyAsBytes(response), null);
    }

    /**
     * 응답 바디를 문자열로 읽기
     */
    private String getResponseBody(ClientHttpResponse response) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {

            StringBuilder body = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                body.append(line).append("\n");
            }

            String responseBody = body.toString().trim();

            if (responseBody.length() > 1000) {
                responseBody = responseBody.substring(0, 1000) + "... (truncated)";
            }

            return responseBody;

        } catch (IOException e) {
            log.warn("Failed to read response body: {}", e.getMessage());
            return "Unable to read response body";
        }
    }

    /**
     * 응답 바디를 바이트 배열로 읽기 (예외 생성용)
     */
    private byte[] getResponseBodyAsBytes(ClientHttpResponse response) {
        try {
            return response.getBody().readAllBytes();
        } catch (IOException e) {
            log.warn("Failed to read response body as bytes: {}", e.getMessage());
            return new byte[0];
        }
    }
}
