package com.sfood.eai.external.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oracle.bmc.util.internal.StringUtils;
import com.sfood.eai.external.entity.Holiday;
import com.sfood.eai.external.util.CustomObjectUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.*;

class OrderServiceImplTest {

    @Test
    void calcAddDeliveryDays() {

        DateTimeFormatter datetimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String paymentDate = "2024-10-25 16:59:59";
        String closeTime = "1730";

        LocalDateTime paymentDateTime = LocalDateTime.parse(paymentDate, datetimeFormatter);

        String closeDateTimeString = MessageFormat.format("{0} {1}:00", paymentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), formatCloseTime(closeTime));

        LocalDateTime closeDateTime = LocalDateTime.parse(closeDateTimeString, datetimeFormatter);
        out.println(MessageFormat.format("payment time : {0}", formatCloseTime(closeTime)));

        Duration du = Duration.between(paymentDateTime, closeDateTime);
        out.println(du.getSeconds());
        out.println("요일 : " + paymentDateTime.getDayOfWeek());

        String hopeDelvDate = switch (paymentDateTime.getDayOfWeek()) {
            case SATURDAY -> paymentDateTime.plusDays(3).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            case SUNDAY -> paymentDateTime.plusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            default ->  calcDeliveryDateByCloseTime(paymentDateTime, closeDateTime);
        };

        out.println("배송희망일자: " + hopeDelvDate);
    }

    private String calcDeliveryDateByCloseTime(LocalDateTime paymentDateTime, LocalDateTime closeDateTime) {
        Duration diffTime = Duration.between(paymentDateTime, closeDateTime);
        if (diffTime.getSeconds() < 0) {
            return paymentDateTime.plusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } else {
            return paymentDateTime.plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
    }

    private String formatCloseTime(String closeTime) {

        if (StringUtils.isEmpty(closeTime)) {
            return "17:00";
        }

        String hours = closeTime.substring(0, 2);
        String minutes = closeTime.substring(2, 4);

        return hours + ":" + minutes;
    }

    @Test
    @DisplayName("배송일자 휴일 처리 테스트")
    void calcDeliveryDate() {
        List<Holiday> holidays = CustomObjectUtils.readToFile("static/holidays.json", new TypeReference<List<Holiday>>() {});

        holidays.forEach(holiday -> out.println(holiday.toString()));

    }
}