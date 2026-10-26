package com.sfood.eai.external.service.converter.impl;

import com.sfood.eai.external.dto.order.DeliverySchedule;
import com.sfood.eai.external.entity.godo.*;
import com.sfood.eai.external.entity.goods.Goods;
import com.sfood.eai.external.entity.goods.GoodsOption;
import com.sfood.eai.external.entity.goods.GoodsPrice;
import com.sfood.eai.external.entity.id.goods.GoodsId;
import com.sfood.eai.external.entity.id.goods.GoodsOptionId;
import com.sfood.eai.external.entity.order.*;
import com.sfood.eai.external.entity.subscription.SubscriptionDetailCalendar;
import com.sfood.eai.external.entity.subscription.SubscriptionDetailTurn;
import com.sfood.eai.external.mapper.order.*;
import com.sfood.eai.external.service.OrderGoodsService;
import com.sfood.eai.external.service.SubscriptionService;
import com.sfood.eai.external.service.converter.BaseOrderDataConverter;
import com.sfood.eai.external.service.converter.OrderDataConverter;
import com.sfood.eai.external.type.ChannelType;
import com.sfood.eai.external.util.CustomBigDecimalUtils;
import com.sfood.eai.external.util.CustomDateUtils;
import com.sfood.eai.external.util.CustomObjectUtils;
import com.sfood.eai.external.util.CustomStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.sfood.eai.external.dto.order.DeliverySchedule.ScheduleType.CALENDAR;
import static com.sfood.eai.external.dto.order.DeliverySchedule.ScheduleType.TURN;
import static com.sfood.eai.external.entity.order.OrderDataEntity.OrderType.NORMAL;
import static com.sfood.eai.external.entity.order.OrderDataEntity.OrderType.SUBSCRIPTION;
import static com.sfood.eai.external.type.Constants.SCHEDULE_FOOD_SET_COUNT;

@Slf4j
@Component
public class GodoOrderDataConverter extends BaseOrderDataConverter implements OrderDataConverter {

    // 정기배송 서비스
    private final SubscriptionService subscriptionService;

    public GodoOrderDataConverter(OrderGoodsService orderGoodsService, SubscriptionService subscriptionService) {
        super(orderGoodsService);
        this.subscriptionService = subscriptionService;
    }

    @Override
    public ChannelType getChannelType() {
        return ChannelType.GODOMALL;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T convertOrderEntity(Object rawData) {

        GodoDataEntity godoData = (GodoDataEntity) rawData;

        if (ObjectUtils.isEmpty(godoData.getOption())) {
            return (T) convertGeneralOrder(godoData);
        } else {
            return (T) convertSubscriptionOrder(godoData);
        }
    }

    private OrderDataEntity convertGeneralOrder(GodoDataEntity godoData) {

        OrderMaster orderMaster = getOrderMasterEntity(godoData);

        List<OrderGoods> orderGoodsList = new ArrayList<>();
        List<OrderDelivery> orderDeliveryList = new ArrayList<>();
        List<OrderClaim> orderClaimList = new ArrayList<>();

        for (GodoOrderGoods godoOrderGoods : godoData.getGoods()) {
            orderMaster.increaseDetailMaxSequence();
            OrderGoods orderGoods = getOrderGoodsEntity(orderMaster, godoOrderGoods);
            orderGoodsList.add(orderGoods);

            GodoOrderInfo godoOrderInfo = findGodoOrderInfo(godoData, godoOrderGoods.getOrderDeliverySno());
            orderDeliveryList.add(getOrderDeliveryEntity(orderMaster, godoOrderGoods, godoOrderInfo));

            godoData.getClaim().stream()
                    .filter(claim -> claim.getSno().equals(godoOrderGoods.getSno()))
                    .findFirst()
                    .ifPresent(claim -> orderClaimList.add(getOrderClaimEntity(orderGoods, claim)));
        }

        for (GodoGift gift : godoData.getGift()) {
            orderMaster.increaseDetailMaxSequence();
            orderGoodsList.add(getOrderGoodsEntity(orderMaster, gift));
            orderDeliveryList.add(getOrderDeliveryEntity(orderMaster, orderDeliveryList.getFirst()));
        }

        OrderPayment orderPayment = getOrderPaymentEntity(orderMaster, godoData);

        return OrderDataEntity.builder()
                .orderMaster(orderMaster)
                .orderGoods(orderGoodsList)
                .orderDelivery(orderDeliveryList)
                .orderPayment(orderPayment)
                .orderClaim(orderClaimList)
                .orderType(NORMAL)
                .build();
    }

    private List<OrderDataEntity> convertSubscriptionOrder(GodoDataEntity godoData) {
        // 주문 데이터 분할 생성해서 리스트로 저장
        List<OrderDataEntity> orderDataList = new ArrayList<>();
        // 일반 택배용 주문 정보
        OrderMaster generalOrderMaster = null;
        List<OrderGoods> generalOrderGoodsList = new ArrayList<>();
        List<OrderDelivery> generalOrderDeliveryList = new ArrayList<>();
        List<OrderClaim> generalOrderClaimList = new ArrayList<>();

        // 품목마다 기준을 잡는다.
        // 일반 품목이면 주문을 신규로 만든다.
        // 대표 품목이면 스케쥴을 확인하고 고도몰 1개 품목당 주문 1개씩 만든다.
        for (GodoOrderGoods godoOrderGoods : godoData.getGoods()) {
            // 옵션 중 goods의 옵션을 찾아야 한다.
            // 연동 품목을 찾아야 하기 때문에 옵션 정보 중 자체옵션코드가 있는 것만 필터한다.
            // 설정 또는 오류가 발생할 수 있기 때문에 리스트로 받는다.
            GodoOrderGoodsOption godoGoodsOption = godoData.getOption().stream()
                    .filter(option -> option.getSno().equals(godoOrderGoods.getSno())
                            && ObjectUtils.isNotEmpty(option.getOptionCode()))      // 여러 옵션 중 1개만 자체품목코드가 저장됨
                    .findFirst().orElse(null);

            // 품목 배송지 정보 조회
            GodoOrderInfo godoOrderInfo = findGodoOrderInfo(godoData, godoOrderGoods.getOrderDeliverySno());
            GodoOrderDelivery godoOrderDelivery  = findGodoOrderDelivery(godoData, godoOrderGoods.getOrderDeliverySno());

            Goods subScriptionGoods = findSubscriptionGoods(godoData.getCompanyCode(), godoOrderGoods, godoGoodsOption);

            // 해당없음. 상세품목인 경우 주문 분리 생성, 상세품목은 Validation 에서 오류로 필터
            if ("NS".equals(subScriptionGoods.getSubscriptionType()) || "PD".equals(subScriptionGoods.getSubscriptionType())) {

                if (ObjectUtils.isEmpty(generalOrderMaster)) {
                    setOrderTitle(godoData, null, godoOrderGoods.getGoodsNm());
                    generalOrderMaster = getOrderMasterEntity(godoData);
                }

                generalOrderMaster.increaseDetailMaxSequence();
                OrderGoods orderGoods = getOrderGoodsEntity(generalOrderMaster, godoOrderGoods, subScriptionGoods);

                // 일반택배에 상세품목이면 예외 처리
                if ("PD".equals(subScriptionGoods.getSubscriptionType())) {
                    orderGoods.markAsExceptionError();
                }

                generalOrderGoodsList.add(orderGoods);
                generalOrderDeliveryList.add(getOrderDeliveryEntity(generalOrderMaster, godoOrderGoods, godoOrderInfo));

                godoData.getClaim().stream()
                        .filter(claim -> claim.getSno().equals(godoOrderGoods.getSno()))
                        .findFirst()
                        .ifPresent(claim -> generalOrderClaimList.add(getOrderClaimEntity(orderGoods, claim)));
                continue;
            }

            // PP 대표 품목일 경우 아래 프로세스
            // 배송 일정 설정
            DeliverySchedule deliverySchedule = subscriptionService.getNewDeliverySchedule(subScriptionGoods.getCompanyCode(), subScriptionGoods.getGoodsCode());

            if (ObjectUtils.isEmpty(deliverySchedule)) {
                // 스케쥴이 없을 경우 해당없음 일반 품목 등록 후 오류 주문으로 제외 시킴
                if (ObjectUtils.isEmpty(generalOrderMaster)) {
                    setOrderTitle(godoData, null, godoOrderGoods.getGoodsNm());
                    generalOrderMaster = getOrderMasterEntity(godoData);
                }

                generalOrderMaster.increaseDetailMaxSequence();
                OrderGoods orderGoods = getOrderGoodsEntity(generalOrderMaster, godoOrderGoods, subScriptionGoods);
                orderGoods.markAsExceptionError();
                generalOrderGoodsList.add(orderGoods);
                generalOrderDeliveryList.add(getOrderDeliveryEntity(generalOrderMaster, godoOrderGoods, godoOrderInfo));

                godoData.getClaim().stream()
                        .filter(claim -> claim.getSno().equals(godoOrderGoods.getSno()))
                        .findFirst()
                        .ifPresent(claim -> generalOrderClaimList.add(getOrderClaimEntity(orderGoods, claim)));
                continue;
            }

            try {
                // 새벽 배송일 경우 첫 배송일 설정 - 고도몰에서 InvoiceNo에 새벽배송(20250301) 형태로 전달함.
                deliverySchedule.setFirstDeliveryDate(String.valueOf(CustomStringUtils.extractNumbers(godoOrderGoods.getInvoiceNo())[0]));
            } catch (Exception e) {
                // 주문일자로 첫 배송일 계산
                deliverySchedule.setFirstDeliveryDate(godoData.getOrder().getOrderDate());
            }

            // 품목 기준으로 주문을 생성하기 때문에 품목명으로 주문 타이틀을 변경한다.
            String orderTitle = godoOrderGoods.getGoodsNm();

            // 달력형 (새벽 배송)
            if (CALENDAR.equals(deliverySchedule.getScheduleType())) {
                // 달력형 배송일정과 구성 품목 조회
                // 시작 배송일자부터 종료 배송일자까지 구성된 품목을 조회한다.
                Map<LocalDate, List<SubscriptionDetailCalendar>> subCalendarList = subscriptionService.findDetailCalendars(deliverySchedule)
                        .stream()
                        .collect(Collectors.groupingBy(cal -> CustomDateUtils.parseLocalDate(cal.getScheduleDate())));

                if (ObjectUtils.isEmpty(subCalendarList)) {
                    if (ObjectUtils.isEmpty(generalOrderMaster)) {
                        setOrderTitle(godoData, null, godoOrderGoods.getGoodsNm());
                        generalOrderMaster = getOrderMasterEntity(godoData);
                    }

                    generalOrderMaster.increaseDetailMaxSequence();
                    OrderGoods orderGoods = getOrderGoodsEntity(generalOrderMaster, godoOrderGoods, subScriptionGoods);
                    orderGoods.markAsExceptionError();
                    generalOrderGoodsList.add(orderGoods);
                    generalOrderDeliveryList.add(getOrderDeliveryEntity(generalOrderMaster, godoOrderGoods, godoOrderInfo));

                    godoData.getClaim().stream()
                            .filter(claim -> claim.getSno().equals(godoOrderGoods.getSno()))
                            .findFirst()
                            .ifPresent(claim -> generalOrderClaimList.add(getOrderClaimEntity(orderGoods, claim)));
                    continue;
                }

                // 배송일 만큼 반복해서 주문을 만든다.
                for (LocalDate deliveryDay : deliverySchedule.getDeliveryDays()) {
                    // 주문 생성 시작 - 주문 품목 타이틀에 주차 표시
                    setOrderTitle(godoData, deliverySchedule, orderTitle);
                    OrderMaster orderMaster = getOrderMasterEntity(godoData);

                    List<OrderGoods> orderGoodsList = new ArrayList<>();
                    List<OrderDelivery> orderDeliveryList = new ArrayList<>();
                    List<OrderClaim> orderClaimList = new ArrayList<>();

                    // 첫 배송일인 경우에 추가
                    if (deliveryDay.equals(deliverySchedule.getFirstDeliveryDate())) {
                        // 냉장 추가 품목 확인
                        List<GodoAddGoods> addGoodsList = godoData.getAddGoods().stream()
                                .filter(addGoods -> godoOrderGoods.getGoodsNo().equals(addGoods.getParentGoodsNo()))
                                .toList();

                        if (CollectionUtils.isNotEmpty(addGoodsList)) {
                            for (GodoAddGoods addGoods : addGoodsList) {
                                orderMaster.increaseDetailMaxSequence();
                                GoodsPrice goodsPrice = getGoodsPrice(new GoodsId(godoData.getCompanyCode(), addGoods.getGoodsCd()));
                                godoOrderGoods.setOptionPrice(godoOrderGoods.getOptionPrice().add(addGoods.getGoodsPrice().multiply(BigDecimal.valueOf(addGoods.getGoodsCnt()))));
                                OrderGoods orderGoods = getOrderGoodsEntity(orderMaster, godoOrderGoods, addGoods, goodsPrice, addGoods.getGoodsCnt(), new DeliverySchedule());
                                orderGoodsList.add(orderGoods);
                                godoOrderGoods.setDeliveryDt(CustomDateUtils.formatDateString(deliveryDay));
                                orderDeliveryList.add(getOrderDeliveryEntity(orderMaster, godoOrderGoods, godoOrderInfo));
                            }
                        }
                    }

                    if (CollectionUtils.isNotEmpty(subCalendarList.get(deliveryDay))) {
                        for (SubscriptionDetailCalendar foodGoods : subCalendarList.get(deliveryDay)) {
                            orderMaster.increaseDetailMaxSequence();
                            OrderGoods orderGoods = getOrderGoodsEntity(orderMaster, godoOrderGoods, getGoodsPrice(new GoodsId(foodGoods.getCompanyCode(), foodGoods.getGoodsCode())), deliverySchedule);
                            orderGoodsList.add(orderGoods);
                            godoOrderGoods.setDeliveryDt(CustomDateUtils.formatDateString(deliveryDay));
                            orderDeliveryList.add(getOrderDeliveryEntity(orderMaster, godoOrderGoods, godoOrderInfo));

                            godoData.getClaim().stream()
                                    .filter(claim -> claim.getSno().equals(godoOrderGoods.getSno()))
                                    .findFirst()
                                    .ifPresent(claim -> orderClaimList.add(getOrderClaimEntity(orderGoods, claim)));
                        }
                    } else {
                        // 배송 일정에 등록된 상세 품목이 없을 경우
                        orderMaster.increaseDetailMaxSequence();
                        OrderGoods orderGoods = getOrderGoodsEntity(orderMaster, godoOrderGoods, getGoodsPrice(new GoodsId(godoData.getCompanyCode(), subScriptionGoods.getGoodsCode())), deliverySchedule);
                        orderGoods.markAsExceptionError();
                        orderGoodsList.add(orderGoods);
                        godoOrderGoods.setDeliveryDt(CustomDateUtils.formatDateString(deliveryDay));
                        orderDeliveryList.add(getOrderDeliveryEntity(orderMaster, godoOrderGoods, godoOrderInfo));

                        godoData.getClaim().stream()
                                .filter(claim -> claim.getSno().equals(godoOrderGoods.getSno()))
                                .findFirst()
                                .ifPresent(claim -> orderClaimList.add(getOrderClaimEntity(orderGoods, claim)));
                    }

                    OrderPayment orderPayment = getOrderPaymentEntity(orderMaster, godoData.getOrder(), godoOrderGoods, godoOrderDelivery);

                    orderDataList.add(
                            OrderDataEntity.builder()
                                    .orderMaster(orderMaster)
                                    .orderGoods(orderGoodsList)
                                    .orderDelivery(orderDeliveryList)
                                    .orderPayment(orderPayment)
                                    .orderClaim(orderClaimList)
                                    .orderType(SUBSCRIPTION)
                                    .build()
                    );
                    // 주문 타이틀에 차수 표시하기 위해 증가
                    deliverySchedule.increaseDeliverySequence();
                }
                // 품목 금액으로 설정된 결제 금액을 주문 수로 분배한다.
                adjustSubscriptionOrderPayment(godoOrderGoods, orderDataList);
                // 배송일자별로 주문전표를 생성했으면 고도몰 주문 품목 금액으로 조정을 한다.
                adjustSubscriptionOrderGoodsPrice(godoOrderGoods, orderDataList);

            } else if (TURN.equals(deliverySchedule.getScheduleType())) {
                // 구성 품목이 있을 경우 정기배송 스케쥴을 보지 않음.
                if (CollectionUtils.isNotEmpty(godoData.getAddGoods())) {
                    // 품목의 구성 폼목을 필터해서 조회
                    List<GodoAddGoods> targetAddGoodsList = godoData.getAddGoods().stream()
                            .filter(addGoods -> godoOrderGoods.getGoodsNo().equals(addGoods.getParentGoodsNo()))
                            .toList();

                    if (CollectionUtils.isEmpty(targetAddGoodsList)) {
                        continue;
                    }
                    // 배송일자를 반복한다.
                    for (LocalDate deliveryDay : deliverySchedule.getDeliveryDays()) {
                        // 주차별 품목과 배송 정보를 생성
                        // 1주차 이상일 경우 주차마다 품목과 배송정보를 생성한다.
                        setOrderTitle(godoData, deliverySchedule, orderTitle);
                        OrderMaster orderMaster = getOrderMasterEntity(godoData);

                        List<OrderGoods> orderGoodsList = new ArrayList<>();
                        List<OrderDelivery> orderDeliveryList = new ArrayList<>();
                        List<OrderClaim> orderClaimList = new ArrayList<>();
                        // 주차에 설정해야 하는 구성품 수
                        // 차감하면서 1주에 포함되어야 할 수량을 맞추는 용도로 사용한다.
                        int foodSetCount = SCHEDULE_FOOD_SET_COUNT * godoOrderGoods.getGoodsCnt().intValue();
                        // 1주에 주문 수량 * 10식씩 분배해서 구성 품목 생성
                        // 1주차 구성품 수량이 남아 있으면 반복해서 구성품 리스트와 수량을 계산한다.
                        while (foodSetCount > 0) {

                            targetAddGoodsList = targetAddGoodsList.stream()
                                    .filter(addGoods -> addGoods.getAssignGoodsCnt() > 0)
                                    .sorted(Comparator.comparing(GodoAddGoods::getGoodsPrice).reversed() // 금액 높은 순
                                            .thenComparing(Comparator.comparing(GodoAddGoods::getAssignGoodsCnt).reversed()) // 수량 많은 순
                                    ).toList();

                            // foodSetCount 소진이 되지 않은 채로 구성 품목의 수량이 소진된 경우 예외 처리
                            if (CollectionUtils.isEmpty(targetAddGoodsList)) {
                                foodSetCount = 0;
                            }

                            // 품목 기준으로 추출한 구성품 마다 수량을 계산한다.
                            for (GodoAddGoods targetAddGoods : targetAddGoodsList) {
                                // 이미 1주차의 구성품 수량을 차감했으면 나머지 구성품은 처리하지 않는다.
                                if (foodSetCount <= 0) {
                                    continue;
                                }
                                // 대상 구성품을 주문 구성품으로 변경하기 전에 먼저 할당 수량을 차감한다.
                                targetAddGoods.decreaseAssignQty();

                                // 이미 같은 주차의 구성품에 추가되어 있는지 찾는다.
                                Optional<OrderGoods> findOrderGoods = orderGoodsList.stream()
                                        .filter(orderGoods -> orderGoods.getMarketGoodsCode().equals(targetAddGoods.getAddGoodsNo()))
                                        .findFirst();
                                // 추가되어 있으면 수량만 증가하면 된다.
                                findOrderGoods.ifPresent(OrderGoods::increaseOrderQty);

                                if (!findOrderGoods.isPresent()) {

                                    GoodsPrice goodsPrice = getGoodsPrice(new GoodsId(godoData.getCompanyCode(), targetAddGoods.getGoodsCd()));

                                    int orderQty = 1;

                                    // 해당없음. 일반 품목일 경우 구성품 식단 수량에 영향이 없어야 한다.
                                    if (ObjectUtils.isNotEmpty(goodsPrice) && "NS".equals(goodsPrice.getSubscriptionType())) {
                                        orderQty = targetAddGoods.getGoodsCnt();
                                        targetAddGoods.setAssignGoodsCnt(0);

                                        godoOrderGoods.setOptionPrice(godoOrderGoods.getOptionPrice().add(targetAddGoods.getGoodsPrice().multiply(BigDecimal.valueOf(targetAddGoods.getGoodsCnt()))));
                                        foodSetCount++;
                                    }

                                    orderMaster.increaseDetailMaxSequence();
                                    OrderGoods orderGoods = getOrderGoodsEntity(orderMaster, godoOrderGoods, targetAddGoods, goodsPrice, orderQty, deliverySchedule);
                                    orderGoodsList.add(orderGoods);
                                    godoOrderGoods.setDeliveryDt(CustomDateUtils.formatDateString(deliveryDay));
                                    orderDeliveryList.add(getOrderDeliveryEntity(orderMaster, godoOrderGoods, godoOrderInfo));

                                    godoData.getClaim().stream()
                                            .filter(claim -> claim.getSno().equals(godoOrderGoods.getSno()))
                                            .findFirst()
                                            .ifPresent(claim -> orderClaimList.add(getOrderClaimEntity(orderGoods, claim)));
                                }

                                // 1주 구성품 수량을 감소한다.
                                foodSetCount--;
                            }
                        }

                        OrderPayment orderPayment = getOrderPaymentEntity(orderMaster, godoData.getOrder(), godoOrderGoods, godoOrderDelivery);

                        orderDataList.add(OrderDataEntity.builder()
                                .orderMaster(orderMaster)
                                .orderGoods(orderGoodsList)
                                .orderDelivery(orderDeliveryList)
                                .orderPayment(orderPayment)
                                .orderClaim(orderClaimList)
                                .orderType(SUBSCRIPTION)
                                .build());

                        deliverySchedule.increaseDeliverySequence();
                    }
                    adjustSubscriptionOrderPayment(godoOrderGoods, orderDataList);
                    adjustSubscriptionOrderGoodsPrice(godoOrderGoods, orderDataList);
                } else {
                    // 차수별 품목 구성 조회 - 냉동
                    // 고도몰에서 구성품 없이 내려올 경우
                    Map<Integer, List<SubscriptionDetailTurn>> subscriptionDetailTurnList = subscriptionService.findDetailTurns(deliverySchedule)
                            .stream()
                            .collect(Collectors.groupingBy(turn -> Integer.parseInt(turn.getTurnNo())));

                    if (ObjectUtils.isEmpty(subscriptionDetailTurnList)) {
                        // 스케쥴이 없을 경우 해당없음 일반 품목 등록 후 오류 주문으로 제외 시킴
                        if (ObjectUtils.isEmpty(generalOrderMaster)) {
                            setOrderTitle(godoData, null, godoOrderGoods.getGoodsNm());
                            generalOrderMaster = getOrderMasterEntity(godoData);
                        }

                        generalOrderMaster.increaseDetailMaxSequence();
                        OrderGoods orderGoods = getOrderGoodsEntity(generalOrderMaster, godoOrderGoods, subScriptionGoods);
                        orderGoods.markAsExceptionError();
                        generalOrderGoodsList.add(orderGoods);
                        generalOrderDeliveryList.add(getOrderDeliveryEntity(generalOrderMaster, godoOrderGoods, godoOrderInfo));

                        godoData.getClaim().stream()
                                .filter(claim -> claim.getSno().equals(godoOrderGoods.getSno()))
                                .findFirst()
                                .ifPresent(claim -> generalOrderClaimList.add(getOrderClaimEntity(orderGoods, claim)));
                        continue;
                    }
                    // 배송 차수
                    int deliverySeq = 0;

                    for (LocalDate deliveryDay : deliverySchedule.getDeliveryDays()) {
                        deliverySeq++;

                        setOrderTitle(godoData, deliverySchedule, orderTitle);
                        OrderMaster orderMaster = getOrderMasterEntity(godoData);

                        List<OrderGoods> orderGoodsList = new ArrayList<>();
                        List<OrderDelivery> orderDeliveryList = new ArrayList<>();
                        List<OrderClaim> orderClaimList = new ArrayList<>();

                        if (CollectionUtils.isNotEmpty(subscriptionDetailTurnList.get(deliverySeq))) {
                            subscriptionDetailTurnList.get(deliverySeq).forEach(foodGoods -> {
                                orderMaster.increaseDetailMaxSequence();
                                OrderGoods orderGoods = getOrderGoodsEntity(orderMaster, godoOrderGoods, getGoodsPrice(new GoodsId(foodGoods.getCompanyCode(), foodGoods.getGoodsCode())), deliverySchedule);
                                orderGoodsList.add(orderGoods);
                                godoOrderGoods.setDeliveryDt(CustomDateUtils.formatDateString(deliveryDay));
                                orderDeliveryList.add(getOrderDeliveryEntity(orderMaster, godoOrderGoods, godoOrderInfo));

                                godoData.getClaim().stream()
                                        .filter(claim -> claim.getSno().equals(godoOrderGoods.getSno()))
                                        .findFirst()
                                        .ifPresent(claim -> orderClaimList.add(getOrderClaimEntity(orderGoods, claim)));
                            });
                        } else {
                            orderMaster.increaseDetailMaxSequence();
                            OrderGoods orderGoods = getOrderGoodsEntity(orderMaster, godoOrderGoods, getGoodsPrice(new GoodsId(godoData.getCompanyCode(), subScriptionGoods.getGoodsCode())), deliverySchedule);
                            orderGoods.markAsExceptionError();
                            orderGoodsList.add(orderGoods);
                            godoOrderGoods.setDeliveryDt(CustomDateUtils.formatDateString(deliveryDay));
                            orderDeliveryList.add(getOrderDeliveryEntity(orderMaster, godoOrderGoods, godoOrderInfo));

                            godoData.getClaim().stream()
                                    .filter(claim -> claim.getSno().equals(godoOrderGoods.getSno()))
                                    .findFirst()
                                    .ifPresent(claim -> orderClaimList.add(getOrderClaimEntity(orderGoods, claim)));
                        }

                        OrderPayment orderPayment = getOrderPaymentEntity(orderMaster, godoData.getOrder(), godoOrderGoods, godoOrderDelivery);

                        orderDataList.add(OrderDataEntity.builder()
                                .orderMaster(orderMaster)
                                .orderGoods(orderGoodsList)
                                .orderDelivery(orderDeliveryList)
                                .orderPayment(orderPayment)
                                .orderClaim(orderClaimList)
                                .orderType(SUBSCRIPTION)
                                .build());
                    }
                    adjustSubscriptionOrderPayment(godoOrderGoods, orderDataList);
                    adjustSubscriptionOrderGoodsPrice(godoOrderGoods, orderDataList);
                }
            }
        }

        if (CollectionUtils.isNotEmpty(generalOrderGoodsList)) {

            if (generalOrderGoodsList.size() > 1) {
                generalOrderMaster.setOrderMergeTitle(generalOrderMaster.getOrderMergeTitle() + " 외 " + (generalOrderGoodsList.size() - 1) + "건");
            }

            OrderDataEntity generalOrder = OrderDataEntity.builder()
                    .orderMaster(generalOrderMaster)
                    .orderGoods(generalOrderGoodsList)
                    .orderDelivery(generalOrderDeliveryList)
                    .orderPayment(getOrderPaymentEntity(generalOrderMaster, godoData))
                    .orderClaim(generalOrderClaimList)
                    .orderType(NORMAL)
                    .build();
            adjustGeneralOrderPayment(generalOrder);
            orderDataList.add(generalOrder);
        }

        if (ObjectUtils.isNotEmpty(godoData.getGift())) {
            OrderDataEntity firstOrder = orderDataList.stream()
                    .filter(order -> order.getOrderDelivery() != null && !order.getOrderDelivery().isEmpty())
                    .min(Comparator.comparing(order -> order.getOrderDelivery().stream()
                            .map(OrderDelivery::getHopeDelvDate)
                            .filter(Objects::nonNull)
                            .min(LocalDateTime::compareTo)
                            .orElse(LocalDateTime.MAX))) // hopeDelvDate가 없을 경우 뒤로 밀림
                    .orElse(null);

            for (GodoGift gift : godoData.getGift()) {
                firstOrder.getOrderMaster().increaseDetailMaxSequence();
                firstOrder.getOrderGoods().add(getOrderGoodsEntity(firstOrder.getOrderMaster(), gift));
                firstOrder.getOrderDelivery().add(getOrderDeliveryEntity(firstOrder.getOrderMaster(), firstOrder.getOrderDelivery().getFirst()));
            }
        }

        adjustDeliveryCost(godoData.getDelivery(), orderDataList);
        return orderDataList;
    }

    private OrderMaster getOrderMasterEntity(GodoDataEntity godoData) {
        return OrderMasterMapper.INSTANCE.toEntity(godoData, godoData.getInfo().getFirst());
    }

    private OrderGoods getOrderGoodsEntity(OrderMaster orderMaster, GodoOrderGoods godoOrderGoods) {
        return OrderGoodsMapper.INSTANCE.toEntity(orderMaster, godoOrderGoods);
    }

    private OrderGoods getOrderGoodsEntity(OrderMaster orderMaster, GodoOrderGoods godoOrderGoods, Goods subGoods) {
        return OrderGoodsMapper.INSTANCE.toEntity(orderMaster, godoOrderGoods, subGoods);
    }

    private OrderGoods getOrderGoodsEntity(OrderMaster orderMaster, GodoGift gift) {
        return OrderGoodsMapper.INSTANCE.toEntity(orderMaster, gift);
    }

    private OrderGoods getOrderGoodsEntity(OrderMaster orderMaster, GodoOrderGoods godoOrderGoods, GoodsPrice goodsPrice, DeliverySchedule deliverySchedule) {
        return OrderGoodsMapper.INSTANCE.toEntity(orderMaster, godoOrderGoods, goodsPrice, deliverySchedule);
    }

    private OrderGoods getOrderGoodsEntity(OrderMaster orderMaster, GodoOrderGoods godoOrderGoods, GodoAddGoods addGoods, GoodsPrice goodsPrice, int goodsQty, DeliverySchedule deliverySchedule) {
        return OrderGoodsMapper.INSTANCE.toEntity(orderMaster, godoOrderGoods, addGoods, goodsPrice, goodsQty, deliverySchedule);
    }

    private OrderDelivery getOrderDeliveryEntity(OrderMaster orderMaster, GodoOrderGoods godoOrderGoods, GodoOrderInfo godoOrderInfo) {
        return OrderDeliveryMapper.INSTANCE.toEntity(orderMaster, godoOrderGoods, godoOrderInfo);
    }

    private OrderDelivery getOrderDeliveryEntity(OrderMaster orderMaster, OrderDelivery orderDelivery) {
        return OrderDeliveryMapper.INSTANCE.toEntity(orderMaster, orderDelivery);
    }

    private OrderPayment getOrderPaymentEntity(OrderMaster orderMaster, GodoDataEntity godoData) {
        return OrderPaymentMapper.INSTANCE.toEntity(orderMaster, godoData.getOrder(), godoData.getGoods().getFirst());
    }

    private OrderPayment getOrderPaymentEntity(OrderMaster orderMaster, GodoOrder godoOrder, GodoOrderGoods godoGoods, GodoOrderDelivery godoOrderDelivery) {
        return OrderPaymentMapper.INSTANCE.toEntityByGoods(orderMaster, godoOrder, godoGoods, godoOrderDelivery);
    }

    private OrderClaim getOrderClaimEntity(OrderGoods orderGoods, GodoOrderClaim godoOrderClaim) {
        return OrderClaimMapper.INSTANCE.toEntity(orderGoods, godoOrderClaim);
    }

    private GodoOrderInfo findGodoOrderInfo(GodoDataEntity godoData, int deliverySNo) {
        GodoOrderInfo godoOrderInfo;
        // 멀티 배송 여부
        if (godoData.isMultipleShipping()) {
            // Delivery 에서 Info 번호 검색
            int infoSno = godoData.getDelivery()
                    .stream()
                    .filter(delivery -> delivery.getSno().equals(deliverySNo))
                    .map(GodoOrderDelivery::getOrderInfoSno)
                    .findFirst()
                    .orElse(1);

            godoOrderInfo = godoData.getInfo()
                    .stream()
                    .filter(info -> Objects.nonNull(info.getSno()))
                    .filter(info -> info.getSno().equals(infoSno))
                    .findFirst()
                    .orElseGet(() -> godoData.getInfo().getFirst());
        } else {
            godoOrderInfo = godoData.getInfo().getFirst();
        }
        return godoOrderInfo;
    }

    private GodoOrderDelivery findGodoOrderDelivery(GodoDataEntity godoData, Integer orderDeliverySno) {
        return godoData.getDelivery().stream().filter(dellivery -> orderDeliverySno.equals(dellivery.getSno())).findFirst().orElse(null);
    }

    private Goods findSubscriptionGoods(String companyCode, GodoOrderGoods godoOrderGoods, GodoOrderGoodsOption godoGoodsOption) {

        Goods findGoods;
        if (ObjectUtils.isEmpty(godoGoodsOption)) {
            //옵션에 설정이 안되어 있거나 일반 품목인 경우에는 고도몰에 등록된 Goods의 자체품목코드로 품목을 조회한다.
            findGoods = getGoods(new GoodsId(companyCode, godoOrderGoods.getGoodsCd()));
        } else {
            GoodsOption goodsOption = getGoodsOption(new GoodsOptionId(
                    companyCode
                    , godoOrderGoods.getGoodsCd()
                    , Integer.parseInt(godoGoodsOption.getOptionCode())));

            if (ObjectUtils.isNotEmpty(goodsOption) && StringUtils.hasText(goodsOption.getRelationGoodsCode())) {
                // 옵션이 설정 되어 있으면 리스트 중 첫번째로 품목을 조회한다. (리스트에 1개만 조회되는 게 정상이다.)
                // 정기배송의 경우 옵션의 대표품목, 일반품목의 코드로 Goods를 구성한다.
                findGoods = getGoods(new GoodsId(companyCode, goodsOption.getRelationGoodsCode()));
            } else {
                findGoods = getGoods(new GoodsId(companyCode, godoOrderGoods.getGoodsCd()));
            }
        }

        if (ObjectUtils.isEmpty(findGoods)) {
            return new Goods(new GoodsId(companyCode, godoOrderGoods.getGoodsCd()));
        }

        return findGoods;
    }

    /**
     * 분할된 주문 결제 금액 대비 차액 발생 분을 마지막 주문 결제금에 반영한다.
     */
    private void adjustGeneralOrderPayment(OrderDataEntity orderDataEntity) {

        OrderPayment orderPayment = orderDataEntity.getOrderPayment();

        orderPayment.setTotalDcCost(orderDataEntity.getOrderGoods().stream().map(OrderGoods::getDcCost).reduce(BigDecimal.ZERO, BigDecimal::add));
        orderPayment.setTotalOrderCost(orderDataEntity.getOrderGoods().stream().map(OrderGoods::getSalesOrderPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
        orderPayment.setTotalOrderCost(orderPayment.getTotalOrderCost().add(orderPayment.getTotalDcCost()));
        orderPayment.setTotalGoodsDcCost(orderDataEntity.getOrderGoods().stream().map(OrderGoods::getGoodsDcCost).reduce(BigDecimal.ZERO, BigDecimal::add));
        orderPayment.setTotalMemberDcCost(orderDataEntity.getOrderGoods().stream().map(OrderGoods::getMemberDcCost).reduce(BigDecimal.ZERO, BigDecimal::add));
        orderPayment.setTotalCouponGoodsDcCost(orderDataEntity.getOrderGoods().stream().map(OrderGoods::getCouponGoodsDcCost).reduce(BigDecimal.ZERO, BigDecimal::add));
        orderPayment.setTotalCouponOrderDcCost(orderDataEntity.getOrderGoods().stream().map(OrderGoods::getCouponOrderDcCost).reduce(BigDecimal.ZERO, BigDecimal::add));
        orderPayment.setTotalUseMileage(orderDataEntity.getOrderGoods().stream().map(OrderGoods::getUseMileage).reduce(BigDecimal.ZERO, BigDecimal::add));
        orderPayment.setTotalUseDeposit(orderDataEntity.getOrderGoods().stream().map(OrderGoods::getUseDeposit).reduce(BigDecimal.ZERO, BigDecimal::add));
        orderPayment.setTotalEnuriDcPrice(orderDataEntity.getOrderGoods().stream().map(OrderGoods::getEnuriDcPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
        orderPayment.setTotalPayCost(orderDataEntity.getOrderGoods().stream().map(OrderGoods::getSalesOrderPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
        orderPayment.setTotalWonCost(orderPayment.getTotalPayCost().divide(BigDecimal.valueOf(1.1), 0, RoundingMode.DOWN));
        orderPayment.setTotalTaxVatCost(CustomBigDecimalUtils.subtract(orderPayment.getTotalPayCost(), orderPayment.getTotalWonCost()));
    }

    /**
     * 분할된 주문 결제 금액 대비 차액 발생 분을 마지막 주문 결제금에 반영한다.
     */
    private void adjustSubscriptionOrderPayment(GodoOrderGoods orderGoods, List<OrderDataEntity> orderDataList) {

        List<OrderDataEntity> filteredOrderList = orderDataList.stream()
                .filter(order -> Optional.ofNullable(order.getOrderGoods())
                        .orElse(Collections.emptyList())
                        .stream()
                        .anyMatch(goods -> String.valueOf(orderGoods.getSno()).equals(goods.getMarketOrderSubNo())))
                .toList();

        if (CollectionUtils.isEmpty(filteredOrderList)) {
            return;
        }

        BigDecimal orderCount = BigDecimal.valueOf(filteredOrderList.size());
        OrderPayment originPayment = CustomObjectUtils.deepCopy(filteredOrderList.getFirst().getOrderPayment(), OrderPayment.class);

        for (OrderDataEntity orderDataEntity : filteredOrderList) {

            OrderPayment orderPayment = orderDataEntity.getOrderPayment();

            orderPayment.setTotalDcCost(orderPayment.getTotalDcCost().divide(orderCount, 0, RoundingMode.DOWN));
            orderPayment.setTotalOrderCost(orderPayment.getTotalOrderCost().divide(orderCount, 0, RoundingMode.DOWN));
            orderPayment.setTotalGoodsDcCost(orderPayment.getTotalGoodsDcCost().divide(orderCount, 0, RoundingMode.DOWN));
            orderPayment.setTotalMemberDcCost(orderPayment.getTotalMemberDcCost().divide(orderCount, 0, RoundingMode.DOWN));
            orderPayment.setTotalMemberDeliveryDcCost(orderPayment.getTotalMemberDeliveryDcCost().divide(orderCount, 0, RoundingMode.DOWN));
            orderPayment.setTotalCouponGoodsDcCost(orderPayment.getTotalCouponGoodsDcCost().divide(orderCount, 0, RoundingMode.DOWN));
            orderPayment.setTotalCouponOrderDcCost(orderPayment.getTotalCouponOrderDcCost().divide(orderCount, 0, RoundingMode.DOWN));
            orderPayment.setTotalCouponDeliveryDcCost(orderPayment.getTotalCouponDeliveryDcCost().divide(orderCount, 0, RoundingMode.DOWN));
            orderPayment.setTotalUseMileage(orderPayment.getTotalUseMileage().divide(orderCount, 0, RoundingMode.DOWN));
            orderPayment.setTotalUseDeposit(orderPayment.getTotalUseDeposit().divide(orderCount, 0, RoundingMode.DOWN));
            orderPayment.setTotalEnuriDcPrice(orderPayment.getTotalEnuriDcPrice().divide(orderCount, 0, RoundingMode.DOWN));
            orderPayment.setTotalPayCost(CustomBigDecimalUtils.subtract(orderPayment.getTotalOrderCost(), orderPayment.getTotalDcCost()));
            orderPayment.setTotalWonCost(orderPayment.getTotalPayCost().divide(BigDecimal.valueOf(1.1), 0, RoundingMode.DOWN));
            orderPayment.setTotalTaxVatCost(CustomBigDecimalUtils.subtract(orderPayment.getTotalPayCost(), orderPayment.getTotalWonCost()));

        }

        BigDecimal totalDcCost = filteredOrderList.stream().map(OrderDataEntity::getOrderPayment).map(OrderPayment::getTotalDcCost).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalOrderCost = filteredOrderList.stream().map(OrderDataEntity::getOrderPayment).map(OrderPayment::getTotalOrderCost).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalGoodsDcCost = filteredOrderList.stream().map(OrderDataEntity::getOrderPayment).map(OrderPayment::getTotalGoodsDcCost).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalMemberDcCost = filteredOrderList.stream().map(OrderDataEntity::getOrderPayment).map(OrderPayment::getTotalMemberDcCost).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalMemberDeliveryDcCost = filteredOrderList.stream().map(OrderDataEntity::getOrderPayment).map(OrderPayment::getTotalMemberDeliveryDcCost).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCouponGoodsDcCost = filteredOrderList.stream().map(OrderDataEntity::getOrderPayment).map(OrderPayment::getTotalCouponGoodsDcCost).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCouponOrderDcCost = filteredOrderList.stream().map(OrderDataEntity::getOrderPayment).map(OrderPayment::getTotalCouponOrderDcCost).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCouponDeliveryDcCost = filteredOrderList.stream().map(OrderDataEntity::getOrderPayment).map(OrderPayment::getTotalCouponDeliveryDcCost).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalUseMileage = filteredOrderList.stream().map(OrderDataEntity::getOrderPayment).map(OrderPayment::getTotalUseMileage).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalUseDeposit = filteredOrderList.stream().map(OrderDataEntity::getOrderPayment).map(OrderPayment::getTotalUseDeposit).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalEnuriDcPrice = filteredOrderList.stream().map(OrderDataEntity::getOrderPayment).map(OrderPayment::getTotalEnuriDcPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalPayCost = filteredOrderList.stream().map(OrderDataEntity::getOrderPayment).map(OrderPayment::getTotalPayCost).reduce(BigDecimal.ZERO, BigDecimal::add);

        filteredOrderList.getLast().getOrderPayment().setTotalDcCost(filteredOrderList.getLast().getOrderPayment().getTotalDcCost().add(CustomBigDecimalUtils.subtract(originPayment.getTotalDcCost(), totalDcCost)));
        filteredOrderList.getLast().getOrderPayment().setTotalOrderCost(filteredOrderList.getLast().getOrderPayment().getTotalOrderCost().add(CustomBigDecimalUtils.subtract(originPayment.getTotalOrderCost(), totalOrderCost)));
        filteredOrderList.getLast().getOrderPayment().setTotalGoodsDcCost(filteredOrderList.getLast().getOrderPayment().getTotalGoodsDcCost().add(CustomBigDecimalUtils.subtract(originPayment.getTotalGoodsDcCost(), totalGoodsDcCost)));
        filteredOrderList.getLast().getOrderPayment().setTotalMemberDcCost(filteredOrderList.getLast().getOrderPayment().getTotalMemberDcCost().add(CustomBigDecimalUtils.subtract(originPayment.getTotalMemberDcCost(), totalMemberDcCost)));
        filteredOrderList.getLast().getOrderPayment().setTotalMemberDeliveryDcCost(filteredOrderList.getLast().getOrderPayment().getTotalMemberDeliveryDcCost().add(CustomBigDecimalUtils.subtract(originPayment.getTotalMemberDeliveryDcCost(), totalMemberDeliveryDcCost)));
        filteredOrderList.getLast().getOrderPayment().setTotalCouponGoodsDcCost(filteredOrderList.getLast().getOrderPayment().getTotalCouponGoodsDcCost().add(CustomBigDecimalUtils.subtract(originPayment.getTotalCouponGoodsDcCost(), totalCouponGoodsDcCost)));
        filteredOrderList.getLast().getOrderPayment().setTotalCouponOrderDcCost(filteredOrderList.getLast().getOrderPayment().getTotalCouponOrderDcCost().add(CustomBigDecimalUtils.subtract(originPayment.getTotalCouponOrderDcCost(), totalCouponOrderDcCost)));
        filteredOrderList.getLast().getOrderPayment().setTotalCouponDeliveryDcCost(filteredOrderList.getLast().getOrderPayment().getTotalCouponDeliveryDcCost().add(CustomBigDecimalUtils.subtract(originPayment.getTotalCouponDeliveryDcCost(), totalCouponDeliveryDcCost)));
        filteredOrderList.getLast().getOrderPayment().setTotalUseMileage(filteredOrderList.getLast().getOrderPayment().getTotalUseMileage().add(CustomBigDecimalUtils.subtract(originPayment.getTotalUseMileage(), totalUseMileage)));
        filteredOrderList.getLast().getOrderPayment().setTotalUseDeposit(filteredOrderList.getLast().getOrderPayment().getTotalUseDeposit().add(CustomBigDecimalUtils.subtract(originPayment.getTotalUseDeposit(), totalUseDeposit)));
        filteredOrderList.getLast().getOrderPayment().setTotalEnuriDcPrice(filteredOrderList.getLast().getOrderPayment().getTotalEnuriDcPrice().add(CustomBigDecimalUtils.subtract(originPayment.getTotalEnuriDcPrice(), totalEnuriDcPrice)));
        filteredOrderList.getLast().getOrderPayment().setTotalPayCost(filteredOrderList.getLast().getOrderPayment().getTotalPayCost().add(CustomBigDecimalUtils.subtract(originPayment.getTotalPayCost(), totalPayCost)));
        filteredOrderList.getLast().getOrderPayment().setTotalWonCost(filteredOrderList.getLast().getOrderPayment().getTotalPayCost().divide(BigDecimal.valueOf(1.1), 0, RoundingMode.DOWN));
        filteredOrderList.getLast().getOrderPayment().setTotalTaxVatCost(CustomBigDecimalUtils.subtract(filteredOrderList.getLast().getOrderPayment().getTotalPayCost(), orderDataList.getLast().getOrderPayment().getTotalWonCost()));
    }

    /**
     * 정기배송 주문에 배송비가 있을 경우 주문 결제금액 비중으로 배송비를 분할한다.
     */
    private void adjustDeliveryCost(List<GodoOrderDelivery> delivery, List<OrderDataEntity> orderDataList) {

        BigDecimal totalDeliveryCost = delivery.stream().map(GodoOrderDelivery::getDeliveryCharge).reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalDeliveryCost.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }

        BigDecimal totalPaymentCost = orderDataList.stream()
                .map(OrderDataEntity::getOrderPayment)
                .toList().stream()
                .map(OrderPayment::getTotalPayCost).reduce(BigDecimal.ZERO, BigDecimal::add);

        orderDataList.forEach(order -> {
            BigDecimal divisionDeliveryCost;

            if (totalPaymentCost.compareTo(BigDecimal.ZERO) > 0) {
                divisionDeliveryCost = order.getOrderPayment().getTotalPayCost().divide(totalPaymentCost, 10, RoundingMode.DOWN).multiply(totalDeliveryCost).setScale(0, RoundingMode.DOWN);
            } else {
                divisionDeliveryCost = totalDeliveryCost.divide(BigDecimal.valueOf(orderDataList.size()), 0, RoundingMode.DOWN);
            }

            order.getOrderPayment().setTotalDeliveryCost(divisionDeliveryCost);
        });

        BigDecimal sumDeliveryCost = orderDataList.stream()
                .map(order -> order.getOrderPayment().getTotalDeliveryCost())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal diffDeliveryCost = totalDeliveryCost.subtract(sumDeliveryCost);
        orderDataList.getLast().getOrderPayment().setTotalDeliveryCost(orderDataList.getLast().getOrderPayment().getTotalDeliveryCost().add(diffDeliveryCost));
    }

    /**
     * 결제 금액 대비 품목 단가 금액을 계산한다.
     */
    private void adjustSubscriptionOrderGoodsPrice(GodoOrderGoods godoOrderGoods, List<OrderDataEntity> orderDataList) {

        List<OrderDataEntity> filteredOrderList = orderDataList.stream()
                .filter(order -> Optional.ofNullable(order.getOrderGoods())
                        .orElse(Collections.emptyList())
                        .stream()
                        .anyMatch(goods -> String.valueOf(godoOrderGoods.getSno()).equals(goods.getMarketOrderSubNo())))
                .toList();

        for (OrderDataEntity orderDataEntity : filteredOrderList) {

            BigDecimal orderPayPrice = orderDataEntity.getOrderPayment().getTotalPayCost();
            // OrderGoods 기준 금액 (OMS 품목 단가 기준) getSalesUnitPrice -> 수량 제외된 단가를 넣어서 수량을 곱하고 합계를 계산해야 한다.
            BigDecimal goodsTotalPrice = orderDataEntity.getOrderGoods().stream()
                    .filter(orderGoods -> orderGoods.getSalesUnitPrice() != null)
                    .map(orderGoods -> orderGoods.getSalesUnitPrice().multiply(BigDecimal.valueOf(orderGoods.getOrderCnt())))
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (goodsTotalPrice.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }

            // 고도몰 금액 비중 = 주문 금액 (품목 금액 + 옵션금액) / OMS 단가를 적용한 품목 전체 합계
            BigDecimal priceRatio = goodsTotalPrice.equals(BigDecimal.ZERO) ? BigDecimal.valueOf(100) : orderPayPrice.divide(goodsTotalPrice, 4, RoundingMode.HALF_UP);

            for (OrderGoods orderGoods : orderDataEntity.getOrderGoods()) {
                orderGoods.setSalesOrderPrice(CustomBigDecimalUtils.multiply(CustomBigDecimalUtils.multiply(orderGoods.getSalesOrderPrice(), BigDecimal.valueOf(orderGoods.getOrderCnt())), priceRatio));
                orderGoods.setSalesUnitPrice(orderGoods.getSalesOrderPrice().divide(BigDecimal.valueOf(orderGoods.getOrderCnt()), 4, RoundingMode.DOWN));
                // 공급가, 부가세는 품목 정보 업데이트 시 다시 계산
            }
            // priceRatio 로 적용된 품목 금액 합계
            // GodoOrderGoods 금액의 합과 안 맞을 수 있어서 차액을 계산하기 위해 합계를 구한다.
            BigDecimal totalSalesOrderPrice = orderDataEntity.getOrderGoods().stream().map(OrderGoods::getSalesOrderPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

            if (totalSalesOrderPrice.equals(BigDecimal.ZERO)) {
                continue;
            }

            // 기타 할인 금액 비중으로 적용
            for (OrderGoods orderGoods : orderDataEntity.getOrderGoods()) {
                // 품목 금액 / 배송일 품목 합계 (전체 금액 중 품목이 차지하는 비중 계산)
                BigDecimal goodsRatio = orderGoods.getSalesOrderPrice().divide(totalSalesOrderPrice, 4, RoundingMode.HALF_UP);

                orderGoods.setDcCost(CustomBigDecimalUtils.multiply(godoOrderGoods.getTotalDcPrice().divide(BigDecimal.valueOf(filteredOrderList.size())), goodsRatio));
                orderGoods.setGoodsDcCost(CustomBigDecimalUtils.multiply(godoOrderGoods.getGoodsDcPrice().divide(BigDecimal.valueOf(filteredOrderList.size())), goodsRatio));
                orderGoods.setMemberDcCost(CustomBigDecimalUtils.multiply(godoOrderGoods.getMemberDcPrice().divide(BigDecimal.valueOf(filteredOrderList.size())), goodsRatio));
                orderGoods.setCouponGoodsDcCost(CustomBigDecimalUtils.multiply(godoOrderGoods.getCouponGoodsDcPrice().divide(BigDecimal.valueOf(filteredOrderList.size())), goodsRatio));
                orderGoods.setCouponOrderDcCost(CustomBigDecimalUtils.multiply(godoOrderGoods.getDivisionCouponOrderDcPrice().divide(BigDecimal.valueOf(filteredOrderList.size())), goodsRatio));
                orderGoods.setUseMileage(CustomBigDecimalUtils.multiply(CustomBigDecimalUtils.add(godoOrderGoods.getDivisionUseMileage().divide(BigDecimal.valueOf(filteredOrderList.size())), godoOrderGoods.getDivisionGoodsDeliveryUseMileage().divide(BigDecimal.valueOf(filteredOrderList.size())), godoOrderGoods.getDivisionCouponOrderMileage().divide(BigDecimal.valueOf(filteredOrderList.size()))), goodsRatio));
                orderGoods.setUseDeposit(CustomBigDecimalUtils.multiply(CustomBigDecimalUtils.add(godoOrderGoods.getDivisionUseDeposit().divide(BigDecimal.valueOf(filteredOrderList.size())), godoOrderGoods.getDivisionGoodsDeliveryUseDeposit().divide(BigDecimal.valueOf(filteredOrderList.size()))), goodsRatio));
                orderGoods.setEnuriDcPrice(CustomBigDecimalUtils.multiply(godoOrderGoods.getEnuri().divide(BigDecimal.valueOf(filteredOrderList.size())), goodsRatio));
            }

            // 생성된 금액 합계 계산
            BigDecimal totalDcCost = orderDataEntity.getOrderGoods().stream().map(OrderGoods::getDcCost).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalGoodsDcCost = orderDataEntity.getOrderGoods().stream().map(OrderGoods::getGoodsDcCost).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalMemberDcCost = orderDataEntity.getOrderGoods().stream().map(OrderGoods::getMemberDcCost).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalCouponGoodsDcPrice = orderDataEntity.getOrderGoods().stream().map(OrderGoods::getCouponGoodsDcCost).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalCouponOrderDcPrice = orderDataEntity.getOrderGoods().stream().map(OrderGoods::getCouponOrderDcCost).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalUseMileage = orderDataEntity.getOrderGoods().stream().map(OrderGoods::getUseMileage).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalUseDeposit = orderDataEntity.getOrderGoods().stream().map(OrderGoods::getUseDeposit).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalEnuriDcPrice = orderDataEntity.getOrderGoods().stream().map(OrderGoods::getEnuriDcPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

            // 차액이 발생했으면 마지막 주문의 마지막 품목에 맞춘다.
            OrderGoods lastGoods = orderDataEntity.getOrderGoods().getLast();
            lastGoods.setSalesOrderPrice(lastGoods.getSalesOrderPrice().add(CustomBigDecimalUtils.subtract(orderDataEntity.getOrderPayment().getTotalPayCost(), totalSalesOrderPrice)));
            lastGoods.setSalesUnitPrice(lastGoods.getSalesOrderPrice().divide(BigDecimal.valueOf(lastGoods.getOrderCnt()), 4, RoundingMode.DOWN));
            lastGoods.setDcCost(lastGoods.getDcCost().add(CustomBigDecimalUtils.subtract(orderDataEntity.getOrderPayment().getTotalDcCost(), totalDcCost)));
            lastGoods.setGoodsDcCost(lastGoods.getGoodsDcCost().add(CustomBigDecimalUtils.subtract(orderDataEntity.getOrderPayment().getTotalGoodsDcCost(), totalGoodsDcCost)));
            lastGoods.setMemberDcCost(lastGoods.getMemberDcCost().add(CustomBigDecimalUtils.subtract(orderDataEntity.getOrderPayment().getTotalMemberDcCost(), totalMemberDcCost)));
            lastGoods.setCouponGoodsDcCost(lastGoods.getCouponGoodsDcCost().add(CustomBigDecimalUtils.subtract(orderDataEntity.getOrderPayment().getTotalCouponGoodsDcCost(), totalCouponGoodsDcPrice)));
            lastGoods.setCouponOrderDcCost(lastGoods.getCouponOrderDcCost().add(CustomBigDecimalUtils.subtract(orderDataEntity.getOrderPayment().getTotalCouponOrderDcCost(), totalCouponOrderDcPrice)));
            lastGoods.setUseMileage(lastGoods.getUseMileage().add(CustomBigDecimalUtils.subtract(orderDataEntity.getOrderPayment().getTotalUseMileage(), totalUseMileage)));
            lastGoods.setUseDeposit(lastGoods.getUseDeposit().add(CustomBigDecimalUtils.subtract(orderDataEntity.getOrderPayment().getTotalUseDeposit(), totalUseDeposit)));
            lastGoods.setEnuriDcPrice(lastGoods.getEnuriDcPrice().add(CustomBigDecimalUtils.subtract(orderDataEntity.getOrderPayment().getTotalEnuriDcPrice(), totalEnuriDcPrice)));
        }
    }

    private void setOrderTitle(GodoDataEntity godoData, DeliverySchedule deliverySchedule, String orderTitle) {
        if (ObjectUtils.isNotEmpty(deliverySchedule) && deliverySchedule.getDeliveryDays().size() > 1) {
            godoData.getOrder().setOrderGoodsNm(orderTitle + " - " + deliverySchedule.getDeliverySequence());
        } else {
            godoData.getOrder().setOrderGoodsNm(orderTitle);
        }
    }
}
