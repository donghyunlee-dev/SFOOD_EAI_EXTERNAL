package com.sfood.eai.external.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sfood.eai.external.config.MallConfiguration;
import com.sfood.eai.external.dto.ApiCallLogReq;
import com.sfood.eai.external.dto.ApiCallLogRes;
import com.sfood.eai.external.dto.CommonSearchReq;
import com.sfood.eai.external.entity.CollectSchedule;
import com.sfood.eai.external.entity.MarketMallMaster;
import com.sfood.eai.external.exception.APIException;

import java.util.List;

public interface MarketMallService {

    MallConfiguration getConfiguration(CommonSearchReq searchReq) throws APIException;

    List<CollectSchedule> findScheduleByChannel(String channelCode);

    void saveRequestLog(ApiCallLogReq apiCallLogReq) throws JsonProcessingException;

    void saveResponseLog(ApiCallLogRes apiCallLogRes) throws JsonProcessingException;
}
