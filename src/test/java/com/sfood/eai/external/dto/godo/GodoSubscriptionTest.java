package com.sfood.eai.external.dto.godo;

import com.sfood.eai.external.entity.subscription.SubscriptionDetailCalendar;
import com.sfood.eai.external.util.CustomDateUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

class GodoSubscriptionTest {


    @Test
    @DisplayName("냉장 배송일자 구하기")
    public void calcDeliveryDays() {
        List<LocalDate> deliveryDays = new ArrayList<>();
        LocalDate firstDeliveryDate = LocalDate.of(2025, 4, 7);
        String DeliveryTermString = "1,4";
        int week = 2;
        int[] deliveryTerm = Arrays.stream(DeliveryTermString.split(",")).mapToInt(Integer::parseInt).toArray();
        deliveryDays.clear();

        for (int i = 0; i < week; i++) {
            for (int j = 0; j < deliveryTerm.length; j++) {

                if (i + j == 0) {
                    deliveryDays.add(firstDeliveryDate);
                } else {
                    deliveryDays.add(getNextMatchingDate(deliveryDays.get(deliveryDays.size() - 1), deliveryTerm[j]));
                }
            }
        }

        System.out.println("배송 일정:");
        deliveryDays.forEach(System.out::println);
    }

    @Test
    @DisplayName("냉장 식단 일자 구하기")
    public void calcFoodsDays() {
        List<LocalDate> foodDays = new ArrayList<>();
        int week = 2;
        int foodDay = 7;
        LocalDate lastFoodDate;
        LocalDate firstDeliveryDate = LocalDate.of(2025, 3, 3);

        if (firstDeliveryDate.getDayOfWeek().equals(DayOfWeek.MONDAY) && foodDay == 5) {
            lastFoodDate = firstDeliveryDate.plusWeeks(week).minusDays(3);
        } else {
            lastFoodDate = firstDeliveryDate.plusWeeks(week).minusDays(1);
        }

        LocalDate currentDate = firstDeliveryDate;
        while (!currentDate.isAfter(lastFoodDate)) {
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            if (foodDay == 5 && (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY)) {
                currentDate = currentDate.plusDays(1);
                continue;

            }
            foodDays.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }

        foodDays.forEach(System.out::println);
    }

    @Test
    @DisplayName("정기배송 - 배송일자별 구성품 집계 확인")
    public void mappingScheduleCalendar() {
        LocalDate startDate = LocalDate.of(2025, 3, 7);
        LocalDate endDate = LocalDate.of(2025, 3, 13);
        List<SubscriptionDetailCalendar> subCalendarList = new ArrayList<>();
        SubscriptionDetailCalendar calendar1 = new SubscriptionDetailCalendar();
        calendar1.setCompanyCode("C00000001");
        calendar1.setScheduleCode("SC202502040002");
        calendar1.setScheduleDate("2025-03-09");
        calendar1.setGoodsCode("GO20241226AAA0961");
        calendar1.setSortNo("2");
        subCalendarList.add(calendar1);

        SubscriptionDetailCalendar calendar2 = new SubscriptionDetailCalendar();
        calendar2.setCompanyCode("C00000001");
        calendar2.setScheduleCode("SC202502040002");
        calendar2.setScheduleDate("2025-03-07");
        calendar2.setGoodsCode("GO20241226AAA0855");
        calendar2.setSortNo("1");
        subCalendarList.add(calendar2);

        SubscriptionDetailCalendar calendar3 = new SubscriptionDetailCalendar();
        calendar3.setCompanyCode("C00000001");
        calendar3.setScheduleCode("SC202502040002");
        calendar3.setScheduleDate("2025-03-13");
        calendar3.setGoodsCode("GO20241226AAA0961");
        calendar3.setSortNo("1");
        subCalendarList.add(calendar3);

        Map<String, List<SubscriptionDetailCalendar>> subCalenderMap = subCalendarList.stream()
                .filter(cal -> CustomDateUtils.parseLocalDate(cal.getScheduleDate()).isAfter(startDate.minusDays(1))
                        && CustomDateUtils.parseLocalDate(cal.getScheduleDate()).isBefore(endDate.plusDays(1)))
                .sorted(Comparator.comparing(SubscriptionDetailCalendar::getScheduleDate).thenComparing(SubscriptionDetailCalendar::getSortNo))
                .collect(Collectors.groupingBy(
                        SubscriptionDetailCalendar::getScheduleDate,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        subCalenderMap.forEach((date, schedules) -> {
            System.out.println(date + ": " + schedules);
        });
    }

    public LocalDate getNextMatchingDate(LocalDate startDate, int targetDayOfWeek) {
        int startDayValue = startDate.getDayOfWeek().getValue(); // 1(Monday) ~ 7(Sunday)
        int daysToAdd = (targetDayOfWeek - startDayValue + 7) % 7; // 다음 해당 요일까지의 차이 계산
        return startDate.plusDays(daysToAdd);
    }

}