package com.sfood.eai.external.mapper.godo;

import com.sfood.eai.external.dto.godo.GodoClaimData;
import com.sfood.eai.external.dto.godo.GodoOrderGoodsData;
import com.sfood.eai.external.entity.godo.GodoOrder;
import com.sfood.eai.external.entity.godo.GodoOrderClaim;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GodoOrderClaimMapper {
    GodoOrderClaimMapper INSTANCE = Mappers.getMapper(GodoOrderClaimMapper.class);

    @Mapping(target = "companyCode", source = "order.companyCode")
    @Mapping(target = "marketMallCode", source = "order.marketMallCode")
    @Mapping(target = "orderNo", source = "goodsData.orderNo")
    @Mapping(target = "sno", source = "goodsData.sno")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target = "updateId", constant = "external")
    GodoOrderClaim toEntity(GodoOrder order, GodoOrderGoodsData goodsData, GodoClaimData claimData);
}
