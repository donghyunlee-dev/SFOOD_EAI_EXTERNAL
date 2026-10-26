package com.sfood.eai.external.entity.order;

import com.sfood.eai.external.util.CustomDateUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OrderDeliveryTest {

    @Test
    @DisplayName("납기예정일 날짜 비교 확인")
    void updateDeliveryDate() {
        // 배송 정보
        OrderDelivery orderDelivery = new OrderDelivery();

        LocalDateTime deliveryDate = CustomDateUtils.nowDateTime();

        // CASE 1 납기예정일이 null 일 경우 설정
        if (ObjectUtils.isEmpty(orderDelivery.getHopeDelvDate())) {
            orderDelivery.setHopeDelvDate(deliveryDate);
        }

        int result = orderDelivery.getHopeDelvDate().compareTo(deliveryDate);
        printDeliveryDate(orderDelivery.getHopeDelvDate(), deliveryDate);
        System.out.println("결과: " + result);

        // CASE 2 납기예정일이 업데이트하는 일자보다 이전일 때
        deliveryDate = CustomDateUtils.nowDateTime().plusDays(2);
        result = orderDelivery.getHopeDelvDate().compareTo(deliveryDate);
        printDeliveryDate(orderDelivery.getHopeDelvDate(), deliveryDate);
        System.out.println("결과: " + result);

        // CASE 2 납기예정일이 업데이트하는 일자보다 이후일 때
        deliveryDate = CustomDateUtils.nowDateTime().minusDays(7);
        result = orderDelivery.getHopeDelvDate().compareTo(deliveryDate);
        printDeliveryDate(orderDelivery.getHopeDelvDate(), deliveryDate);
        System.out.println("결과: " + result);
    }

    private void printDeliveryDate(LocalDateTime deliveryDate, LocalDateTime updateDate) {
        System.out.println("HOPE DEV DATE: " + deliveryDate);
        System.out.println("UPDATE DATE: " + updateDate);
    }
}