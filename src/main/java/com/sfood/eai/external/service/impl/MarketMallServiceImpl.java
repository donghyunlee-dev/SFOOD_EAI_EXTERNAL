package com.sfood.eai.external.service.impl;

import com.sfood.eai.external.config.MallConfiguration;
import com.sfood.eai.external.dto.ApiCallLogReq;
import com.sfood.eai.external.dto.ApiCallLogRes;
import com.sfood.eai.external.dto.CommonSearchReq;
import com.sfood.eai.external.entity.*;
import com.sfood.eai.external.entity.id.ApiUrlId;
import com.sfood.eai.external.exception.APIException;
import com.sfood.eai.external.mapper.ApiCallLogMapper;
import com.sfood.eai.external.repository.*;
import com.sfood.eai.external.service.MarketMallService;
import com.sfood.eai.external.type.APIError;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MarketMallServiceImpl implements MarketMallService {

    private final MarketMallMasterRepository marketMallRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final ApiUrlRepository apiUrlRepository;
    private final ApiCallLogRepository apiCallLogRepository;
    private final CollectScheduleFileRepository collectScheduleFileRepository;
    private final CollectScheduleRepository collectScheduleRepository;

    private List<MarketMallMaster> mallList;
    private List<CollectSchedule> fixedCollectSchedules; // 전일 조회 고정 스케쥴 - Json 파일 읽어서 저장
    private List<CollectSchedule> collectSchedules;

    @PostConstruct
    private void initialize() {
        setMalls();
        setFixedCollectSchedules();
        setCollectSchedules();
    }

    @Override
    public MallConfiguration getConfiguration(CommonSearchReq searchReq) throws APIException {
        ApiUrl apiUrl = getApiUrl(searchReq);

        if (ObjectUtils.isEmpty(apiUrl)) {
            throw new APIException(APIError.INVALID_REQUEST);
        }

        return MallConfiguration.builder()
                .condition(searchReq)
                .apiKeys((ConcurrentHashMap<String, String>) getApiKeys(searchReq.getCompanyCode(), searchReq.getMallCode()).stream().collect(Collectors.toConcurrentMap(ApiKey::getKeyName, ApiKey::getKeyValue)))
                .mallApiUrl(getMarketMall(searchReq.getChannelCode(), searchReq.getMallCode()).getApiUrl() + apiUrl.getApiUri())
                .xmlFileName(apiUrl.getSubInfo1())
                .storageDirPath(apiUrl.getSubInfo2())
                .build();
    }

    @Override
    public List<CollectSchedule> findScheduleByChannel(final String channelCode) {

        List<MarketMallMaster> malls = findAllByChannel(channelCode);

        Set<String> mallCodes = malls.stream()
                .map(mall -> mall.getMallCode())
                .collect(Collectors.toSet());

        return this.collectSchedules.stream().filter(sch -> mallCodes.contains(sch.getMallCode())).toList();
    }

    @Transactional
    @Override
    public void saveRequestLog(ApiCallLogReq apiCallLogReq)  {
        ApiCallLog apiCallLog = ApiCallLogMapper.INSTANCE.toEntity(apiCallLogReq);
        apiCallLogRepository.save(apiCallLog);
    }

    @Transactional
    @Override
    public void saveResponseLog(ApiCallLogRes apiCallLogRes) {
        ApiCallLog apiCallLog = apiCallLogRepository.findByLogId(apiCallLogRes.getLogId());
        apiCallLog.setResponseData(apiCallLogRes);
    }

    @Transactional(readOnly = true)
    @Scheduled(cron = "0 0 0 * * ?")
    public void setMalls() {
        this.mallList = new ArrayList<>(marketMallRepository.findAllInUse());
    }

    @Transactional(readOnly = true)
    @Scheduled(fixedDelay = 55000)
    public void setCollectSchedules() {
        this.collectSchedules  = new ArrayList<>(fixedCollectSchedules);
        findCollectSchedules();
    }

    private List<MarketMallMaster> findAllByChannel(final String channelCode) {
        return this.mallList.stream().filter(mall -> mall.getChannelCode().equals(channelCode)).toList();
    }

    private MarketMallMaster getMarketMall(String channelCode, String mallCode) {
        return this.mallList.stream()
                .filter(mall ->
                        mall.getChannelCode().equals(channelCode) && mall.getMallCode().equals(mallCode))
                .findFirst()
                .orElse(null);
    }

    private List<ApiKey> getApiKeys(String companyCode, String mallCode) {
        return apiKeyRepository.findByCompanyCodeAndMallCode(companyCode, mallCode);
    }

    private ApiUrl getApiUrl(CommonSearchReq searchDto) {
        ApiUrlId apiUrlId = ApiUrlId.builder()
                .companyCode(searchDto.getCompanyCode())
                .mallCode(searchDto.getMallCode())
                .requestUri(searchDto.getRequestUri())
                .build();

        return apiUrlRepository.findById(apiUrlId).orElse(null);
    }

    private void findCollectSchedules() {
        if (CollectionUtils.isEmpty(collectSchedules)) {
            this.collectSchedules = collectScheduleRepository.findAll();
        } else {
            this.collectSchedules.addAll(collectScheduleRepository.findAll());
        }
    }

    private void setFixedCollectSchedules() {
        this.fixedCollectSchedules = collectScheduleFileRepository.findAll();
    }
}
