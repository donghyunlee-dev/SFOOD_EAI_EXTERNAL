package com.sfood.eai.external.mapper;

import com.sfood.eai.external.dto.ApiCallLogReq;
import com.sfood.eai.external.entity.ApiCallLog;
import com.sfood.eai.external.util.CustomStringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApiCallLogMapper {
    ApiCallLogMapper INSTANCE = Mappers.getMapper(ApiCallLogMapper.class);
    @Mapping(target = "logId", source = "id")
    @Mapping(target = "logType", source = "logType")
    @Mapping(target = "companyCode", source = "companyCode")
    @Mapping(target = "mallCode", source = "mallCode")
    @Mapping(target = "mallId", source = "mallId")
    @Mapping(target = "interfaceId", source = "type")
    @Mapping(target = "statusCode", source = "statusCode")
    @Mapping(target = "statusName", source = "statusName")
    @Mapping(target = "reqData", source = "data", qualifiedByName = "substringReqData")
    @Mapping(target = "reqTime", source = "reqTime")
    @Mapping(target = "clientIp", source =  "clientIp")
    @Mapping(target = "userId", source =  "userId")
    ApiCallLog toEntity(ApiCallLogReq apiCallLogReq);

    @Named("substringReqData")
    default String substringReqData(String data) {
        return CustomStringUtils.substringByBytes(data, 0, 4000);
    }
}
