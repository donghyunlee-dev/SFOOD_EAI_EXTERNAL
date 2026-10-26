package com.sfood.eai.external.mapper.godo;

import com.sfood.eai.external.dto.godo.GodoOrderDeliveryData;
import com.sfood.eai.external.entity.godo.GodoOrder;
import com.sfood.eai.external.entity.godo.GodoOrderDelivery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GodoOrderDeliveryMapper {
    GodoOrderDeliveryMapper INSTANCE = Mappers.getMapper(GodoOrderDeliveryMapper.class);

    @Mapping(target = "companyCode", source = "order.companyCode")
    @Mapping(target = "marketMallCode", source = "order.marketMallCode")
    @Mapping(target = "orderNo", source = "order.orderNo")
    @Mapping(target = "sno", source = "delivery.sno", defaultValue = "1")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target = "updateId", constant = "external")
    GodoOrderDelivery toEntity(GodoOrder order, GodoOrderDeliveryData delivery);
}
