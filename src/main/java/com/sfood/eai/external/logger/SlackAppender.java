package com.sfood.eai.external.logger;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class SlackAppender extends AppenderBase<ILoggingEvent> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Seoul"));
    private final ThrowableProxyConverter throwableConverter = new ThrowableProxyConverter();

    private String webhookUrl;

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    @Override
    public void start() {
        throwableConverter.start();
        super.start();
    }

    @Override
    protected void append(ILoggingEvent event) {
        if (!event.getLevel().isGreaterOrEqual(Level.ERROR)) {
            return;
        }

        String message = formatSlackMessage(event);
        sendToSlack(message);
    }

    private String formatSlackMessage(ILoggingEvent event) {
        String message = "";

        if (event.getThrowableProxy() != null) {
            message += "\n" + event.getThrowableProxy().getMessage() + "\n";
            // 스택트레이스 추가 (선택적)
            // message += throwableConverter.convert(event);
        } else {
            message = "\n" + event.getMessage() + "\n";
        }

        if (message.length() > 2500) {
            message = message.substring(0, 2500) + "\n...(truncated)";
        }

        return """
                {
                  "blocks": [
                    {
                      "type": "header",
                      "text": {
                        "type": "plain_text",
                        "text": "🚨 [%s] 오류 발생"
                      }
                    },
                    {
                      "type": "section",
                      "text": {
                        "type": "mrkdwn",
                        "text": "*Logger :* `%s`\\n*Error :* `%s`\\n*Time :* `%s`"
                      }
                    },
                    {
                      "type": "section",
                      "text": {
                        "type": "mrkdwn",
                        "text": "*Message :*\\n```%s```"
                      }
                    }
                  ]
                }
                """.formatted(
                event.getLevel(),
                event.getLoggerName(),
                escapeMessage(event.getFormattedMessage()),
                formatter.format(Instant.ofEpochMilli(event.getTimeStamp())),
                escapeMessage(message)
        );

        // 슬랙 유효성 테스트 검사 : https://app.slack.com/block-kit-builder
    }

    private void sendToSlack(String message) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(webhookUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(message, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != HttpStatus.OK.value()) {
                addError("Slack 전송 실패: " + response.statusCode() + ", 응답: " + response.body());
            }
        } catch (Exception e) {
            addError("Slack 전송 실패: " + e.getMessage(), e);
        }
    }

    private String escapeMessage(String message) {
        if (!StringUtils.hasText(message)) {
            return "";
        }

        return message
                .replace("\\", "\\\\")  // 역슬래시 먼저 처리
                .replace("\"", "\\\"")  // 따옴표 이스케이프
                .replace("\n", "\\n")   // 줄바꿈 이스케이프
                .replace("\r", "\\r")   // 캐리지 리턴 이스케이프
                .replace("\t", "\\t")   // 탭 이스케이프
                .replace("`", "\\`")    // 백틱 이스케이프
                .replace("\b", "\\b")   // 백스페이스 이스케이프
                .replace("\f", "\\f");  // 폼피드 이스케이프
    }
}
