package com.sfood.eai.external.mapper.godo;

import com.sfood.eai.external.dto.godo.GodoOrderData;
import com.sfood.eai.external.dto.godo.GodoOrderSearchReq;
import com.sfood.eai.external.entity.godo.GodoOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GodoOrderMapper {

    GodoOrderMapper INSTANCE = Mappers.getMapper(GodoOrderMapper.class);

    @Mapping(target = "companyCode", source = "searchReq.companyCode")
    @Mapping(target = "marketMallCode", source = "searchReq.mallCode")
    @Mapping(target = "orderChannelCode", source = "searchReq.channelCode")
    @Mapping(target = "orderNo", source = "godoOrder.orderNo")
    @Mapping(target = "orderStatus", source = "godoOrder.orderStatus")
    @Mapping(target = "mallSno", source = "godoOrder.mallSno")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target = "updateId", constant = "external")
    GodoOrder toEntity(GodoOrderSearchReq searchReq, GodoOrderData godoOrder);


}
