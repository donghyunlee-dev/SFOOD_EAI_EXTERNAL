package com.sfood.eai.external.mapper.sabang;

import com.sfood.eai.external.dto.CommonSearchReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.sfood.eai.external.dto.sabang.SabangOrderData;
import com.sfood.eai.external.dto.sabang.SabangOrderSearchReq;
import com.sfood.eai.external.entity.sabang.SabangOrder;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SabangOrderMapper {

    SabangOrderMapper INSTANCE = Mappers.getMapper(SabangOrderMapper.class);

    @Mapping(target = "companyCode", source = "searchDto.companyCode")
    @Mapping(target = "marketMallCode", source = "searchDto.mallCode")
    @Mapping(target = "orderChannelCode", source = "searchDto.channelCode")
    @Mapping(target = "inMallCode", source = "inMallCode")
    @Mapping(target = "orderStatus", source = "sabangOrderData.orderStatus")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "userId", source = "sabangOrderData.userId")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target = "updateId", constant = "external")
    SabangOrder toEntity(CommonSearchReq searchDto, SabangOrderData sabangOrderData, String inMallCode);
}
