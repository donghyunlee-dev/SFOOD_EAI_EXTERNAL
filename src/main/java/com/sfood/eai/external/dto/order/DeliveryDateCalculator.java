package com.sfood.eai.external.dto.order;

import com.sfood.eai.external.entity.Holiday;
import com.sfood.eai.external.util.CustomDateUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DeliveryDateCalculator {

    private final Set<LocalDate> holidays;

    public DeliveryDateCalculator(List<Holiday> holidays) {
        this.holidays = holidays.stream().map(Holiday::getDate).collect(Collectors.toUnmodifiableSet());
    }

    public LocalDate calculate(LocalDateTime orderDateTime, LocalDateTime closeTime) {

        boolean isAfterCloseTime = Duration.between(orderDateTime, closeTime).getSeconds() < 0;
        // 고도몰 & 사방넷
        String deliveryDate = switch (orderDateTime.getDayOfWeek()) {
            case MONDAY,
                 TUESDAY,
                 WEDNESDAY,
                 THURSDAY ->
                    (isAfterCloseTime) ? CustomDateUtils.addDays(orderDateTime, 2) : CustomDateUtils.addDays(orderDateTime, 1);
            case FRIDAY ->
                    (isAfterCloseTime) ? CustomDateUtils.addDays(orderDateTime, 4) : CustomDateUtils.addDays(orderDateTime, 1);
            case SATURDAY -> CustomDateUtils.addDays(orderDateTime, 3);
            case SUNDAY -> CustomDateUtils.addDays(orderDateTime, 2);
        };

        return CustomDateUtils.parseLocalDate(deliveryDate);
    }
}
