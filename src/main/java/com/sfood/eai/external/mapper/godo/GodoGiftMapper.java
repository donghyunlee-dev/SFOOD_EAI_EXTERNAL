package com.sfood.eai.external.mapper.godo;

import com.sfood.eai.external.dto.godo.GodoGiftData;
import com.sfood.eai.external.entity.godo.GodoGift;
import com.sfood.eai.external.entity.godo.GodoOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GodoGiftMapper {

    GodoGiftMapper INSTANCE = Mappers.getMapper(GodoGiftMapper.class);

    @Mapping(target = "companyCode", source = "order.companyCode")
    @Mapping(target = "marketMallCode", source = "order.marketMallCode")
    @Mapping(target = "orderNo", source = "order.orderNo")
    @Mapping(target = "sno", source = "gift.sno")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target = "updateId", constant = "external")
    GodoGift toEntity(GodoOrder order, GodoGiftData gift);
}
