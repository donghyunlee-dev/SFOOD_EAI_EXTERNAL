package com.sfood.eai.external.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class CustomDateUtilsTest {

    @Test
    @DisplayName("DateTime 형식 데이터를 yyyyMMdd 형식으로 변환 테스트")
    public void parseDateToStringTest() {

        String originDate = "2025-02-02 00:00:00";

        // 1차 날짜로 변환
        LocalDateTime parseLocalDateTime = CustomDateUtils.parseLocalDateTime(originDate, "yyyy-MM-dd HH:mm:ss");
        System.out.println(parseLocalDateTime);

        // 2차 문자열 파싱
        String result = CustomDateUtils.formatDateTimeString(parseLocalDateTime, "yyyyMMdd");
        System.out.println(result);
    }

    @Test
    @DisplayName("LocalDate 문자열 변환 테스트")
    public void localDateUtilsTest() {
        String originDate = "2025-02-02";

        LocalDate deliveryDate = CustomDateUtils.parseLocalDate(originDate, "yyyy-MM-dd");
        System.out.println(deliveryDate);

        String StringDate = CustomDateUtils.formatDateString(LocalDate.now(), "yyyy-MM-dd");
        System.out.println(StringDate);
    }

    @Test
    @DisplayName("LocalDateTime 문자열 변환 테스트")
    public void localDateTimeTest() {

        String orderDate = "2025-03-07 11:19:13";

        LocalDateTime localDateTime = CustomDateUtils.parseLocalDateTime(orderDate, "yyyy-MM-dd HH:mm:ss");

        System.out.println(localDateTime);
        Assertions.assertEquals("2025-03-07", localDateTime.toLocalDate().toString());
    }
    @Test
    @DisplayName("마감 시간 변환 테스트")
    public void calcCloseDateTest() {
        LocalDateTime now = LocalDateTime.now();

        LocalDate localDate = now.toLocalDate();
        LocalTime localTime = LocalTime.of(17, 0);
        LocalDateTime closeDate = now.toLocalDate().atStartOfDay().plusHours(localTime.getHour()).plusMinutes(localTime.getMinute());
        System.out.println(closeDate);
        Assertions.assertEquals( "%sT17:00".formatted(localDate.toString()), closeDate.toString());
    }

    @Test
    @DisplayName("LocalDate 타입으로 날짜 계산해서 String으로 리턴 테스트")
    public void getAddDaysTest() {

        String result = CustomDateUtils.addDays(-1);
        System.out.println(MessageFormat.format("result:{0}", result));
    }

    @Test
    @DisplayName("Date 타입 생성 확인")
    public void localDateToDateTest() {
       Date now = Date.from(LocalDate.now(ZoneId.of("Asia/Seoul")).atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant());
        System.out.println(MessageFormat.format("now : {0}", now));

        Date utcDate = Date.from(LocalDate.now(ZoneId.of("Asia/Seoul")).atStartOfDay(ZoneId.of("Asia/Seoul")).withZoneSameInstant(ZoneId.of("UTC")).toInstant());
        System.out.println(MessageFormat.format("utc : {0}", utcDate));
    }

    @Test
    @DisplayName("LocalDate 비교")
    public void testLocalDate() {
        String deliveryDate = "2025-10-10";

        int result = CustomDateUtils.parseLocalDate("2025-10-10").compareTo(CustomDateUtils.parseLocalDate(deliveryDate));
        System.out.println(result);
        if (CustomDateUtils.parseLocalDate("2025-10-11").compareTo(CustomDateUtils.parseLocalDate(deliveryDate)) > 0) {
            deliveryDate = "2025-10-11";
        }
        System.out.println(deliveryDate);
    }
}
