package com.sfood.eai.external.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfood.eai.external.component.document.XmlDocument;
import com.sfood.eai.external.component.document.godo.GodoDocument;
import com.sfood.eai.external.component.document.godo.data.GodoOrderStatusData;
import com.sfood.eai.external.component.http.GodoHttpClient;
import com.sfood.eai.external.component.oci.StorageManager;
import com.sfood.eai.external.config.MallConfiguration;
import com.sfood.eai.external.dto.CommonDataRes;
import com.sfood.eai.external.dto.CommonSearchReq;
import com.sfood.eai.external.dto.godo.*;
import com.sfood.eai.external.dto.order.*;
import com.sfood.eai.external.entity.godo.*;
import com.sfood.eai.external.entity.id.godo.GodoOrderId;
import com.sfood.eai.external.entity.id.order.OrderGoodsId;
import com.sfood.eai.external.exception.APIException;
import com.sfood.eai.external.mapper.godo.*;
import com.sfood.eai.external.repository.godo.*;
import com.sfood.eai.external.service.GodoOrderService;
import com.sfood.eai.external.service.MarketMallService;
import com.sfood.eai.external.service.OrderService;
import com.sfood.eai.external.type.ChannelType;
import com.sfood.eai.external.type.godo.GodoClaimCode;
import com.sfood.eai.external.type.godo.GodoClaimPayment;
import com.sfood.eai.external.type.godo.GodoOrderStatus;
import com.sfood.eai.external.util.CustomDateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.webjars.NotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class GodoOrderServiceImpl implements GodoOrderService {
    private static final String LOTTE_DELIVERY_ID = "18"; // 롯데 택배 고정 값
    private final MarketMallService mallService;
    private final StorageManager storageManager;
    private final GodoHttpClient godoHttpClient;
    private final OrderService orderService;

    private final GodoOrderRepository godoOrderRepository;
    private final GodoOrderInfoRepository godoOrderInfoRepository;
    private final GodoOrderDeliveryRepository godoOrderDeliveryRepository;
    private final GodoOrderGoodsRepository godoOrderGoodsRepository;
    private final GodoOrderClaimRepository godoOrderClaimRepository;
    private final GodoAddGoodsRepository godoAddGoodsRepository;
    private final GodoOrderGoodsOptionRepository godoOrderGoodsOptionRepository;
    private final GodoOrderStatusLogRepository godoOrderStatusLogRepository;
    private final GodoGiftRepository godoGiftRepository;

    @Override
    public List<GodoOrderData> retrieveGodoOrders(GodoOrderSearchReq orderSearch) throws Exception {
        List<GodoOrderData> godoOrderDataList = callOrderAPI(orderSearch);
        printLog(orderSearch, godoOrderDataList);
        return godoOrderDataList;
    }

    @Override
    public List<GodoOrderData> retrieveGodoClaims(GodoOrderSearchReq orderSearch) throws Exception {
        List<GodoOrderData> godoOrderClaimDataList = callClaimAPI(orderSearch);
        printLog(orderSearch, godoOrderClaimDataList);
        return godoOrderClaimDataList;
    }

    @Override
    public Map<String, String> retrieveOrderReceipts(GodoOrderSearchReq orderSearch) throws APIException {
        List<GodoReceiptRes.ReceiptData> godoOrderReceiptDataList = callReceiptAPI(orderSearch);
        Map<String, String> receiptMap = godoOrderReceiptDataList.stream()
                .collect(Collectors.toMap(GodoReceiptRes.ReceiptData::getOrderNo, GodoReceiptRes.ReceiptData::getReceiptFl, (a, b) -> a));
        return receiptMap;
    }

    @Transactional
    @Override
    public GodoDataEntity saveGodoOrder(GodoOrderSearchReq orderSearch, GodoOrderData godoData) {

        GodoOrder godoOrder = GodoOrderMapper.INSTANCE.toEntity(orderSearch, godoData);

        List<GodoOrderInfo> godoOrderInfoList = godoData.getGodoOrderInfoList().stream()
                .map(orderInfo -> GodoOrderInfoMapper.INSTANCE.toEntity(godoOrder, orderInfo))
                .toList();

        List<GodoOrderDelivery> godoOrderDeliveryList = godoData.getGodoOrderDeliveryList().stream()
                .map(orderDelivery -> GodoOrderDeliveryMapper.INSTANCE.toEntity(godoOrder, orderDelivery))
                .toList();

        List<GodoOrderGoods> godoOrderGoodsList = new ArrayList<>();
        List<GodoOrderClaim> godoOrderClaimList = new ArrayList<>();
        List<GodoOrderGoodsOption> godoOrderGoodsOptionList = new ArrayList<>();

        if (CollectionUtils.isEmpty(godoData.getGodoOrderGoodsList())) {
            // 메디쏠라 냉동 주문의 배송중인 주문을 수집하는 경우 오류가 발생함.
            // 상세 냉동 품목은 송장번호를 전송하지 않아서 상태가 결제완료일 때 주문은 조회되고 GodoOrderGoods는 제외되서 오류 발생함.
            // 이미 배송 진행 중인 주문이라 return 시킴
            // log.error("message : GodoOrderData.getGodoOrderGoodsList() is null, orderNo : {}", godoOrder.getOrderNo());
            return null;
        }

        for (GodoOrderGoodsData orderGoods : godoData.getGodoOrderGoodsList()) {
            godoOrderGoodsList.add(GodoOrderGoodsMapper.INSTANCE.toEntity(godoOrder, orderGoods));

            if (ObjectUtils.isNotEmpty(orderGoods.getOptionSno()) && orderGoods.getOptionSno() > 0) {
                List<List<Object>> optionList = convertOptionToObject(orderGoods.getOptionInfo());

                if (CollectionUtils.isNotEmpty(optionList)) {
                    for (List<Object> option : optionList) {
                        godoOrderGoodsOptionList.add(GodoOrderGoodsOption.toEntity(godoOrder, orderGoods, option));
                    }
                }
            }

            if (!ObjectUtils.isEmpty(orderGoods.getClaimData())) {
                godoOrderClaimList.add(GodoOrderClaimMapper.INSTANCE.toEntity(godoOrder, orderGoods, orderGoods.getClaimData()));
            }
        }

        adjustGoodsDcPrice(godoOrder, godoOrderGoodsList);

        List<GodoAddGoods> godoAddGoodsList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(godoData.getGodoAddGoodsList())) {
            godoData.getGodoAddGoodsList().forEach(addGoods -> godoAddGoodsList.add(GodoAddGoodsMapper.INSTANCE.toEntity(godoOrder, addGoods)));
        }

        List<GodoGift> godoGiftList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(godoData.getGodoGiftList())) {
            godoData.getGodoGiftList().forEach(godoGift -> godoGiftList.add(GodoGiftMapper.INSTANCE.toEntity(godoOrder, godoGift)));
        }

        return GodoDataEntity.builder()
                .companyCode(orderSearch.getCompanyCode())
                .mallCode(orderSearch.getMallCode())
                .channelType(ChannelType.of(orderSearch.getChannelCode()))
                .collectDate(orderSearch.getCollectDate())
                .order(godoOrderRepository.save(godoOrder))
                .info(godoOrderInfoRepository.saveAll(godoOrderInfoList))
                .delivery(godoOrderDeliveryRepository.saveAll(godoOrderDeliveryList))
                .goods(godoOrderGoodsRepository.saveAll(godoOrderGoodsList))
                .option(godoOrderGoodsOptionRepository.saveAll(godoOrderGoodsOptionList))
                .claim(godoOrderClaimRepository.saveAll(godoOrderClaimList))
                .addGoods(godoAddGoodsRepository.saveAll(godoAddGoodsList))
                .gift(godoGiftRepository.saveAll(godoGiftList))
                .build();
    }

    @Override
    public GodoOrderStatusRes sendInvoice(CommonSearchReq searchReq, OrderInvoiceReq.OrderInvoiceInfo invoice) throws APIException {
        GodoOrderStatusReq orderStatusReq = GodoOrderStatusReq.builder()
                .companyCode(searchReq.getCompanyCode())
                .channelCode(ChannelType.GODOMALL.code())
                .mallCode(searchReq.getMallCode())
                .requestUri(searchReq.getRequestUri())
                .orderNo(invoice.getMarketOrderNo())
                .sno(invoice.getMarketOrderSubNo())
                .invoiceNo(invoice.getInvoiceNo())
                .invoiceCompanySno(LOTTE_DELIVERY_ID)
                .orderStatus(GodoOrderStatus.D1.code()) // 송장번호 송신 시 D1 배송중으로 고정
                .build();

        GodoOrderStatusRes res = callOrderInvoiceAPI(orderStatusReq);
        return res;
    }

    @Override
    public GodoOrderStatusRes sendInvoicesWithXmlFile(CommonSearchReq searchReq, List<OrderInvoiceReq.OrderInvoiceInfo> invoice) throws Exception {
        GodoOrderStatusRes godoOrderStatusRes = null;
        MallConfiguration configuration = mallService.getConfiguration(searchReq);
        String fileAt = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());

        try {
            List<GodoOrderStatusData> invoiceDataList = new ArrayList<>();

            int goodsIndex = 1;
            for (OrderInvoiceReq.OrderInvoiceInfo invoiceInfo : invoice) {
                invoiceInfo.setIdx(String.valueOf(goodsIndex));
                invoiceDataList.add(GodoOrderInvoiceMapper.INSTANCE.toDataModel(invoiceInfo));
                goodsIndex++;
            }

            if (CollectionUtils.isEmpty(invoiceDataList)) {
                return godoOrderStatusRes;
            }

            XmlDocument invoiceDoc = new GodoDocument<>(invoiceDataList);

            storageManager.uploadXmlFile(invoiceDoc, configuration.getStorageDirPath(), MessageFormat.format(configuration.getXmlFileName(), fileAt));

            godoOrderStatusRes = callOrderInvoiceAPI(configuration, storageManager.getFilePublicUrl(MessageFormat.format(configuration.getStorageFileName(), fileAt)));

        } catch (Exception e) {
            throw e;
        } finally {
            storageManager.deleteXmlFile(configuration.getStorageDirPath(), MessageFormat.format(configuration.getXmlFileName(), fileAt));
        }

        return godoOrderStatusRes;
    }

    @Override
    public GodoOrderStatusRes sendOrderStatus(CommonSearchReq searchReq, OrderStatusReq.OrderStatusInfo orderStatusInfo, GodoOrderStatus status) throws APIException {
        GodoOrderStatusReq orderStatusReq = GodoOrderStatusReq.builder()
                .companyCode(searchReq.getCompanyCode())
                .channelCode(ChannelType.GODOMALL.code())
                .mallCode(searchReq.getMallCode())
                .requestUri(searchReq.getRequestUri())
                .orderNo(orderStatusInfo.getMarketOrderNo())
                .sno(orderStatusInfo.getMarketOrderSubNo())
                .orderStatus(status.code())
                .build();

        return callOrderStatusAPI(orderStatusReq);
    }

    @Transactional
    @Override
    public void saveStatusResponseLog(OrderGoodsId goodsId, GodoOrderStatusRes orderStatusResponse) {

        List<GodoOrderStatusLog> statuslogList = new ArrayList<>();

        if (ObjectUtils.isNotEmpty(orderStatusResponse.getReturnData())) {

            if (ObjectUtils.isNotEmpty(orderStatusResponse.getReturnData().getSuccess())) {
                for (GodoStatusData successResponse : orderStatusResponse.getReturnData().getSuccess().getStatusData()) {
                    statuslogList.add(GodoOrderStatusLogMapper.INSTANCE.toEntity(goodsId, orderStatusResponse.getHeader(), successResponse));
                }
            }

            if (ObjectUtils.isNotEmpty(orderStatusResponse.getReturnData().getFail())) {
                for (GodoStatusData failResponse : orderStatusResponse.getReturnData().getFail().getStatusData()) {
                    statuslogList.add(GodoOrderStatusLogMapper.INSTANCE.toEntity(goodsId, orderStatusResponse.getHeader(), failResponse));
                }
            }

            for (GodoStatusData returnResponse : orderStatusResponse.getReturnData().getStatusData()) {
                statuslogList.add(GodoOrderStatusLogMapper.INSTANCE.toEntity(goodsId, orderStatusResponse.getHeader(), returnResponse));
            }
        }

        if (statuslogList.isEmpty()) {
            statuslogList.add(GodoOrderStatusLogMapper.INSTANCE.toEntity(goodsId, orderStatusResponse));
        }

        godoOrderStatusLogRepository.saveAll(statuslogList);
    }

    @Transactional
    @Override
    public void saveStatusResponseLog(CommonSearchReq searchReq, OrderInvoiceReq.MallOrderInvoice invoices, GodoOrderStatusRes orderStatusResponse) {

        List<GodoOrderStatusLog> statuslogList = new ArrayList<>();

        for (OrderInvoiceReq.OrderInvoiceInfo invoice : invoices.getOrderInvoiceList()) {

            OrderGoodsId goodsId = new OrderGoodsId(
                    searchReq.getCompanyCode()
                    , searchReq.getMallCode()
                    , invoice.getOrderMergeNo()
                    , invoice.getOrderDetailSeq());

            // 응답 데이터가 없는 경우 기본 로그만 저장
            if (ObjectUtils.isEmpty(orderStatusResponse.getReturnData())) {
                statuslogList.add(GodoOrderStatusLogMapper.INSTANCE.toEntity(goodsId, orderStatusResponse));
                continue;
            }

            if (ObjectUtils.isNotEmpty(orderStatusResponse.getReturnData().getSuccess())) {
                orderStatusResponse.getReturnData().getSuccess().getStatusData().stream()
                        .filter(result -> result.getOrderNo().equals(invoice.getMarketOrderNo()) &&
                                String.valueOf(result.getSno()).equals(invoice.getMarketOrderSubNo()))
                        .findFirst()
                        .map(statusData -> GodoOrderStatusLogMapper.INSTANCE.toEntity(goodsId, orderStatusResponse.getHeader(), statusData))
                        .ifPresent(statuslogList::add);
            }

            if (ObjectUtils.isNotEmpty(orderStatusResponse.getReturnData().getFail())) {
                orderStatusResponse.getReturnData().getFail().getStatusData().stream()
                        .filter(result -> result.getOrderNo().equals(invoice.getMarketOrderNo()) &&
                                String.valueOf(result.getSno()).equals(invoice.getMarketOrderSubNo()))
                        .findFirst()
                        .map(statusData -> GodoOrderStatusLogMapper.INSTANCE.toEntity(goodsId, orderStatusResponse.getHeader(), statusData))
                        .ifPresent(statuslogList::add);
            }
        }

        godoOrderStatusLogRepository.saveAll(statuslogList);
    }

    @Override
    public CommonDataRes sendOrderClaim(OrderClaimReq orderClaim) throws Exception {

        OrderGoodsId orderGoodsId = new OrderGoodsId(orderClaim.getCompanyCode(), orderClaim.getMallCode(), orderClaim.getOrderMergeNo(), orderClaim.getOrderDetailSeq());

        SimpleOrderGoods orderGoods = orderService.getSimpleOrderGoods(orderGoodsId);

        if (ObjectUtils.isEmpty(orderGoods)) {
            return CommonDataRes.noDataResponse();
        }

        GodoOrderStatusReq orderStatusReq = GodoOrderStatusReq.builder()
                .companyCode(orderClaim.getCompanyCode())
                .channelCode(ChannelType.GODOMALL.code())
                .mallCode(orderClaim.getMallCode())
                .requestUri(orderClaim.getRequestUri())
                .orderNo(orderGoods.getMarketOrderNo())
                .sno(orderGoods.getMarketOrderSubNo())
                .orderStatus(GodoOrderStatus.fromValue(orderClaim.getClaimStatusCode()).code())
                .handleReason(GodoClaimCode.C04001014.code())
                .returnMethod(GodoClaimPayment.C04003004.code())
                .handleDetailReason(orderClaim.getClaimReasonContent())
                .build();

        GodoOrderStatusRes orderStatusResponse = callOrderClaimAPI(orderStatusReq);

        saveStatusResponseLog(orderGoodsId, orderStatusResponse);

        return CommonDataRes.getDataResponse(orderStatusResponse.getHeader().getCode().equals("000") ? 1 : 0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public CommonDataRes reCreateOrder(MallReprocessOrderReq reprocessOrderReq) {

        GodoOrderId id = new GodoOrderId(
                reprocessOrderReq.getCompanyCode()
                , reprocessOrderReq.getChannelCode()
                , reprocessOrderReq.getMallCode()
                , reprocessOrderReq.getOrderNo());

        GodoDataEntity godoData = getGodoOrder(reprocessOrderReq, id);

        if (!orderService.existsOrder(godoData.getCompanyCode(), godoData.getMallCode(), godoData.getGodoOrderNo())) {
            if (ObjectUtils.isEmpty(godoData.getOption())) {
                orderService.saveOrderOne(godoData.getChannelType(), godoData);
            } else {
                orderService.saveSubscriptionOrder(godoData.getChannelType(), godoData);
            }
        }

        return CommonDataRes.getDataResponse(1);
    }

    @Override
    public List<MallOrderId> retrieveUnSettledOrderIds(GodoOrderSearchReq searchReq) {
        return godoOrderRepository.findUnsettledOrderIds(searchReq);
    }

    private GodoDataEntity getGodoOrder(MallReprocessOrderReq reprocessOrderReq, GodoOrderId id) {

        GodoOrder order = godoOrderRepository.findById(id).orElseThrow(() -> new NotFoundException("GodoMall order not found"));
        List<GodoOrderInfo> orderInfos = godoOrderInfoRepository.findByOrderNo(id.getOrderNo());
        List<GodoOrderDelivery> orderDeliveries = godoOrderDeliveryRepository.findByOrderNo(id.getOrderNo());
        List<GodoOrderGoods> orderGoods = godoOrderGoodsRepository.findByOrderNo(id.getOrderNo());
        List<GodoOrderGoodsOption> orderGoodsOptions = godoOrderGoodsOptionRepository.findByOrderNo(id.getOrderNo());
        List<GodoAddGoods> addGoods = godoAddGoodsRepository.findByOrderNo(id.getOrderNo());
        addGoods.forEach(goods -> goods.setAssignGoodsCnt(goods.getGoodsCnt()));
        List<GodoOrderClaim> claims = godoOrderClaimRepository.findByOrderNo(id.getOrderNo());
        List<GodoGift> gifts = godoGiftRepository.findByOrderNo(id.getOrderNo());

        GodoDataEntity godoData = GodoDataEntity.builder()
                .companyCode(reprocessOrderReq.getCompanyCode())
                .mallCode(reprocessOrderReq.getMallCode())
                .channelType(ChannelType.of(reprocessOrderReq.getChannelCode()))
                .collectDate(CustomDateUtils.formatDateTimeString(order.getOrderDate(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"))
                .order(order)
                .info(orderInfos)
                .delivery(orderDeliveries)
                .goods(orderGoods)
                .option(orderGoodsOptions)
                .claim(claims)
                .addGoods(addGoods)
                .gift(gifts)
                .build();

        return godoData;
    }


    private void adjustGoodsDcPrice(GodoOrder godoOrder, List<GodoOrderGoods> godoOrderGoodsList) {
        /*
        - divisionCouponOrderDcPrice 주문할인 안분된 쿠폰할인
          GOODS_DC_PRICE 적용 후 계산
            -> 문제 원인
            couponGoodsDcPrice 가 적용되기 전에 주문할인이 먼저 계산됨

            TOTAL_COUPON_ORDER_DC_PRICE > GOODS_DC_PRICE

           상품쿠폰할인이 적용된 판매금액 비중으로 주문할인을 안분해야 함.
            ((GOODS_PRICE * GOODS_CNT) / TOTAL_GOODS_PRICE) * TOTAL_COUPON_ORDER_DC_PRICE
            -> 수정 방법
               상품할인을 먼저 다 적용하고 비중을 구한다.
            (((GOODS_PRICE * GOODS_CNT) - COUPON_GOODS_DC_PRICE) / TOTAL_GOODS_PRICE) * TOTAL_COUPON_ORDER_DC_PRICE
        */
        List<GodoOrderGoods> normalGoodslist = godoOrderGoodsList
                .stream()
                .filter(goods -> GodoOrderStatus.O1.code().equals(goods.getOrderStatus()) || GodoOrderStatus.P1.code().equals(goods.getOrderStatus()))
                .toList();

        if (CollectionUtils.isEmpty(normalGoodslist)) {
            return;
        }

        if (godoOrder.getTotalCouponGoodsDcPrice().compareTo(BigDecimal.ZERO) > 0) {
            for (GodoOrderGoods godoOrderGoods : normalGoodslist) {
                BigDecimal salesGoodsPrice = (godoOrderGoods.getGoodsPrice().add(godoOrderGoods.getOptionPrice()))
                        .multiply(BigDecimal.valueOf(godoOrderGoods.getGoodsCnt()))
                        .subtract(godoOrderGoods.getCouponGoodsDcPrice());
                BigDecimal calcCouponOrderDcPrice = salesGoodsPrice
                        .divide(godoOrder.getTotalGoodsPrice().subtract(godoOrder.getTotalCouponGoodsDcPrice()), 10, RoundingMode.HALF_UP)
                        .multiply(godoOrder.getTotalCouponOrderDcPrice()).setScale(0, RoundingMode.HALF_UP);

                if (calcCouponOrderDcPrice.compareTo(BigDecimal.ZERO) > 0) {
                    godoOrderGoods.setDivisionCouponOrderDcPrice(calcCouponOrderDcPrice);
                } else {
                    godoOrderGoods.setDivisionCouponOrderDcPrice(BigDecimal.ZERO);
                }
            }

            BigDecimal totalCouponOrderDcPrice = normalGoodslist.stream().map(GodoOrderGoods::getDivisionCouponOrderDcPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            normalGoodslist.getLast().setDivisionCouponOrderDcPrice(normalGoodslist.getLast().getDivisionCouponOrderDcPrice().add(godoOrder.getTotalCouponOrderDcPrice().subtract(totalCouponOrderDcPrice)));
        }

        if (godoOrder.getUseMileage().compareTo(BigDecimal.ZERO) > 0) {
            for (GodoOrderGoods godoOrderGoods : normalGoodslist) {
                BigDecimal salesGoodsPrice = (godoOrderGoods.getGoodsPrice().add(godoOrderGoods.getOptionPrice()))
                        .multiply(BigDecimal.valueOf(godoOrderGoods.getGoodsCnt()))
                        .subtract(godoOrderGoods.getGoodsDcPrice())
                        .subtract(godoOrderGoods.getMemberDcPrice())
                        .subtract(godoOrderGoods.getCouponGoodsDcPrice())
                        .subtract(godoOrderGoods.getDivisionCouponOrderDcPrice());

                BigDecimal calcUseMileage = salesGoodsPrice
                        .divide(godoOrder.getTotalGoodsPrice()
                                .subtract(godoOrder.getTotalGoodsDcPrice())
                                .subtract(godoOrder.getTotalMemberDcPrice())
                                .subtract(godoOrder.getTotalCouponGoodsDcPrice())
                                .subtract(godoOrder.getTotalCouponOrderDcPrice()), 10, RoundingMode.HALF_UP)
                        .multiply(godoOrder.getUseMileage()).setScale(0, RoundingMode.HALF_UP);

                if (calcUseMileage.compareTo(BigDecimal.ZERO) > 0) {
                    godoOrderGoods.setDivisionUseMileage(calcUseMileage.subtract(godoOrderGoods.getDivisionGoodsDeliveryUseMileage()));
                } else {
                    godoOrderGoods.setDivisionUseMileage(BigDecimal.ZERO);
                }
            }
        }

        BigDecimal totalUseMileage = normalGoodslist.stream().map(GodoOrderGoods::getDivisionUseMileage).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalDivisionDeliveryUseMileage = normalGoodslist.stream().map(GodoOrderGoods::getDivisionGoodsDeliveryUseMileage).reduce(BigDecimal.ZERO, BigDecimal::add);
        normalGoodslist.getLast().setDivisionUseMileage(normalGoodslist.getLast().getDivisionUseMileage().add(godoOrder.getUseMileage().subtract(totalUseMileage).subtract(totalDivisionDeliveryUseMileage)));
    }

    private List<GodoOrderData> callOrderAPI(final GodoOrderSearchReq orderSearch) throws APIException {
        GodoOrderRes godoOrderRes = godoHttpClient.retrieveOrder(mallService.getConfiguration(orderSearch)).getBody();
        return (List<GodoOrderData>) CollectionUtils.emptyIfNull(godoOrderRes.getGodoReturnOrder().getOrderDataList());
    }

    private List<GodoOrderData> callClaimAPI(final GodoOrderSearchReq orderSearch) throws APIException {
        GodoOrderRes godoOrderRes = godoHttpClient.retrieveClaim(mallService.getConfiguration(orderSearch)).getBody();
        return (List<GodoOrderData>) CollectionUtils.emptyIfNull(godoOrderRes.getGodoReturnOrder().getOrderDataList());
    }

    private List<GodoReceiptRes.ReceiptData> callReceiptAPI(final GodoOrderSearchReq orderSearch) throws APIException {
        MallConfiguration configuration = MallConfiguration.builder().condition(orderSearch).build();
        GodoReceiptRes godoReceiptRes = godoHttpClient.retrieveReceipt(configuration).getBody();
        return (List<GodoReceiptRes.ReceiptData>) CollectionUtils.emptyIfNull(godoReceiptRes.getData());
    }

    private GodoOrderStatusRes callOrderStatusAPI(GodoOrderStatusReq orderStatus) throws APIException {
        return godoHttpClient.sendOrderStatus(mallService.getConfiguration(orderStatus)).getBody();
    }

    private GodoOrderStatusRes callOrderInvoiceAPI(GodoOrderStatusReq orderStatus) throws APIException {
        return godoHttpClient.sendOrderInvoice(mallService.getConfiguration(orderStatus)).getBody();
    }

    private GodoOrderStatusRes callOrderInvoiceAPI(final MallConfiguration mallConfiguration, final String requestFileName) throws APIException {
        return godoHttpClient.sendOrderInvoice(mallConfiguration, requestFileName).getBody();
    }

    private GodoOrderStatusRes callOrderClaimAPI(GodoOrderStatusReq orderStatus) throws APIException {
        return godoHttpClient.sendOrderClaim(mallService.getConfiguration(orderStatus)).getBody();
    }

    private List<List<Object>> convertOptionToObject(String optionInfo) {

        if (!StringUtils.hasText(optionInfo)) {
            return null;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(optionInfo, new TypeReference<>() {
            });

        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private static void printLog(GodoOrderSearchReq orderSearch, List<GodoOrderData> godoOrderDataList) {
        log.info("연동몰코드 : {}, 주문일 : {}, 주문 수 : {}, 상태 : {}", orderSearch.getMallCode(), orderSearch.getCollectDate(), godoOrderDataList.size(), orderSearch.getOrderStatus());
    }
}
