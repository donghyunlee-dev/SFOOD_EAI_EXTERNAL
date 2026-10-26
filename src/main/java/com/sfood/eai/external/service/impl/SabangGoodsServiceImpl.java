package com.sfood.eai.external.service.impl;

import com.sfood.eai.external.component.document.XmlDocument;
import com.sfood.eai.external.component.document.sabang.SabangDocument;
import com.sfood.eai.external.component.document.sabang.data.SabangGoodsLiteRegister;
import com.sfood.eai.external.component.document.sabang.data.SabangGoodsRegister;
import com.sfood.eai.external.component.http.SabangHttpClient;
import com.sfood.eai.external.component.oci.StorageManager;
import com.sfood.eai.external.config.AppConfig;
import com.sfood.eai.external.config.MallConfiguration;
import com.sfood.eai.external.dto.CommonDataRes;
import com.sfood.eai.external.dto.CommonSearchReq;
import com.sfood.eai.external.dto.sabang.SabangGoodsLiteReq;
import com.sfood.eai.external.dto.sabang.SabangGoodsReq;
import com.sfood.eai.external.dto.sabang.SabangGoodsRes;
import com.sfood.eai.external.dto.sabang.SabangGoodsResult;
import com.sfood.eai.external.entity.id.sabang.SabangGoodsId;
import com.sfood.eai.external.entity.sabang.SabangGoodsLog;
import com.sfood.eai.external.entity.sabang.SabangGoodsTransLog;
import com.sfood.eai.external.mapper.sabang.SabangGoodsLogMapper;
import com.sfood.eai.external.mapper.sabang.SabangGoodsMapper;
import com.sfood.eai.external.mapper.sabang.SabangGoodsTransLogMapper;
import com.sfood.eai.external.repository.sabang.SabangGoodsLogRepository;
import com.sfood.eai.external.repository.sabang.SabangGoodsRepository;
import com.sfood.eai.external.repository.sabang.SabangGoodsTransLogRepository;
import com.sfood.eai.external.service.MarketMallService;
import com.sfood.eai.external.service.SabangGoodsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SabangGoodsServiceImpl implements SabangGoodsService {
    private static final String RESULT_MESSAGE_SUCCESS = "SUCCESS";
    private static final String RESULT_MESSAGE_FAIL = "FAIL";

    private final StorageManager storageManager;
    private final SabangHttpClient sabangHttpClient;
    private final MarketMallService mallService;
    private final SabangGoodsRepository sabangGoodsRepository;
    private final SabangGoodsLogRepository sabangGoodsLogRepository;
    private final SabangGoodsTransLogRepository sabangGoodsTransLogRepository;

    @Transactional
    @Override
    public CommonDataRes sendGoodsInfo(SabangGoodsReq goodsRequest) throws Exception {

        if ("dev".equals(AppConfig.ACTIVE_PROFILE)) {
            return CommonDataRes.noDataResponse();
        }

        CommonDataRes res = new CommonDataRes();
        MallConfiguration configuration = mallService.getConfiguration(goodsRequest);

        try {
            List<SabangGoodsRegister.Data> goodsData = createGoodsData(goodsRequest);

            if (CollectionUtils.isEmpty(goodsData)) {
                res.setSuccessCnt(0);
                return res;
            }

            XmlDocument goodsDoc = new SabangDocument(new SabangGoodsRegister(configuration, goodsData));

            storageManager.uploadXmlFile(goodsDoc, configuration.getStorageDirPath(), configuration.getXmlFileName());

            List<SabangGoodsResult> responseResult = callGoodsAPI(configuration, storageManager.getFilePublicUrl(configuration.getStorageFileName()));

            saveLog(goodsRequest, responseResult);

            res.setSuccessCnt(getSuccessResultCount(responseResult));

        } catch (Exception e) {
            throw e;
        } finally {
            storageManager.deleteXmlFile(configuration.getStorageDirPath(), configuration.getXmlFileName());
        }

        return res;
    }

    @Transactional
    @Override
    public CommonDataRes sendGoodsLiteInfo(SabangGoodsLiteReq goodsRequest) throws Exception {

        if ("dev".equals(AppConfig.ACTIVE_PROFILE)) {
            return CommonDataRes.noDataResponse();
        }

        CommonDataRes res = new CommonDataRes();
        MallConfiguration configuration = mallService.getConfiguration(goodsRequest);

        try {
            List<SabangGoodsLiteRegister.Data> goodsData = createGoodsLiteData(goodsRequest);

            if (CollectionUtils.isEmpty(goodsData)) {
                res.setSuccessCnt(0);
                return res;
            }

            XmlDocument goodsDoc = new SabangDocument(new SabangGoodsLiteRegister(configuration, goodsData));

            storageManager.uploadXmlFile(goodsDoc, configuration.getStorageDirPath(), configuration.getXmlFileName());

            List<SabangGoodsResult> responseResult = callGoodsLiteAPI(configuration, storageManager.getFilePublicUrl(configuration.getStorageFileName()));

            saveLiteLog(goodsRequest, responseResult);

            res.setSuccessCnt(getSuccessResultCount(responseResult));
        } catch (Exception e) {
            throw e;
        } finally {
            storageManager.deleteXmlFile(configuration.getStorageDirPath(), configuration.getXmlFileName());
        }

        return res;
    }

    private List<SabangGoodsRegister.Data> createGoodsData(SabangGoodsReq goodsRequest) {

        List<SabangGoodsRegister.Data> goodsData = new ArrayList<>();

        for (SabangGoodsReq.SabangGoodsInfo goodsInfo : goodsRequest.getSabangGoodsInfoList()) {
            goodsData.add(SabangGoodsMapper.INSTANCE.toDataModel(goodsInfo));
        }

        return goodsData;
    }

    private List<SabangGoodsLiteRegister.Data> createGoodsLiteData(SabangGoodsLiteReq goodsRequest) {

        List<SabangGoodsLiteRegister.Data> goodsData = new ArrayList<>();

        for (SabangGoodsLiteReq.SabangGoodsInfo goodsInfo : goodsRequest.getSabangGoodsInfoList()) {
            goodsData.add(SabangGoodsMapper.INSTANCE.toDataModel(goodsInfo));
        }
        return goodsData;
    }

    private List<SabangGoodsResult> callGoodsAPI(final MallConfiguration config, final String requestFileName) throws Exception {
        SabangGoodsRes response = (SabangGoodsRes) sabangHttpClient.sendGoods(config, requestFileName).getBody();
        return (List<SabangGoodsResult>) CollectionUtils.emptyIfNull(response.getData());
    }

    private List<SabangGoodsResult> callGoodsLiteAPI(final MallConfiguration config, final String requestFileName) throws Exception {
        SabangGoodsRes response = (SabangGoodsRes) sabangHttpClient.sendGoodsLite(config, requestFileName).getBody();
        return (List<SabangGoodsResult>) CollectionUtils.emptyIfNull(response.getData());
    }

    private void saveLog(SabangGoodsReq goodsRequest, List<SabangGoodsResult> responseResults) {
        String companyCode = goodsRequest.getCompanyCode();

        for (SabangGoodsResult goodsResult : responseResults) {
            updateMallGoodsCodeByResult(companyCode, goodsResult);
            SabangGoodsReq.SabangGoodsInfo goodsReqInfo = findGoodsRequestInfo(goodsRequest, goodsResult);
            SabangGoodsLog savedGoodsLog = saveResponseLog(companyCode, goodsResult);
            saveGoodsTransferLog(goodsReqInfo, savedGoodsLog);
        }
    }

    private void saveLiteLog(CommonSearchReq goodsRequest, List<SabangGoodsResult> responseResults) {

        for (SabangGoodsResult goodsResult : responseResults) {
            updateMallGoodsCodeByResult(goodsRequest.getCompanyCode(), goodsResult);
            saveResponseLog(goodsRequest.getCompanyCode(), goodsResult);
        }
    }

    private SabangGoodsReq.SabangGoodsInfo findGoodsRequestInfo(SabangGoodsReq goodsRequest, SabangGoodsResult goodsResult) {
        return goodsRequest.getSabangGoodsInfoList().stream()
                .filter(info -> info.getGoodsCode().equals(goodsResult.getCompanyGoodsCd()))
                .findFirst().orElse(new SabangGoodsReq.SabangGoodsInfo(goodsResult.getCompanyGoodsCd()));
    }

    private void updateMallGoodsCodeByResult(String companyCode, SabangGoodsResult goodsResult) {
        if (RESULT_MESSAGE_SUCCESS.equals(goodsResult.getResult())) {
            sabangGoodsRepository.findById(new SabangGoodsId(companyCode, goodsResult.getCompanyGoodsCd()))
                    .ifPresent(sabangGoods -> sabangGoods.setMallGoodsCode(goodsResult.getProductId()));
        }
    }

    private SabangGoodsLog saveResponseLog(String companyCode, SabangGoodsResult goodsResult) {
        return sabangGoodsLogRepository.save(SabangGoodsLogMapper.INSTANCE.toEntity(companyCode, goodsResult));
    }

    private SabangGoodsTransLog saveGoodsTransferLog(SabangGoodsReq.SabangGoodsInfo goodsReqInfo, SabangGoodsLog savedGoodsLog) {
        return sabangGoodsTransLogRepository.save(SabangGoodsTransLogMapper.INSTANCE.toEntity(goodsReqInfo, savedGoodsLog));
    }

    private int getSuccessResultCount(List<SabangGoodsResult> goodsResult) {
        return (int) goodsResult.stream().filter(data -> RESULT_MESSAGE_SUCCESS.equals(data.getResult())).count();
    }
}
