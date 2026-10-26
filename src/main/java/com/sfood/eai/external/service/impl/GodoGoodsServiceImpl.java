package com.sfood.eai.external.service.impl;

import com.sfood.eai.external.component.document.XmlDocument;
import com.sfood.eai.external.component.document.godo.GodoDocument;
import com.sfood.eai.external.component.document.godo.data.GodoGoodsData;
import com.sfood.eai.external.component.http.GodoHttpClient;
import com.sfood.eai.external.component.oci.StorageManager;
import com.sfood.eai.external.config.AppConfig;
import com.sfood.eai.external.config.MallConfiguration;
import com.sfood.eai.external.dto.CommonDataRes;
import com.sfood.eai.external.dto.godo.GodoGoodsReq;
import com.sfood.eai.external.dto.godo.GodoGoodsRes;
import com.sfood.eai.external.entity.godo.GodoGoodsLog;
import com.sfood.eai.external.entity.godo.GodoGoodsTransLog;
import com.sfood.eai.external.entity.id.godo.GodoGoodsId;
import com.sfood.eai.external.mapper.godo.GodoGoodsLogMapper;
import com.sfood.eai.external.mapper.godo.GodoGoodsMapper;
import com.sfood.eai.external.mapper.godo.GodoGoodsTransLogMapper;
import com.sfood.eai.external.repository.godo.GodoGoodsLogRepository;
import com.sfood.eai.external.repository.godo.GodoGoodsRepository;
import com.sfood.eai.external.repository.godo.GodoGoodsTransLogRepository;
import com.sfood.eai.external.service.GodoGoodsService;
import com.sfood.eai.external.service.MarketMallService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class GodoGoodsServiceImpl implements GodoGoodsService {
    private static final String RESULT_MESSAGE_SUCCESS = "000";

    private final MarketMallService mallService;
    private final GodoHttpClient godoHttpClient;
    private final StorageManager storageManager;
    private final GodoGoodsRepository godoGoodsRepository;
    private final GodoGoodsLogRepository godoGoodsLogRepository;
    private final GodoGoodsTransLogRepository godoGoodsTransLogRepository;

    @Transactional
    @Override
    public CommonDataRes sendNewGoods(GodoGoodsReq goodsRequest) throws Exception {

        if ("dev".equals(AppConfig.ACTIVE_PROFILE)) {
            return CommonDataRes.noDataResponse();
        }

        MallConfiguration configuration = mallService.getConfiguration(goodsRequest);
        CommonDataRes res = new CommonDataRes();
        try {
            boolean success = uploadGoodsXmlFile(configuration, goodsRequest);

            if (!success) {
                return CommonDataRes.noDataResponse();
            }

            GodoGoodsRes response = callAddGoodsAPI(configuration, storageManager.getFilePublicUrl(configuration.getStorageFileName()));

            if (ObjectUtils.isEmpty(response.getReturnData())) {
                return CommonDataRes.noDataResponse();
            }

            saveLog(LogType.INSERT, goodsRequest, response.getReturnData().getGoodsData());

            res.setSuccessCnt(getSuccessResultCount(response));
        } catch (Exception e) {
            throw e;
        } finally {
            storageManager.deleteXmlFile(configuration.getStorageDirPath(), configuration.getXmlFileName());
        }

        return res;
    }

    @Transactional
    @Override
    public CommonDataRes sendGoodsUpdateInfo(GodoGoodsReq goodsRequest) throws Exception {

        if ("dev".equals(AppConfig.ACTIVE_PROFILE)) {
            return CommonDataRes.noDataResponse();
        }

        CommonDataRes res = new CommonDataRes();
        MallConfiguration configuration = mallService.getConfiguration(goodsRequest);

        try {

            boolean success = uploadGoodsXmlFile(configuration, goodsRequest);

            if (!success) {
                return CommonDataRes.noDataResponse();
            }

            GodoGoodsRes response = callUpdateGoodsAPI(configuration, storageManager.getFilePublicUrl(configuration.getStorageFileName()));

            if (ObjectUtils.isEmpty(response.getReturnData())) {
                return CommonDataRes.noDataResponse();
            }

            saveLog(LogType.UPDATE, goodsRequest, response.getReturnData().getGoodsData());

            res.setSuccessCnt(getSuccessResultCount(response));

        } catch (Exception e) {
            throw e;
        } finally {
            storageManager.deleteXmlFile(configuration.getStorageDirPath(), configuration.getXmlFileName());
        }

        return res;
    }

    private List<GodoGoodsData> createGoodsData(GodoGoodsReq goodsReq) {

        List<GodoGoodsData> goodsDataList = new ArrayList<>();

        int goodsIndex = 1;
        for (GodoGoodsReq.GodoGoodsInfo godoGoodsInfo : goodsReq.getGodoGoodsInfoList()) {
            godoGoodsInfo.setIdx(String.valueOf(goodsIndex));
            goodsDataList.add(GodoGoodsMapper.INSTANCE.toDataModel(godoGoodsInfo));
            goodsIndex++;
        }
        return goodsDataList;
    }

    private boolean uploadGoodsXmlFile(MallConfiguration configuration, GodoGoodsReq goodsRequest) throws Exception {

        List<GodoGoodsData> goodsDataList = createGoodsData(goodsRequest);

        if (CollectionUtils.isEmpty(goodsDataList)) {
            return false;
        }

        XmlDocument goodsDoc = new GodoDocument<>(goodsDataList);

        storageManager.uploadXmlFile(goodsDoc, configuration.getStorageDirPath(), configuration.getXmlFileName());
        return true;
    }

    private GodoGoodsRes callAddGoodsAPI(final MallConfiguration mallConfiguration, final String requestFileName) throws Exception {
        return godoHttpClient.sendNewGoods(mallConfiguration, requestFileName).getBody();
    }

    private GodoGoodsRes callUpdateGoodsAPI(final MallConfiguration mallConfiguration, final String requestFileName) throws Exception {
        return godoHttpClient.sendUpdateGoods(mallConfiguration, requestFileName).getBody();
    }

    private void saveLog(LogType type, GodoGoodsReq goodsRequest, List<GodoGoodsRes.GoodsData> goodsResults) {

        for (GodoGoodsRes.GoodsData goodsResult : goodsResults) {

            if (RESULT_MESSAGE_SUCCESS.equals(goodsResult.getResultCode()) && LogType.INSERT.equals(type)) {
                updateMallGoodsCodeByResult(goodsRequest, goodsResult);
                changeGoodsNoForUpdate(goodsRequest, goodsResult);
            }
            GodoGoodsLog goodsLog = saveResponseLog(type, goodsRequest, goodsResult);
            GodoGoodsReq.GodoGoodsInfo goodsInfo = findGoodsRequestInfo(goodsRequest, goodsResult);
            saveTransferLog(goodsInfo, goodsLog);
        }

        goodsRequest.getGodoGoodsInfoList().removeIf(goodsInfo -> goodsInfo.getGoodsNo() == null);
    }

    private GodoGoodsLog saveResponseLog(LogType type, GodoGoodsReq goodsReq, GodoGoodsRes.GoodsData goodsResult) {
        return godoGoodsLogRepository.save(GodoGoodsLogMapper.INSTANCE.toEntity(goodsReq, type.getName(), goodsResult));
    }

    private void updateMallGoodsCodeByResult(GodoGoodsReq goodsReq, GodoGoodsRes.GoodsData goodsResult) {
        godoGoodsRepository.findById(new GodoGoodsId(goodsReq.getCompanyCode(), goodsReq.getMallCode(), goodsResult.getData().getGoodsCd()))
                .ifPresent(godoGoods -> godoGoods.setMallGoodsCode(goodsResult.getData().getGoodsNo()));
    }

    private GodoGoodsReq.GodoGoodsInfo findGoodsRequestInfo(GodoGoodsReq goodsRequest, GodoGoodsRes.GoodsData goodsResult) {
        Optional<GodoGoodsReq.GodoGoodsInfo> optionalGodoGoods = goodsRequest.getGodoGoodsInfoList().stream()
                .filter(goods -> goods.getIdx().equals(goodsResult.getIdx()))
                .findFirst();

        return optionalGodoGoods.orElse(null);
    }

    private GodoGoodsTransLog saveTransferLog(GodoGoodsReq.GodoGoodsInfo goodsInfo, GodoGoodsLog goodsLog) {

        if (ObjectUtils.isEmpty(goodsLog)) {
            return null;
        }
        return godoGoodsTransLogRepository.save(GodoGoodsTransLogMapper.INSTANCE.toEntity(goodsInfo, goodsLog));
    }

    private void changeGoodsNoForUpdate(GodoGoodsReq goodsReq, GodoGoodsRes.GoodsData goodsResult) {
        goodsReq.getGodoGoodsInfoList().stream()
                .filter(goodsInfo -> goodsInfo.getGoodsCd().equals(goodsResult.getData().getGoodsCd()))
                .findFirst()
                .ifPresent(godoGoods -> godoGoods.setGoodsNo(goodsResult.getData().getGoodsNo()));
    }

    private int getSuccessResultCount(GodoGoodsRes goodsRes) {
        return (int) goodsRes.getReturnData().getGoodsData().stream()
                .filter(data -> RESULT_MESSAGE_SUCCESS.equals(data.getResultCode())).count();
    }

    @Getter
    @AllArgsConstructor
    private enum LogType {
        INSERT("등록"),
        UPDATE("수정"),
        DELETE("삭제");

        private final String name;
    }
}
