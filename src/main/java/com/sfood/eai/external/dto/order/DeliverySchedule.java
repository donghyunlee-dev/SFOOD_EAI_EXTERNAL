package com.sfood.eai.external.dto.order;

import com.sfood.eai.external.util.CustomDateUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.sfood.eai.external.util.CustomDateUtils.PATTERN_LOCAL_DATE_TIME;

@Slf4j
@Setter
@Getter
public class DeliverySchedule {
    /**
     * 회사코드
     */
    private String companyCode;
    /**
     * 옵션 코드
     */
    private String scheduleCode;
    /**
     * 옵션 명
     */
    private String scheduleName;
    /**
     * 정기배송 유형
     */
    private ScheduleType scheduleType;
    /**
     * 주차 (1주 ~ 8주)
     */
    private int week;
    /**
     * 배송 주기
     */
    private int[] deliveryTerm;
    /**
     * 첫 배송일
     * 식단 시작일
     */
    private LocalDate firstDeliveryDate;
    /**
     * 마지막 배송일 일자
     */
    private LocalDate lastDeliveryDate;
    /**
     * 배송일자 배열
     */
    private List<LocalDate> deliveryDays;
    /**
     * 배송 차수
     */
    private int deliverySequence;

    /**
     * 첫 배송일 설정
     * 첫 배송일 기준으로 마지막 식단일자까지 구한다.
     * 스케쥴 옵션이 먼저 설정되어야 한다.
     * @param deliveryDate
     */
    public void setFirstDeliveryDate(String deliveryDate) {

        try {
            if (PATTERN_LOCAL_DATE_TIME.matcher(deliveryDate).matches()) {
                // 주문일자 기준으로 첫 배송일자 계산 - 일반 택배
                LocalDate orderDate = CustomDateUtils.parseLocalDate(deliveryDate, "yyyy-MM-dd HH:mm:ss");
                this.firstDeliveryDate = switch (orderDate.getDayOfWeek()) {
                    case MONDAY,
                         TUESDAY,
                         WEDNESDAY,
                         THURSDAY -> orderDate.plusDays(2);
                    case FRIDAY -> orderDate.plusDays(4);
                    case SATURDAY -> orderDate.plusDays( 3);
                    case SUNDAY -> orderDate.plusDays(2);
                };
            } else {
                // 새벽 배송 - 고객이 선택한 첫 배송일자
                this.firstDeliveryDate = CustomDateUtils.parseLocalDate(deliveryDate, "yyyyMMdd");
            }
        } catch (Exception e) {
            this.firstDeliveryDate = LocalDate.now().plusDays(2);
        }

        createDeliveryDays();
    }

    public void setDeliveryTerm(String deliveryTerm) {
        this.deliveryTerm = Arrays.stream(deliveryTerm.split(",")).mapToInt(Integer::parseInt).toArray();
    }

    public DeliverySchedule() {
        this.deliverySequence = 1;
    }

    public void increaseDeliverySequence() {
        this.deliverySequence++;
    }

    private LocalDate getNextMatchingDate(LocalDate startDate, int targetDayOfWeek) {
        int startDayValue = startDate.getDayOfWeek().getValue(); // 1(Monday) ~ 7(Sunday)
        int daysToAdd = ((targetDayOfWeek + 7) - startDayValue) % 7;

        if (daysToAdd == 0) {
            daysToAdd = 7;
        }

        return startDate.plusDays(daysToAdd);
    }

    private void createDeliveryDays() {
        this.deliveryDays = new ArrayList<>();

        int firstDay = this.firstDeliveryDate.getDayOfWeek().getValue();

        boolean hasDaysBefore = Arrays.stream(this.deliveryTerm).anyMatch(day -> day < firstDay);
        List<Integer> deliveryOrdered = new ArrayList<>();

        if (hasDaysBefore) {
            Arrays.stream(this.deliveryTerm).filter(day -> day >= firstDay).forEach(deliveryOrdered::add);
            Arrays.stream(this.deliveryTerm).filter(day -> day < firstDay).forEach(deliveryOrdered::add);
        } else {
            Arrays.stream(this.deliveryTerm).forEach(deliveryOrdered::add);
        }

        for (int i = 0; i < week; i++) {
            for (int j = 0; j < deliveryOrdered.size(); j++) {
                if (i + j == 0) {
                    // 첫 번째 일자는 첫 배송일자
                    this.deliveryDays.add(this.firstDeliveryDate);
                } else {
                    // 주 1회 택배 배송인 경우 - 요일 지정을 하지 않음.
                    if (deliveryOrdered.get(j) == 0) {
                        this.deliveryDays.add(this.deliveryDays.get(this.deliveryDays.size() - 1).plusDays(7));
                    } else {
                        this.deliveryDays.add(getNextMatchingDate(this.deliveryDays.get(this.deliveryDays.size() - 1), deliveryOrdered.get(j)));
                    }
                }
            }
        }

        this.lastDeliveryDate = this.deliveryDays.getLast();
    }

    public enum ScheduleType {
        CALENDAR, TURN
    }
}
