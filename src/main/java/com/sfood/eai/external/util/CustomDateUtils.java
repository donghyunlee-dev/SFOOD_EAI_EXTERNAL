package com.sfood.eai.external.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * 날짜 커스텀 유틸
 */
public final class CustomDateUtils {

    public static final Pattern PATTERN_LOCAL_DATE_TIME = Pattern.compile("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$");

    private CustomDateUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String now() {
        DateTimeFormatter datetimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.now(ZoneId.of("Asia/Seoul")).format(datetimeFormatter);
    }

    public static String now(String format) {
        DateTimeFormatter datetimeFormatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.now(ZoneId.of("Asia/Seoul")).format(datetimeFormatter);
    }

    public static LocalDateTime nowDateTime() {
        return LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    public static LocalDateTime parseLocalDateTime(String date) {
        LocalDate localDate = LocalDate.parse(date.replace("-", ""), DateTimeFormatter.ofPattern("yyyyMMdd"));
        return localDate.atStartOfDay();
    }

    public static LocalDateTime parseLocalDateTime(String date, String format) {
        DateTimeFormatter datetimeFormatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(date, datetimeFormatter);
    }

    public static String formatDateTimeString(LocalDateTime date, String format) {
        DateTimeFormatter datetimeFormatter = DateTimeFormatter.ofPattern(format);
        return date.format(datetimeFormatter);
    }

    public static String formatDateTimeString(String date, String inputFormat, String outputFormat) {
        // 1차 날짜로 변환
        LocalDateTime parseLocalDateTime = CustomDateUtils.parseLocalDateTime(date, inputFormat);
        // 2차 문자열 파싱
        return CustomDateUtils.formatDateTimeString(parseLocalDateTime, outputFormat);
    }

    public static LocalDate parseLocalDate(LocalDateTime datetime) {
        return datetime.toLocalDate();
    }

    public static LocalDate parseLocalDate(String date) {
        return LocalDate.parse(date.replace("-", ""), DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public static LocalDate parseLocalDate(String date, String format) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(format));
    }

    public static String formatDateString(LocalDate date) {
        DateTimeFormatter datetimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(datetimeFormatter);
    }

    public static String formatDateString(LocalDate date, String format) {
        DateTimeFormatter datetimeFormatter = DateTimeFormatter.ofPattern(format);
        return date.format(datetimeFormatter);
    }

    public static String addDays(int days) {
        DateTimeFormatter datetimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.now(ZoneId.of("Asia/Seoul")).plusDays(days).format(datetimeFormatter);
    }

    public static String addDays(LocalDateTime date, int days) {
        return date.plusDays(days).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
