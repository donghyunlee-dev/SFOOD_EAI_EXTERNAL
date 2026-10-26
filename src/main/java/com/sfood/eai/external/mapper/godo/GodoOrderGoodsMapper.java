package com.sfood.eai.external.mapper.godo;

import com.sfood.eai.external.dto.godo.GodoOrderGoodsData;
import com.sfood.eai.external.entity.godo.GodoOrder;
import com.sfood.eai.external.entity.godo.GodoOrderGoods;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GodoOrderGoodsMapper {
    GodoOrderGoodsMapper INSTANCE = Mappers.getMapper(GodoOrderGoodsMapper.class);

    @Mapping(target = "companyCode", source = "order.companyCode")
    @Mapping(target = "marketMallCode", source = "order.marketMallCode")
    @Mapping(target = "orderNo", source = "goods.orderNo")
    @Mapping(target = "orderStatus", source = "goods.orderStatus")
    @Mapping(target = "paymentDt", source = "goods.paymentDt")
    @Mapping(target = "mallSno", source = "goods.mallSno")
    @Mapping(target = "apiOrderGoodsNo", source = "goods.apiOrderGoodsNo")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target = "updateId", constant = "external")
    GodoOrderGoods toEntity(GodoOrder order, GodoOrderGoodsData goods);
}
