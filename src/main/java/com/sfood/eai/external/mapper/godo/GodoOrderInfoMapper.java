package com.sfood.eai.external.mapper.godo;

import com.sfood.eai.external.dto.godo.GodoOrderInfoData;
import com.sfood.eai.external.entity.godo.GodoOrder;
import com.sfood.eai.external.entity.godo.GodoOrderInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GodoOrderInfoMapper {
    GodoOrderInfoMapper INSTANCE = Mappers.getMapper(GodoOrderInfoMapper.class);

    @Mapping(target = "companyCode", source = "order.companyCode")
    @Mapping(target = "marketMallCode", source = "order.marketMallCode")
    @Mapping(target = "orderNo", source = "order.orderNo")
    @Mapping(target = "sno", source = "info.sno", defaultValue = "1")
    @Mapping(target = "orderEmail", source = "info.orderEmail")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target = "updateId", constant = "external")
    GodoOrderInfo toEntity(GodoOrder order, GodoOrderInfoData info);
}
