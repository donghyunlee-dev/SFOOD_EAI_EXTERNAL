package com.sfood.eai.external.service.impl;

import com.sfood.eai.external.component.document.XmlDocument;
import com.sfood.eai.external.component.document.sabang.SabangDocument;
import com.sfood.eai.external.component.document.sabang.data.SabangInvRegister;
import com.sfood.eai.external.component.document.sabang.data.SabangOrderList;
import com.sfood.eai.external.component.http.SabangHttpClient;
import com.sfood.eai.external.component.oci.StorageManager;
import com.sfood.eai.external.config.MallConfiguration;
import com.sfood.eai.external.dto.CommonDataRes;
import com.sfood.eai.external.dto.order.MallReprocessOrderReq;
import com.sfood.eai.external.dto.order.OrderInvoiceReq;
import com.sfood.eai.external.dto.sabang.*;
import com.sfood.eai.external.entity.id.sabang.SabangOrderId;
import com.sfood.eai.external.entity.sabang.SabangClaim;
import com.sfood.eai.external.entity.sabang.SabangDataEntity;
import com.sfood.eai.external.entity.sabang.SabangInvoiceLog;
import com.sfood.eai.external.entity.sabang.SabangOrder;
import com.sfood.eai.external.mapper.sabang.SabangClaimMapper;
import com.sfood.eai.external.mapper.sabang.SabangOrderMapper;
import com.sfood.eai.external.repository.sabang.SabangClaimRepository;
import com.sfood.eai.external.repository.sabang.SabangInvoiceLogRepository;
import com.sfood.eai.external.repository.sabang.SabangOrderRepository;
import com.sfood.eai.external.service.MarketMallService;
import com.sfood.eai.external.service.OrderService;
import com.sfood.eai.external.service.SabangOrderService;
import com.sfood.eai.external.type.MallOrderType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SabangOrderServiceImpl implements SabangOrderService {

    private final StorageManager storageManager;
    private final SabangHttpClient sabangHttpClient;
    private final OrderService orderService;
    private final MarketMallService mallService;
    private final SabangOrderRepository sabangOrderRepository;
    private final SabangClaimRepository sabangClaimRepository;
    private final SabangInvoiceLogRepository sabangInvoiceLogRepository;

    @Override
    public List<SabangOrderData> retrieveSabangOrder(MallConfiguration configuration, SabangOrderSearchReq orderSearch) throws Exception {

        String mallIdAt = orderSearch.getMallId() + "_" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());

        try {
            // 파트너별 주문 조회 시 전달할 Document 객체 생성
            XmlDocument searchDoc = new SabangDocument<>(new SabangOrderList(configuration, orderSearch));
            // 파트너별 XML 파일 생성
            storageManager.uploadXmlFile(searchDoc, configuration.getStorageDirPath(), MessageFormat.format(configuration.getXmlFileName(), mallIdAt));
            // 파트너별 사방넷 API 호출
            List<SabangOrderData> responseData = callOrderAPI(configuration, storageManager.getFilePublicUrl(MessageFormat.format(configuration.getStorageFileName(), mallIdAt)));

            printOrderLog(orderSearch, configuration, responseData);

            return responseData;

        } catch (Exception e) {
            throw e;
        } finally {
            storageManager.deleteXmlFile(configuration.getStorageDirPath(), MessageFormat.format(configuration.getXmlFileName(), mallIdAt));
        }
    }

    @Transactional
    @Override
    public List<SabangOrder> saveSabangOrder(MallConfiguration configuration, SabangOrderSearchReq orderSearch, List<SabangOrderData> sabangOrders) throws Exception {
        List<SabangOrder> savedOrderData = new ArrayList<>();

        for (SabangOrderData orderData : sabangOrders) {
            savedOrderData.add(SabangOrderMapper.INSTANCE.toEntity(configuration.getCondition(), orderData, orderSearch.getMallId()));
        }

        return sabangOrderRepository.saveAll(savedOrderData);
    }

    @Override
    public List<SabangInvoiceResult> sendInvoice(OrderInvoiceReq orderInvoiceReq, List<SabangInvRegister.Data> dataModels) throws Exception {

        MallConfiguration configuration = mallService.getConfiguration(orderInvoiceReq);

        try {
            // 파트너별 주문 조회 시 전달할 Document 객체 생성
            XmlDocument searchDoc = new SabangDocument<>(new SabangInvRegister(configuration, dataModels));
            // XML 파일 생성
            storageManager.uploadXmlFile(searchDoc, configuration.getStorageDirPath(), configuration.getXmlFileName());

            List<SabangInvoiceResult> invoiceResult = callInvoiceAPI(configuration, storageManager.getFilePublicUrl(configuration.getStorageFileName()));

            return invoiceResult;

        } catch (Exception e) {
            throw e;
        } finally {
            storageManager.deleteXmlFile(configuration.getStorageDirPath(), configuration.getXmlFileName());
        }
    }

    @Transactional
    @Override
    public void saveSabangInvoiceLog(List<SabangInvoiceLog> invoiceLogs, List<SabangInvoiceResult> invoiceResult) {
        invoiceLogs.forEach(invoice -> {
            SabangInvoiceResult resultData = invoiceResult.stream()
                    .filter(sir -> sir.getSabangnetIdx().equals(invoice.getIdx()))
                    .findFirst()
                    .orElse(new SabangInvoiceResult("ERROR", "결과를 받지 못했습니다!"));

            invoice.setResult(resultData.getResult());
            invoice.setMessage(resultData.getResultMsg());
        });

        sabangInvoiceLogRepository.saveAll(invoiceLogs);
    }

    @SuppressWarnings("unchecked")
    @Override
    public CommonDataRes reprocessOrder(MallReprocessOrderReq reprocessOrderReq) {

        SabangOrderId id = new SabangOrderId(
                reprocessOrderReq.getCompanyCode()
                , reprocessOrderReq.getMallCode()
                , reprocessOrderReq.getOrderNo());

        SabangOrderSearchReq orderSearchReq = new SabangOrderSearchReq();
        orderSearchReq.setCompanyCode(reprocessOrderReq.getCompanyCode());
        orderSearchReq.setChannelCode(reprocessOrderReq.getChannelCode());
        orderSearchReq.setMallCode(reprocessOrderReq.getMallCode());
        orderSearchReq.setOrderNo(reprocessOrderReq.getOrderNo());

        SabangDataEntity sabangDataEntity = getSabangOrder(orderSearchReq, id);

        if (ObjectUtils.isEmpty(sabangDataEntity)) {
            return CommonDataRes.noDataResponse();
        }

        if (orderService.existsOrder(reprocessOrderReq.getCompanyCode(), reprocessOrderReq.getMallCode(), reprocessOrderReq.getOrderNo())) {
            orderService.updateOrderOne(MallOrderType.fromByCode(reprocessOrderReq.getCompanyCode(), reprocessOrderReq.getMallCode()).channelType(), sabangDataEntity);
        } else {
            orderService.saveOrderOne(MallOrderType.fromByCode(reprocessOrderReq.getCompanyCode(),reprocessOrderReq.getMallCode()).channelType(), sabangDataEntity);
        }

        return CommonDataRes.getDataResponse(1);
    }

    private SabangDataEntity getSabangOrder(SabangOrderSearchReq reprocessOrderReq, SabangOrderId id) {
        List<SabangOrder> savedOrderData = sabangOrderRepository.findAllByOrderId(id.getIdx());

        if (CollectionUtils.isEmpty(savedOrderData)) {
            return null;
        }

        SabangDataEntity sabangDataEntity = SabangDataEntity.builder()
                .condition(reprocessOrderReq)
                .orders(savedOrderData)
                .build();

        return sabangDataEntity;
    }

    private List<SabangClaim> saveCollectedClaimData(final MallConfiguration config, final List<SabangClaimData> sabangClaimData) {
        List<SabangClaim> savedClaimData = new ArrayList<>();

        for (SabangClaimData claimData : sabangClaimData) {
            savedClaimData.add(SabangClaimMapper.INSTANCE.toEntity(config.getCondition(), claimData));
        }

        return sabangClaimRepository.saveAll(savedClaimData);
    }

    private List<SabangOrderData> callOrderAPI(final MallConfiguration config, final String requestFileName) throws Exception {
        SabangOrderRes response = (SabangOrderRes) sabangHttpClient.retrieveOrder(config, requestFileName).getBody();
        return (List<SabangOrderData>) CollectionUtils.emptyIfNull(response.getSabangOrderList());
    }

    private List<SabangClaimData> callClaimAPI(final MallConfiguration config, final String requestFileName) throws Exception {
        SabangClaimRes response = (SabangClaimRes) sabangHttpClient.retrieveClaim(config, requestFileName).getBody();
        return (List<SabangClaimData>) CollectionUtils.emptyIfNull(response.getSabangOrderList());
    }

    private List<SabangInvoiceResult> callInvoiceAPI(final MallConfiguration config, final String requestFileName) throws Exception {
        SabangInvoiceRes response = (SabangInvoiceRes) sabangHttpClient.sendInvoice(config, requestFileName).getBody();
        return (List<SabangInvoiceResult>) CollectionUtils.emptyIfNull(response.getSabangInvoiceResultList());
    }

    private static void printOrderLog(SabangOrderSearchReq orderSearch, MallConfiguration configuration, List<SabangOrderData> responseData) {
        log.info("주문 - 연동몰코드 : {}, 수집일 : {}, 주문 수 : {}", orderSearch.getMallId(), configuration.getCondition().getCollectDate(), responseData.size());
    }

    private static void printClaimLog(MallConfiguration configuration, List<SabangClaimData> responseData) {
        log.info("클레임 수집일 : {}, 클레임 수 : {}", configuration.getCondition().getCollectDate(), responseData.size());
    }
}
