package com.sfood.eai.external.mapper.godo;

import com.sfood.eai.external.dto.godo.GodoAddGoodsData;
import com.sfood.eai.external.entity.godo.GodoAddGoods;
import com.sfood.eai.external.entity.godo.GodoOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GodoAddGoodsMapper {
    GodoAddGoodsMapper INSTANCE = Mappers.getMapper(GodoAddGoodsMapper.class);

    @Mapping(target = "companyCode", source = "order.companyCode")
    @Mapping(target = "marketMallCode", source = "order.marketMallCode")
    @Mapping(target = "orderNo", source = "goods.orderNo")
    @Mapping(target = "assignGoodsCnt", source = "goods.goodsCnt")
    @Mapping(target = "mallSno", source = "goods.mallSno")
    @Mapping(target = "apiOrderGoodsNo", source = "goods.apiOrderGoodsNo")
    @Mapping(target = "orderStatus", source = "goods.orderStatus")
    @Mapping(target = "paymentDt", source = "goods.paymentDt")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target = "updateId", constant = "external")
    GodoAddGoods toEntity(GodoOrder order, GodoAddGoodsData goods);
}
