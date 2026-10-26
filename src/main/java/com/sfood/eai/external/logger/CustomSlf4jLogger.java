package com.sfood.eai.external.logger;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.Slf4JLogger;
import org.springframework.util.StringUtils;

public class CustomSlf4jLogger extends Slf4JLogger {

    @Override
    public void logText(String text) {

        if (!StringUtils.hasText(text)) {
            return;
        }

        super.logText(text);
    }

    @Override
    public void logSQL(int connectionId, String now, long elapsed, Category category, String prepared, String sql, String url) {

        if (!StringUtils.hasText(sql)) {
            return;
        }
        // 반복되는 쿼리 제거, return 시 아래 logSQL 을 무시하기 때문에 로그 출력되지 않음.
        if (sql.contains("QRTZ_") || sql.contains("BATCH_") || sql.contains("COLLECT_SCHEDULE") || sql.toUpperCase().contains("SELECT 1 FROM DUAL")) {
            return;
        }

        super.logSQL(connectionId, now, elapsed, category, prepared, sql, url);
    }
}
