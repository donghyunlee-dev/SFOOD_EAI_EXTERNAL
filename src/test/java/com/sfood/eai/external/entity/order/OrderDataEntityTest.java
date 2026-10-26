package com.sfood.eai.external.entity.order;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;

class OrderDataEntityTest {

    @Test
    @DisplayName("수정 가능한 상태 코드 확인")
    public void checkOrderStatus() {
        boolean isFalse;
        boolean isTrue;
        List<String> case1 = Arrays.asList("OS1010", "OS1030", "OS2010", "OS2020");
        List<String> case2 = Arrays.asList("OS1030", "OS1030", "OS2010");

        OptionalInt falseCase = case1.stream().mapToInt(goods -> Integer.parseInt(goods.substring(2))).max();
        OptionalInt trueCase = case2.stream().mapToInt(goods -> Integer.parseInt(goods.substring(2))).max();

        System.out.println("False Case Max Status : " + falseCase.getAsInt());
        if (falseCase.isPresent() && falseCase.getAsInt() > 2010 && falseCase.getAsInt() < 8010) {
            isFalse = false;
        } else {
            isFalse = true;
        }

        System.out.println("True Case Max Status : " + trueCase.getAsInt());
        if (trueCase.isPresent() && trueCase.getAsInt() > 2010 && trueCase.getAsInt() < 8010) {
            isTrue = false;
        } else {
            isTrue = true;
        }

        System.out.println("isFalse : " + isFalse);
        System.out.println("isTrue : " + isTrue);
        Assertions.assertEquals(isFalse, false);
        Assertions.assertEquals(isTrue, true);
    }
}