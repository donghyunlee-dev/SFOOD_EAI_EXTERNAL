package com.sfood.eai.external.logger;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.springframework.util.StringUtils;

public class CustomSqlFormatter implements MessageFormattingStrategy {
    private static final long SLOW_QUERY_THRESHOLD_MS = 1000;

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        if (!StringUtils.hasText(sql) ) {
            return "";
        }

        if (elapsed > SLOW_QUERY_THRESHOLD_MS) {
            return String.format("Slow Query (%d ms): %s", elapsed, format(sql));
        }

        return String.format("Timestamp: %s, Execution Time: %d ms, Category: %s%n %s", now, elapsed, category, format(sql));
    }

    private String format(String sql) {
        if (isDDL(sql)) {
            return FormatStyle.DDL.getFormatter().format(sql);
        }

        if (isDML(sql)) {
            return FormatStyle.BASIC.getFormatter().format(sql);
        }

        return sql;
    }

    private boolean isDDL(String sql) {
        String upperCaseSql = sql.toUpperCase();
        return upperCaseSql.startsWith("CREATE")
                || upperCaseSql.startsWith("ALTER")
                || upperCaseSql.startsWith("DROP")
                || upperCaseSql.startsWith("ADD")
                || upperCaseSql.startsWith("COMMENT");
    }

    private boolean isDML(String sql) {
        String upperCaseSql = sql.toUpperCase();
        return upperCaseSql.startsWith("SELECT")
                || upperCaseSql.startsWith("INSERT")
                || upperCaseSql.startsWith("UPDATE")
                || upperCaseSql.startsWith("DELETE");
    }
}