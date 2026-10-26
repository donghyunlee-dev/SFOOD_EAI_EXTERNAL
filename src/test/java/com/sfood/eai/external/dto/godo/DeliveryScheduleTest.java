package com.sfood.eai.external.dto.godo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryScheduleTest {

    @Test
    @DisplayName("배송일자 재정렬 처리 테스트")
    void setDeliveryTerm() {
        int today = 2;// 월=1, ..., 일=7
        int[] days = {1, 2, 3, 4, 5};
        // 중복 제거 및 정렬
        List<Integer> sorted = new ArrayList<>();
        for (int i = 0; i < days.length; i++) {
            if (!sorted.contains(days[i])) {
                sorted.add(days[i]);
            }
        }
        sorted.sort(Integer::compareTo);

        // 오늘보다 작은 값이 하나라도 있으면 재정렬
        boolean hasSmaller = sorted.stream().anyMatch(d -> d < today);
        if (hasSmaller) {
            List<Integer> reordered = new ArrayList<>();

            // today 이상 먼저 추가
            sorted.stream()
                    .filter(d -> d >= today)
                    .forEach(reordered::add);

            // today 미만 나중에 추가
            sorted.stream()
                    .filter(d -> d < today)
                    .forEach(reordered::add);

            for (int day : reordered.stream().mapToInt(Integer::intValue).toArray()) {
                System.out.print(day + " ");
            }
            return;
        }

        // 정렬만 한 배열 반환
        for (int day : sorted.stream().mapToInt(Integer::intValue).toArray()) {
            System.out.print(day + " ");
        }

    }
}