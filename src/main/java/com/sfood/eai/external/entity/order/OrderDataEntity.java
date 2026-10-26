package com.sfood.eai.external.entity.order;

import com.sfood.eai.external.entity.Warehouse;
import com.sfood.eai.external.entity.id.order.OrderClaimId;
import com.sfood.eai.external.type.OrderStatus;
import com.sfood.eai.external.util.CustomDateUtils;
import com.sfood.eai.external.util.CustomObjectUtils;
import lombok.Builder;
import lombok.Getter;
import org.springframework.cglib.core.Local;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.sfood.eai.external.type.Constants.EXTERNAL_USER_ID;

@Builder
@Getter
public class OrderDataEntity {
    private OrderType orderType;
    private OrderMaster orderMaster;
    private List<OrderGoods> orderGoods;
    private List<OrderDelivery> orderDelivery;
    private OrderPayment orderPayment;
    private List<OrderClaim> orderClaim;
    private Warehouse returnWarehouse;
    private boolean isDoneClaim;    // 메디쏠라 중복 처리 방지

    public String getCompanyCode() {
        return orderMaster.getCompanyCode();
    }

    public String getChannelCode() {
        return orderMaster.getOrderChannelCode();
    }

    public String getMarketMallCode() {
        return orderMaster.getMarketMallCode();
    }

    public String getMarketOrderNo() {
        return orderMaster.getMarketOrderNo();
    }

    public LocalDateTime getOrderDate() {
        return orderMaster.getOrderDate();
    }

    public LocalDateTime getPayDate() {
        return orderPayment.getPayDate();
    }

    public String getOrderPartnerCode() {
        return orderMaster.getOrderPartnerCode();
    }

    public void setReturnWarehouse(Warehouse warehouse) {
        this.returnWarehouse = warehouse;
    }

    public boolean isChanged() {
        // 1. 상태 변경 체크
        // 현재 신규주문, 업데이트 주문확정(대기) 이면 업데이트
        if (this.orderGoods.stream().anyMatch(OrderGoods::isConfirmed)) {
            return true;
        }

        // 2. 주문 데이터 변경 체크
        boolean isOrderChanged = this.orderGoods.stream().anyMatch(OrderGoods::isChanged)
                || this.orderDelivery.stream().anyMatch(OrderDelivery::isChanged)
                || this.orderPayment.isChanged();

        this.orderGoods.forEach(goods -> {
            // 주문확정(대기) 건 외에는 업데이트 상태 변경을 하지 않는다.
            // 추가된 품목은 PreOrderStatusCode 없어서 제외
            if (StringUtils.hasText(goods.getPreOrderStatusCode())) {
                goods.setOrderStatusCode(goods.getPreOrderStatusCode());
            }
            // 변경된 데이터가 있으면 수정자 ID를 변경해서 WMS에서 조회할 수 있게 한다.
            if (isOrderChanged) {
                goods.setUpdateId(EXTERNAL_USER_ID);
            }
        });

        return isOrderChanged;
    }

    public void setOrderNumber(String orderMergeNo) {
        this.orderMaster.setOrderMergeNo(orderMergeNo);
        this.orderMaster.setOrderNo(this.orderMaster.getOrderMergeNo().substring(this.orderMaster.getOrderMergeNo().length() - 5, this.orderMaster.getOrderMergeNo().length()));
        this.orderGoods.forEach(orderGoods -> orderGoods.setOrderMergeNo(orderMergeNo));
        this.orderDelivery.forEach(orderDelivery -> orderDelivery.setOrderMergeNo(orderMergeNo));
        this.orderPayment.setOrderMergeNo(orderMergeNo);

        if (!ObjectUtils.isEmpty(this.orderClaim)) {
            this.orderClaim.forEach(claim -> claim.getId().setOrderMergeNo(orderMergeNo));
        }
    }

    public void update(OrderDataEntity updateOrderData) {

        this.orderMaster.update(updateOrderData.getOrderMaster());

        Map<Integer, Integer> updateKeyMap = updateGoods(updateOrderData);
        updateDelivery(updateOrderData, updateKeyMap);

        if (!ObjectUtils.isEmpty(this.orderPayment)) {
            this.orderPayment.update(updateOrderData.getOrderPayment());
        }
    }

    public void cancel(OrderDataEntity cancelOrder) {

        if (this.isDoneClaim) {
            return;
        }

        for (OrderGoods cancelOrderGoods : cancelOrder.getOrderGoods()) {
            // 수집된 취소 주문으로 원 주문의 취소 대상 품목 찾기 + 취소 가능 여부 상태 체크
            Optional<OrderGoods> matchedOrderGoods = findCancelableOrderGoods(cancelOrderGoods);

            if (matchedOrderGoods.isEmpty()) {
                continue;
            }

            OrderGoods originOrderGoods = matchedOrderGoods.get();

            Optional<OrderClaim> matchedOrderClaim = findOrderClaimByOrderGoods(cancelOrderGoods, cancelOrder.getOrderClaim());

            if (matchedOrderClaim.isEmpty()) {
                continue;
            }

            OrderClaim newOrderClaim = matchedOrderClaim.get();

            originOrderGoods.setCancelCnt(cancelOrderGoods.getOrderCnt());
            originOrderGoods.setCancelReasonMstCode(newOrderClaim.getClaimReasonMstCode());
            originOrderGoods.setCancelReasonDetailCode(newOrderClaim.getClaimReasonDetailCode());
            originOrderGoods.setCancelReasonContent(newOrderClaim.getClaimReasonContent());
            originOrderGoods.setPreOrderStatusCode(originOrderGoods.getOrderStatusCode());
            originOrderGoods.setOrderStatusCode(cancelOrderGoods.getOrderStatusCode());
            originOrderGoods.setSalesOrderPrice(BigDecimal.ZERO);
            originOrderGoods.setSalesSupplyPrice(BigDecimal.ZERO);
            originOrderGoods.setSalesVatPrice(BigDecimal.ZERO);
            originOrderGoods.setDcCost(BigDecimal.ZERO);
            originOrderGoods.setUpdateId(EXTERNAL_USER_ID);
        }

        cancelGiftGoods();

        this.isDoneClaim = true;
    }

    public void refund(OrderDataEntity refundOrder) {

        if (this.isDoneClaim) {
            return;
        }

        for (OrderGoods refundOrderGoods : refundOrder.getOrderGoods()) {
            // 수집된 환불 주문으로 취소완료가 아닌 주문 품목 중 환불 대상 동일 품목 찾기 (상세번호 기준), 메디쏠라 영향으로 리스트 추출
            List<OrderGoods> matchedOrderGoodsList = this.getOrderGoods().stream()
                    .filter(goods -> goods.getMarketOrderNo().equals(refundOrderGoods.getMarketOrderNo())
                            && goods.getMarketOrderSubNo().equals(refundOrderGoods.getMarketOrderSubNo()))
                    .toList();

            // 매칭되는 주문 품목리스트가 있는 경우 전액 환불이기 때문에 매칭된 품목을 취소 처리한다.
            if (!CollectionUtils.isEmpty(matchedOrderGoodsList)) {

                boolean isCompletedClaim = false;

                for (OrderGoods target : matchedOrderGoodsList) {
                    if (target.isCanceled() || target.isReturning()) {
                        isCompletedClaim = true;
                    }
                }
                // 이미 취소 상태이거나 반품 상태이면 리턴
                if (isCompletedClaim) {
                    this.isDoneClaim = true;
                    continue;
                }

                for (OrderGoods refundTargetGoods : matchedOrderGoodsList) {

                    // 환불 클레임 중 refundOrderGoods 의 클레임 찾기
                    Optional<OrderClaim> matchedOrderClaim = findOrderClaimByOrderGoods(refundOrderGoods, refundOrder.getOrderClaim());

                    // 고도몰 클레임 정보가 없으면 넘긴다.
                    if (matchedOrderClaim.isEmpty()) {
                        continue;
                    }

                    // 상품 준비중일 때 : 주문채널 > 네이버페이 통해서 취소요청 > 고도몰 환불접수 리스트로 들어옴(환불접수 상태) > OMS 취소요청
                    if ((OrderStatus.OS2020.equals(refundTargetGoods.getOrderStatusCode()) && "naverpay".equals(refundOrder.getOrderPayment().getPaymentMsg()))) {
                        refundTargetGoods.setPreOrderStatusCode(refundTargetGoods.getOrderStatusCode());
                        refundTargetGoods.setOrderStatusCode(OrderStatus.OS3010.code());
                        refundTargetGoods.setUpdateId(EXTERNAL_USER_ID);
                        continue;
                    }

                    // 물류 진행 중이면 취소요청 처리
                    // 반품처리로 변경 2025-10-20
                    if (refundTargetGoods.isShipping()) {
                        // 메디쏠라는 동일한 refundOrderGoods 으로 처리하기 때문에 복사해서 사용함.
                        OrderGoods addReturnOrderGoods = CustomObjectUtils.deepCopy(refundOrderGoods, OrderGoods.class);
                        refundOrderGoods(addReturnOrderGoods, refundTargetGoods, matchedOrderClaim);
                    } else {
                        // 주문확정(결제완료) ~ 출고요청까지는 취소완료 처리
                        // 결제완료 후 환불접수이면 취소완료 처리 (정상적인 환불 절차)
                        refundTargetGoods.setCancelCnt(refundOrderGoods.getOrderCnt());
                        refundTargetGoods.setCancelReasonMstCode(matchedOrderClaim.get().getClaimReasonMstCode());
                        refundTargetGoods.setCancelReasonDetailCode(matchedOrderClaim.get().getClaimReasonDetailCode());
                        refundTargetGoods.setCancelReasonContent(matchedOrderClaim.get().getClaimReasonContent());
                        refundTargetGoods.setPreOrderStatusCode(refundTargetGoods.getOrderStatusCode());
                        refundTargetGoods.setOrderStatusCode(OrderStatus.OS3030.code());
                        refundTargetGoods.setSalesOrderPrice(BigDecimal.ZERO);
                        refundTargetGoods.setSalesSupplyPrice(BigDecimal.ZERO);
                        refundTargetGoods.setSalesVatPrice(BigDecimal.ZERO);
                        refundTargetGoods.setDcCost(BigDecimal.ZERO);
                        refundTargetGoods.setUpdateId(EXTERNAL_USER_ID);
                    }

                    this.isDoneClaim = true;
                }
            } else {
                // 매칭되는 주문 품목이 없으면 부분 환불
                // 주문 품목 중 환불된 고도몰 품목 코드로 원 대상 주문 품목 찾기.
                Optional<OrderGoods> targetOrderGoods = this.getOrderGoods().stream()
                        .filter(goods -> goods.getMarketOrderNo().equals(refundOrderGoods.getMarketOrderNo())
                                && goods.getMarketGoodsCode().equals(refundOrderGoods.getMarketGoodsCode()))
                        .sorted(Comparator.comparing(OrderGoods::getOrderDetailSeq))
                        .findFirst();

                if(targetOrderGoods.isEmpty()) {
                    continue;
                }

                // 이미 취소 / 반품 상태이면 넘긴다.
                if (targetOrderGoods.get().isCanceled() || targetOrderGoods.get().isReturning()) {
                    continue;
                }

                // 환불 클레임 중 refundOrderGoods 의 클레임 찾기
                Optional<OrderClaim> matchedOrderClaim = findOrderClaimByOrderGoods(refundOrderGoods, refundOrder.getOrderClaim());

                if (matchedOrderClaim.isEmpty()) {
                    continue;
                }

                // 출하대기, 출고완료이면 반품 처리
                if (targetOrderGoods.get().isShipping()) {
                    refundOrderGoods(refundOrderGoods, targetOrderGoods.get(), matchedOrderClaim);
                } else {
                    // 물류 진행 중이 아니면 원 주문 품목 취소 후 남은 수량 만큼 신규 등록 진행

                    // 원 주문 품목 취소 처리
                    OrderGoods addOrderGoods = CustomObjectUtils.deepCopy(targetOrderGoods.get(), OrderGoods.class);

                    targetOrderGoods.get().setCancelCnt(targetOrderGoods.get().getOrderCnt());
                    targetOrderGoods.get().setCancelReasonMstCode(matchedOrderClaim.get().getClaimReasonMstCode());
                    targetOrderGoods.get().setCancelReasonDetailCode(matchedOrderClaim.get().getClaimReasonDetailCode());
                    targetOrderGoods.get().setCancelReasonContent(matchedOrderClaim.get().getClaimReasonContent());
                    targetOrderGoods.get().setPreOrderStatusCode(targetOrderGoods.get().getOrderStatusCode());
                    targetOrderGoods.get().setOrderStatusCode(OrderStatus.OS3030.code());
                    targetOrderGoods.get().setSalesOrderPrice(BigDecimal.ZERO);
                    targetOrderGoods.get().setSalesSupplyPrice(BigDecimal.ZERO);
                    targetOrderGoods.get().setSalesVatPrice(BigDecimal.ZERO);
                    targetOrderGoods.get().setDcCost(BigDecimal.ZERO);
                    targetOrderGoods.get().setUpdateId(EXTERNAL_USER_ID);

                    // 남은 수량으로 신규 품목 등록
                    this.orderMaster.increaseDetailMaxSequence();
                    addOrderGoods.setOrderDetailSeq(this.orderMaster.getMaxOrderDetailSeq());
                    addOrderGoods.setOrderStatusCode(OrderStatus.OS1030.code());
                    addOrderGoods.setOrderCnt(Math.abs(targetOrderGoods.get().getOrderCnt() - refundOrderGoods.getOrderCnt()));
                    addOrderGoods.setAssignOrderCnt(addOrderGoods.getOrderCnt());
                    addOrderGoods.setDcCost(addOrderGoods.getDcCost().subtract(refundOrderGoods.getDcCost()));
                    addOrderGoods.setGoodsDcCost(addOrderGoods.getGoodsDcCost().subtract(refundOrderGoods.getGoodsDcCost()));
                    addOrderGoods.setMemberDcCost(addOrderGoods.getMemberDcCost().subtract(refundOrderGoods.getMemberDcCost()));
                    addOrderGoods.setCouponGoodsDcCost(addOrderGoods.getCouponGoodsDcCost().subtract(refundOrderGoods.getCouponGoodsDcCost()));
                    addOrderGoods.setCouponOrderDcCost(addOrderGoods.getCouponOrderDcCost().subtract(refundOrderGoods.getCouponOrderDcCost()));
                    addOrderGoods.setCouponDeliveryDcCost(addOrderGoods.getCouponDeliveryDcCost().subtract(refundOrderGoods.getCouponDeliveryDcCost()));
                    addOrderGoods.setUseMileage(addOrderGoods.getUseMileage().subtract(refundOrderGoods.getUseMileage()));
                    addOrderGoods.setUseDeposit(addOrderGoods.getUseDeposit().subtract(refundOrderGoods.getUseDeposit()));
                    addOrderGoods.setEnuriDcPrice(addOrderGoods.getEnuriDcPrice().subtract(refundOrderGoods.getEnuriDcPrice()));

                    addOrderGoods.setSalesOrderPrice(refundOrderGoods.getOriginSalesUnitPrice().multiply(BigDecimal.valueOf(addOrderGoods.getOrderCnt())).subtract(addOrderGoods.getDcCost()).setScale(0, RoundingMode.DOWN));
                    addOrderGoods.setSalesUnitPrice(addOrderGoods.getSalesOrderPrice().divide(BigDecimal.valueOf(Math.abs(addOrderGoods.getOrderCnt())), 4, RoundingMode.DOWN));

                    // 원 주문에 부가세가 존재했으면..
                    if (targetOrderGoods.get().getSalesVatUnitPrice().compareTo(BigDecimal.ZERO) > 0) {
                        addOrderGoods.setSalesSupplyPrice(addOrderGoods.getSalesOrderPrice().divide(BigDecimal.valueOf(1.1), 0, RoundingMode.DOWN));
                        addOrderGoods.setSalesVatPrice(addOrderGoods.getSalesOrderPrice().subtract(addOrderGoods.getSalesSupplyPrice()));
                        addOrderGoods.setSalesSupplyUnitPrice(addOrderGoods.getSalesUnitPrice().divide(BigDecimal.valueOf(1.1),4, RoundingMode.DOWN));
                        addOrderGoods.setSalesVatUnitPrice(addOrderGoods.getSalesUnitPrice().subtract(addOrderGoods.getSalesSupplyUnitPrice()));
                    } else {
                        addOrderGoods.setSalesSupplyPrice(addOrderGoods.getSalesOrderPrice());
                        addOrderGoods.setSalesVatPrice(BigDecimal.ZERO);
                        addOrderGoods.setSalesSupplyUnitPrice(addOrderGoods.getSalesUnitPrice());
                        addOrderGoods.setSalesVatUnitPrice(BigDecimal.ZERO);
                    }

                    this.orderGoods.add(addOrderGoods);

                    Optional<OrderDelivery> matchedOrderDelivery = findOrderDeliveryByOrderGoods(targetOrderGoods.get());
                    OrderDelivery orderDelivery = CustomObjectUtils.deepCopy(matchedOrderDelivery.get(), OrderDelivery.class);
                    orderDelivery.setOrderDetailSeq(addOrderGoods.getOrderDetailSeq());
                    this.orderDelivery.add(orderDelivery);
                }

                this.isDoneClaim = true;
            }
        }

        cancelGiftGoods();

    }

    public void exchange(OrderDataEntity exchangeOrder) {

        if (this.isDoneClaim) {
            return;
        }

        for (OrderGoods exchangeOrderGoods : exchangeOrder.getOrderGoods()) {

            Optional<OrderGoods> matchedOrderGoods = findFinishedOrderGoodsByMarketGoodsCode(exchangeOrderGoods);

            if (matchedOrderGoods.isEmpty()) {
                continue;
            }

            boolean existsExchangeOrderGoods = this.getOrderGoods().stream()
                    .filter(goods -> goods.getMarketOrderNo().equals(exchangeOrderGoods.getMarketOrderNo())
                            && goods.getMarketOrderSubNo().equals(exchangeOrderGoods.getMarketOrderSubNo()))
                    .anyMatch(goods -> goods.getOrderStatusCode().startsWith("OS40"));

            if (existsExchangeOrderGoods) {
                continue;
            }

            OrderGoods originOrderGoods = matchedOrderGoods.get();

            Optional<OrderClaim> matchedOrderClaim = findOrderClaimByOrderGoods(exchangeOrderGoods, exchangeOrder.getOrderClaim());

            if (matchedOrderClaim.isEmpty()) {
                continue;
            }

            this.orderMaster.increaseDetailMaxSequence();
            exchangeOrderGoods.setOrderMergeNo(originOrderGoods.getOrderMergeNo());
            exchangeOrderGoods.setOrderDetailSeq(this.orderMaster.getMaxOrderDetailSeq());
            exchangeOrderGoods.setClaimOrderDetailSeq(originOrderGoods.getOrderDetailSeq());
            exchangeOrderGoods.setOrderGoodsCode(originOrderGoods.getOrderGoodsCode());
            exchangeOrderGoods.setOrderGoodsName(originOrderGoods.getOrderGoodsName());
            exchangeOrderGoods.setErpGoodsCode(originOrderGoods.getErpGoodsCode());
            exchangeOrderGoods.setErpGoodsName(originOrderGoods.getErpGoodsName());
            exchangeOrderGoods.setGoodsWmsApiYn(originOrderGoods.getGoodsWmsApiYn());
            exchangeOrderGoods.setSalesUnit(originOrderGoods.getSalesUnit());
            exchangeOrderGoods.setFromWarehouseCode(!ObjectUtils.isEmpty(this.returnWarehouse) ? this.returnWarehouse.getId().getWarehouseCode() : originOrderGoods.getFromWarehouseCode());
            exchangeOrderGoods.setBundleYn(originOrderGoods.getBundleYn());
            exchangeOrderGoods.setBarcode(originOrderGoods.getBarcode());
            exchangeOrderGoods.setSubscriptionType(originOrderGoods.getSubscriptionType());
            exchangeOrderGoods.setOrderCnt(Math.abs(exchangeOrderGoods.getOrderCnt()) * -1);
            exchangeOrderGoods.setAssignOrderCnt(Math.abs(exchangeOrderGoods.getOrderCnt()) * -1);
            exchangeOrderGoods.setSalesOrderPrice(originOrderGoods.getSalesUnitPrice().multiply(BigDecimal.valueOf(exchangeOrderGoods.getOrderCnt())).abs().multiply(BigDecimal.valueOf(-1)).setScale(0, RoundingMode.DOWN));
            exchangeOrderGoods.setSalesSupplyPrice(originOrderGoods.getSalesSupplyUnitPrice().multiply(BigDecimal.valueOf(exchangeOrderGoods.getOrderCnt())).abs().multiply(BigDecimal.valueOf(-1)));
            exchangeOrderGoods.setSalesVatPrice(originOrderGoods.getSalesVatUnitPrice().multiply(BigDecimal.valueOf(exchangeOrderGoods.getOrderCnt())).abs().multiply(BigDecimal.valueOf(-1)));
            exchangeOrderGoods.setSalesUnitPrice(originOrderGoods.getSalesUnitPrice());
            exchangeOrderGoods.setSalesSupplyUnitPrice(originOrderGoods.getSalesSupplyUnitPrice());
            exchangeOrderGoods.setSalesVatUnitPrice(originOrderGoods.getSalesVatUnitPrice());
            exchangeOrderGoods.setReceiptYn(originOrderGoods.getReceiptYn());
            exchangeOrderGoods.setReturnReqDate(CustomDateUtils.parseLocalDate(matchedOrderClaim.get().getClaimDate()));

            this.orderGoods.add(exchangeOrderGoods);

            Optional<OrderDelivery> matchedOrderDelivery = findOrderDeliveryByOrderGoods(originOrderGoods);
            addOrderDeliveryOfClaim(matchedOrderDelivery, exchangeOrderGoods);

            OrderClaim orderClaim = matchedOrderClaim.get();
            addOrderClaimIfAbsent(orderClaim, exchangeOrderGoods);

            OrderGoods addOrderGoods = CustomObjectUtils.deepCopy(exchangeOrderGoods, OrderGoods.class);

            this.orderMaster.increaseDetailMaxSequence();
            addOrderGoods.setOrderDetailSeq(this.orderMaster.getMaxOrderDetailSeq());
            addOrderGoods.setOrderStatusCode(OrderStatus.OS1030.code());
            addOrderGoods.setOrderCnt(Math.abs(exchangeOrderGoods.getOrderCnt()));
            addOrderGoods.setAssignOrderCnt(Math.abs(exchangeOrderGoods.getOrderCnt()));
            addOrderGoods.setFromWarehouseCode(originOrderGoods.getFromWarehouseCode());
            addOrderGoods.setSalesOrderPrice(addOrderGoods.getSalesUnitPrice().multiply(BigDecimal.valueOf(addOrderGoods.getOrderCnt())).setScale(0, RoundingMode.DOWN));
            addOrderGoods.setSalesSupplyPrice(addOrderGoods.getSalesSupplyUnitPrice().multiply(BigDecimal.valueOf(addOrderGoods.getOrderCnt())));
            addOrderGoods.setSalesVatPrice(addOrderGoods.getSalesVatUnitPrice().multiply(BigDecimal.valueOf(addOrderGoods.getOrderCnt())));
            this.orderGoods.add(addOrderGoods);

            matchedOrderDelivery = findOrderDeliveryByOrderGoods(originOrderGoods);
            OrderDelivery orderDelivery = CustomObjectUtils.deepCopy(matchedOrderDelivery.get(), OrderDelivery.class);
            orderDelivery.setOrderDetailSeq(addOrderGoods.getOrderDetailSeq());
            this.orderDelivery.add(orderDelivery);

            this.isDoneClaim = true;
        }
    }

    public void returned(OrderDataEntity returnOrder) {

        if (this.isDoneClaim) {
            return;
        }

        for (OrderGoods returnOrderGoods : returnOrder.getOrderGoods()) {

            Optional<OrderGoods> matchedOrderGoods = findFinishedOrderGoods(returnOrderGoods);

            if (matchedOrderGoods.isEmpty()) {
                continue;
            }

            boolean existsReturnOrderGoods = this.getOrderGoods().stream()
                    .filter(goods -> goods.getMarketOrderNo().equals(returnOrderGoods.getMarketOrderNo())
                            && goods.getMarketOrderSubNo().equals(returnOrderGoods.getMarketOrderSubNo()))
                    .anyMatch(goods -> goods.getOrderStatusCode().startsWith("OS50"));

            if (existsReturnOrderGoods) {
                continue;
            }

            OrderGoods originOrderGoods = matchedOrderGoods.get();

            Optional<OrderClaim> matchedOrderClaim = findOrderClaimByOrderGoods(returnOrderGoods, returnOrder.getOrderClaim());

            if (matchedOrderClaim.isEmpty()) {
                continue;
            }

            // 원 주문의 반품 등록
            this.orderMaster.increaseDetailMaxSequence();
            returnOrderGoods.setOrderMergeNo(originOrderGoods.getOrderMergeNo());
            returnOrderGoods.setOrderDetailSeq(this.orderMaster.getMaxOrderDetailSeq());
            returnOrderGoods.setClaimOrderDetailSeq(originOrderGoods.getOrderDetailSeq());
            returnOrderGoods.setOrderGoodsCode(originOrderGoods.getOrderGoodsCode());
            returnOrderGoods.setOrderGoodsName(originOrderGoods.getOrderGoodsName());
            returnOrderGoods.setErpGoodsCode(originOrderGoods.getErpGoodsCode());
            returnOrderGoods.setErpGoodsName(originOrderGoods.getErpGoodsName());
            returnOrderGoods.setGoodsWmsApiYn(originOrderGoods.getGoodsWmsApiYn());
            returnOrderGoods.setSalesUnit(originOrderGoods.getSalesUnit());
            returnOrderGoods.setFromWarehouseCode(!ObjectUtils.isEmpty(this.returnWarehouse) ? this.returnWarehouse.getId().getWarehouseCode() : originOrderGoods.getFromWarehouseCode());
            returnOrderGoods.setBundleYn(originOrderGoods.getBundleYn());
            returnOrderGoods.setBarcode(originOrderGoods.getBarcode());
            returnOrderGoods.setSubscriptionType(originOrderGoods.getSubscriptionType());
            returnOrderGoods.setOrderCnt(Math.abs(returnOrderGoods.getOrderCnt()) * -1);
            returnOrderGoods.setAssignOrderCnt(Math.abs(returnOrderGoods.getOrderCnt()) * -1);
            returnOrderGoods.setSalesOrderPrice(originOrderGoods.getSalesOrderPrice().abs().multiply(BigDecimal.valueOf(-1)).setScale(0, RoundingMode.DOWN));

            if (originOrderGoods.getSalesVatUnitPrice().compareTo(BigDecimal.ZERO) > 0) {
                returnOrderGoods.setSalesSupplyPrice(originOrderGoods.getSalesSupplyPrice().abs().multiply(BigDecimal.valueOf(-1)));
                returnOrderGoods.setSalesVatPrice(originOrderGoods.getSalesSupplyPrice().subtract(returnOrderGoods.getSalesSupplyPrice()));
            }
            returnOrderGoods.setSalesUnitPrice(originOrderGoods.getSalesUnitPrice());
            returnOrderGoods.setSalesSupplyUnitPrice(originOrderGoods.getSalesSupplyUnitPrice());
            returnOrderGoods.setSalesVatUnitPrice(originOrderGoods.getSalesVatUnitPrice());
            returnOrderGoods.setReceiptYn(originOrderGoods.getReceiptYn());
            this.orderGoods.add(returnOrderGoods);

            Optional<OrderDelivery> matchedOrderDelivery = findOrderDeliveryByOrderGoods(originOrderGoods);
            addOrderDeliveryOfClaim(matchedOrderDelivery, returnOrderGoods);

            OrderClaim newOrderClaim = matchedOrderClaim.get();
            addOrderClaimIfAbsent(newOrderClaim, returnOrderGoods);

            this.isDoneClaim = true;
        }
    }

    public boolean validate() {
        boolean isOrderDataValid;

        isOrderDataValid = this.orderMaster.validate();

        if (!isOrderDataValid) {
            this.orderGoods.forEach(OrderGoods::error);
            return false;
        }

        for (OrderGoods goods : this.orderGoods) {
            boolean isOrderGoodsValid = goods.validate();

            if (!isOrderGoodsValid) {
                isOrderDataValid = false;
            }
        }

        boolean isPaymentValid = this.orderPayment.validate();

        if (!isPaymentValid) {
            isOrderDataValid = false;
        }

        if (isOrderDataValid) {
            this.orderGoods.forEach(OrderGoods::confirm);
        }

        return isOrderDataValid;
    }

    public boolean isUpdatable() {

        OptionalInt maxStatus = this.orderGoods.stream()
                .filter(goods -> Integer.parseInt(goods.getOrderStatusCode().substring(2)) < OrderStatus.OS3010.codeToInt()
                 || Integer.parseInt(goods.getOrderStatusCode().substring(2)) > OrderStatus.OS6040.codeToInt())
                .mapToInt(goods -> Integer.parseInt(goods.getOrderStatusCode().substring(2)))
                .max();

        return !maxStatus.isEmpty() && (maxStatus.getAsInt() <= OrderStatus.OS2020.codeToInt() || maxStatus.getAsInt() >= OrderStatus.OS8010.codeToInt());
    }

    private Map<Integer, Integer> updateGoods(OrderDataEntity updateOrderData) {

        Map<String, OrderGoods> existGoodsMap = this.orderGoods.stream()
                .filter(goods -> !OrderStatus.OS3030.code().equals(goods.getOrderStatusCode()))
                .collect(Collectors.toMap(OrderGoods::getMarketOrderSubNo, goods -> goods));

        if (existGoodsMap.isEmpty()) {
            return null;
        }

        Map<Integer, Integer> updateSequenceMap = new HashMap<>();

        for (OrderGoods updateOrderGoods : updateOrderData.orderGoods) {

            String mallOrderGoodsNo = updateOrderGoods.getMarketOrderSubNo();

            if (existGoodsMap.containsKey(mallOrderGoodsNo)) {
                // update
                existGoodsMap.get(mallOrderGoodsNo).update(updateOrderGoods);
                updateSequenceMap.put(updateOrderGoods.getOrderDetailSeq(), existGoodsMap.get(mallOrderGoodsNo).getOrderDetailSeq());
            } else {
                // add
                int maxSeq = this.orderGoods.stream().map(OrderGoods::getOrderDetailSeq).max(Integer::compareTo).orElse(0) + 1;
                updateSequenceMap.put(updateOrderGoods.getOrderDetailSeq(), maxSeq);
                updateOrderGoods.setOrderGoodsId(this.orderMaster.getOrderMergeNo(), maxSeq);
                this.orderGoods.add(updateOrderGoods);
            }
        }

        return updateSequenceMap;
    }

    private void updateDelivery(OrderDataEntity updateOrderData, Map<Integer, Integer> updateSequenceMap) {

        Map<Integer, OrderDelivery> existDeliveryMap = this.orderDelivery.stream()
                .collect(Collectors.toMap(OrderDelivery::getOrderDetailSeq, delivery -> delivery));

        for (OrderDelivery updateOrderDelivery : updateOrderData.orderDelivery) {
            if (updateSequenceMap.containsKey(updateOrderDelivery.getOrderDetailSeq())) {
                int detailSeq = updateSequenceMap.get(updateOrderDelivery.getOrderDetailSeq());

                if (existDeliveryMap.containsKey(detailSeq)) {
                    existDeliveryMap.get(detailSeq).update(updateOrderDelivery);
                } else {
                    updateOrderDelivery.setOrderDeliveryId(this.orderMaster.getOrderMergeNo(), detailSeq);
                    LocalDateTime hopeDelvDate = this.orderDelivery
                            .stream()
                            .filter(delivery -> Objects.nonNull(delivery.getHopeDelvDate()))
                            .map(OrderDelivery::getHopeDelvDate)
                            .max(LocalDateTime::compareTo).orElse(null);
                    updateOrderDelivery.setHopeDelvDate(hopeDelvDate);
                    this.orderDelivery.add(updateOrderDelivery);
                }
            }
        }
    }

    private void refundOrderGoods(OrderGoods refundOrderGoods, OrderGoods targetOrderGoods, Optional<OrderClaim> matchedOrderClaim) {
        this.orderMaster.increaseDetailMaxSequence();
        refundOrderGoods.setOrderGoodsId(this.orderMaster.getOrderMergeNo(), this.orderMaster.getMaxOrderDetailSeq());
        refundOrderGoods.setClaimOrderDetailSeq(targetOrderGoods.getOrderDetailSeq());
        refundOrderGoods.setOrderGoodsCode(targetOrderGoods.getOrderGoodsCode());
        refundOrderGoods.setOrderGoodsName(targetOrderGoods.getOrderGoodsName());
        refundOrderGoods.setErpGoodsCode(targetOrderGoods.getErpGoodsCode());
        refundOrderGoods.setErpGoodsName(targetOrderGoods.getErpGoodsName());
        refundOrderGoods.setGoodsWmsApiYn(targetOrderGoods.getGoodsWmsApiYn());
        refundOrderGoods.setSalesUnit(targetOrderGoods.getSalesUnit());
        refundOrderGoods.setBundleYn(targetOrderGoods.getBundleYn());
        refundOrderGoods.setBarcode(targetOrderGoods.getBarcode());
        refundOrderGoods.setSubscriptionType(targetOrderGoods.getSubscriptionType());
        refundOrderGoods.setOrderCnt(Math.abs(refundOrderGoods.getOrderCnt()) * -1);
        refundOrderGoods.setAssignOrderCnt(Math.abs(refundOrderGoods.getOrderCnt()) * -1);
        refundOrderGoods.setSalesUnitPrice(targetOrderGoods.getSalesUnitPrice());
        refundOrderGoods.setSalesSupplyUnitPrice(targetOrderGoods.getSalesSupplyUnitPrice());
        refundOrderGoods.setSalesVatUnitPrice(targetOrderGoods.getSalesVatUnitPrice());
        refundOrderGoods.setReturnReqDate(CustomDateUtils.parseLocalDate(matchedOrderClaim.get().getClaimDate()));
        refundOrderGoods.setReceiptYn(targetOrderGoods.getReceiptYn());
        refundOrderGoods.setFromWarehouseCode(!ObjectUtils.isEmpty(this.returnWarehouse) ? this.returnWarehouse.getId().getWarehouseCode() : targetOrderGoods.getFromWarehouseCode());
        refundOrderGoods.setOrderStatusCode(OrderStatus.OS5020.code());

        // 수량이 동일하면 그대로 금액 - 처리
        if (targetOrderGoods.getOrderCnt().equals(Math.abs(refundOrderGoods.getOrderCnt()))) {
            refundOrderGoods.setSalesOrderPrice(targetOrderGoods.getSalesOrderPrice().multiply(BigDecimal.valueOf(-1)));
            refundOrderGoods.setSalesSupplyPrice(targetOrderGoods.getSalesSupplyPrice().multiply(BigDecimal.valueOf(-1)));
            refundOrderGoods.setSalesVatPrice(targetOrderGoods.getSalesVatPrice().multiply(BigDecimal.valueOf(-1)));
        } else {
            refundOrderGoods.setSalesOrderPrice(refundOrderGoods.getOriginSalesUnitPrice().multiply(BigDecimal.valueOf(refundOrderGoods.getOrderCnt())).add(refundOrderGoods.getDcCost()).setScale(0, RoundingMode.DOWN));
            refundOrderGoods.setSalesUnitPrice(refundOrderGoods.getSalesOrderPrice().divide(BigDecimal.valueOf(Math.abs(refundOrderGoods.getOrderCnt())), 4, RoundingMode.DOWN).abs());

            // 원 주문에 부가세가 존재했으면..
            if (targetOrderGoods.getSalesVatUnitPrice().compareTo(BigDecimal.ZERO) > 0) {
                refundOrderGoods.setSalesSupplyPrice(refundOrderGoods.getSalesOrderPrice().divide(BigDecimal.valueOf(1.1), 0, RoundingMode.DOWN));
                refundOrderGoods.setSalesVatPrice(refundOrderGoods.getSalesOrderPrice().subtract(refundOrderGoods.getSalesSupplyPrice()));
                refundOrderGoods.setSalesSupplyUnitPrice(refundOrderGoods.getSalesUnitPrice().divide(BigDecimal.valueOf(1.1),4, RoundingMode.DOWN));
                refundOrderGoods.setSalesVatUnitPrice(refundOrderGoods.getSalesUnitPrice().subtract(refundOrderGoods.getSalesSupplyUnitPrice()));
            } else {
                refundOrderGoods.setSalesSupplyPrice(refundOrderGoods.getSalesOrderPrice());
                refundOrderGoods.setSalesVatPrice(BigDecimal.ZERO);
                refundOrderGoods.setSalesSupplyUnitPrice(refundOrderGoods.getSalesUnitPrice());
                refundOrderGoods.setSalesVatUnitPrice(BigDecimal.ZERO);
            }
        }

        this.orderGoods.add(refundOrderGoods);

        Optional<OrderDelivery> matchedOrderDelivery = findOrderDeliveryByOrderGoods(targetOrderGoods);
        addOrderDeliveryOfClaim(matchedOrderDelivery, refundOrderGoods);

        addOrderClaimIfAbsent(matchedOrderClaim.get(), refundOrderGoods);
    }

    private Optional<OrderGoods> findCancelableOrderGoods(OrderGoods cancelOrderGoods) {
        return this.getOrderGoods().stream()
                .filter(goods -> goods.getMarketOrderNo().equals(cancelOrderGoods.getMarketOrderNo())
                        && goods.getMarketOrderSubNo().equals(cancelOrderGoods.getMarketOrderSubNo()))
                .filter(OrderGoods::isCancelable)
                .findFirst();
    }

    private Optional<OrderGoods> findRefundableOrderGoods(OrderGoods cancelOrderGoods) {
        return this.getOrderGoods().stream()
                .filter(goods -> goods.getMarketOrderNo().equals(cancelOrderGoods.getMarketOrderNo())
                        && goods.getMarketOrderSubNo().equals(cancelOrderGoods.getMarketOrderSubNo()))
                .filter(goods -> !goods.isCanceled())
                .findFirst();
    }

    private Optional<OrderGoods> findFinishedOrderGoods(OrderGoods cancelOrderGoods) {
        return this.getOrderGoods().stream()
                .filter(goods -> goods.getMarketOrderNo().equals(cancelOrderGoods.getMarketOrderNo())
                        && goods.getMarketOrderSubNo().equals(cancelOrderGoods.getMarketOrderSubNo()))
                .filter(OrderGoods::isFinished)
                .findFirst();
    }

    private Optional<OrderGoods> findFinishedOrderGoodsByMarketGoodsCode(OrderGoods cancelOrderGoods) {
        return this.getOrderGoods().stream()
                .filter(goods -> goods.getMarketOrderNo().equals(cancelOrderGoods.getMarketOrderNo())
                        && goods.getMarketGoodsCode().equals(cancelOrderGoods.getMarketGoodsCode()))
                .filter(OrderGoods::isFinished)
                .findFirst();
    }

    private Optional<OrderClaim> findOrderClaimByOrderGoods(OrderGoods orderGoods, List<OrderClaim> orderClaims) {
        return orderClaims.stream()
                .filter(claim -> claim.getMarketClaimId().equals(orderGoods.getMarketOrderSubNo()))
                .findFirst();
    }

    private Optional<OrderDelivery> findOrderDeliveryByOrderGoods(OrderGoods orderGoods) {
        return this.orderDelivery.stream()
                .filter(delivery -> delivery.getOrderDetailSeq().equals(orderGoods.getOrderDetailSeq()))
                .findFirst();
    }

    private void addOrderClaimIfAbsent(OrderClaim orderClaim, OrderGoods orderGoods) {
        OrderClaimId orderClaimId = OrderClaimId.newId(orderGoods);

        boolean exists = this.orderClaim.stream()
                .anyMatch(claim -> claim.getId().equals(orderClaimId));

        if (!exists) {
            orderClaim.changeId(orderClaimId);
            this.orderClaim.add(orderClaim);
        }
    }

    private void addOrderClaimByCancelGoods(OrderGoods cancelGoods, OrderGoods giftGoods) {

        OrderClaimId cancleOrderClaimId = OrderClaimId.newId(cancelGoods);
        OrderClaimId giftOrderClaimId = OrderClaimId.newId(giftGoods);

        Optional<OrderClaim> existsOrderClaim = this.orderClaim.stream()
                .filter(claim -> claim.getId().equals(cancleOrderClaimId)).findFirst();

        if (existsOrderClaim.isPresent()) {
            OrderClaim copyOrderClaim = CustomObjectUtils.deepCopy(existsOrderClaim.get(), OrderClaim.class);
            copyOrderClaim.changeId(giftOrderClaimId);
            copyOrderClaim.setMarketClaimId(giftGoods.getMarketOrderSubNo());
            this.orderClaim.add(copyOrderClaim);
        }
    }

    private void addOrderDeliveryOfClaim(Optional<OrderDelivery> matchedOrderDelivery, OrderGoods returnOrderGoods) {

        if (matchedOrderDelivery.isEmpty()) {
            return;
        }

        OrderDelivery returnOrderDelivery = CustomObjectUtils.deepCopy(matchedOrderDelivery.get(), OrderDelivery.class);
        returnOrderDelivery.setOrderDetailSeq(returnOrderGoods.getOrderDetailSeq());
        returnOrderDelivery.setReturnReqDate(returnOrderGoods.getReturnReqDate());
        returnOrderDelivery.setStartDelvDate(null);
        returnOrderDelivery.setEndDelvDate(null);
        this.orderDelivery.add(returnOrderDelivery);
    }

    private void cancelGiftGoods() {
        // 일반 품목 리스트 추출
        List<OrderGoods> nonGiftGoods = this.orderGoods.stream()
                .filter(goods -> !"Y".equals(goods.getGiftYn()))
                .toList();

        if (nonGiftGoods.isEmpty()) {
            return;
        }
        // 증정품 업데이트 전제 조건 : 연동몰에서 품목 전체 취소가 되었을 때 증정품도 같은 상태로 업데이트 한다.

        // 일반품목 전체 취소 요청 여부 판단.
        boolean allRequestCancel = nonGiftGoods.stream()
                .allMatch(goods -> OrderStatus.OS3010.code().equals(goods.getOrderStatusCode()));
        // 일반품목 전체 취소 완료 여부 판단.
        boolean allCanceled = nonGiftGoods.stream()
                .allMatch(goods -> OrderStatus.OS3030.code().equals(goods.getOrderStatusCode()));

        OrderGoods cancelGoods = nonGiftGoods.getFirst();

        if (allRequestCancel) {
            this.orderGoods.stream()
                    .filter(goods -> "Y".equals(goods.getGiftYn()))
                    .forEach(giftGoods -> {
                                giftGoods.setPreOrderStatusCode(giftGoods.getOrderStatusCode());
                                giftGoods.setOrderStatusCode(cancelGoods.getOrderStatusCode());
                                giftGoods.setUpdateId(EXTERNAL_USER_ID);
                            }
                    );
        }

        if (allCanceled) {
            this.orderGoods.stream()
                    .filter(goods -> "Y".equals(goods.getGiftYn()))
                    .forEach(giftGoods -> {
                                giftGoods.setCancelCnt(giftGoods.getOrderCnt());
                                giftGoods.setCancelReasonMstCode(cancelGoods.getCancelReasonMstCode());
                                giftGoods.setCancelReasonDetailCode(cancelGoods.getCancelReasonDetailCode());
                                giftGoods.setCancelReasonContent(cancelGoods.getCancelReasonContent());
                                giftGoods.setPreOrderStatusCode(giftGoods.getOrderStatusCode());
                                giftGoods.setOrderStatusCode(cancelGoods.getOrderStatusCode());
                                giftGoods.setSalesOrderPrice(BigDecimal.ZERO);
                                giftGoods.setSalesSupplyPrice(BigDecimal.ZERO);
                                giftGoods.setSalesVatPrice(BigDecimal.ZERO);
                                giftGoods.setDcCost(BigDecimal.ZERO);
                                giftGoods.setUpdateId(EXTERNAL_USER_ID);

                                addOrderClaimByCancelGoods(cancelGoods, giftGoods);
                            }
                    );
        }
    }

    public enum OrderType {
        NORMAL, SUBSCRIPTION
    }
}
