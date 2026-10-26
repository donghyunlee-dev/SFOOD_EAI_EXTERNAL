package com.sfood.eai.external.mapper.godo;

import com.sfood.eai.external.dto.godo.GodoGoodsReq;
import com.sfood.eai.external.entity.godo.GodoGoodsLog;
import com.sfood.eai.external.entity.godo.GodoGoodsTransLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GodoGoodsTransLogMapper {
    GodoGoodsTransLogMapper INSTANCE = Mappers.getMapper(GodoGoodsTransLogMapper.class);
    @Mapping(target = "seqNo", source = "goodsLog.seqNo")
    @Mapping(target = "companyCode", source = "goodsLog.companyCode")
    @Mapping(target = "mallCode", source = "goodsLog.mallCode")
    @Mapping(target = "goodsCode", source = "godoGoods.goodsCd")
    @Mapping(target = "mallGoodsCode", source = "goodsLog.goodsNo")
    @Mapping(target = "mallGoodsName", source = "godoGoods.goodsNm")
    @Mapping(target = "supplier", source = "godoGoods.supplier")
    @Mapping(target = "origin", source = "godoGoods.origin")
    @Mapping(target = "searchKeyword", source = "godoGoods.search")
    @Mapping(target = "displayPcYn", source = "godoGoods.goodsDisplayFl")
    @Mapping(target = "salesPcYn", source = "godoGoods.goodsSellFl")
    @Mapping(target = "displayMobileYn", source = "godoGoods.goodsDisplayMobileFl")
    @Mapping(target = "salesMobileYn", source = "godoGoods.goodsSellMobileFl")
    @Mapping(target = "cost", source = "godoGoods.cost")
    @Mapping(target = "fixedPrice", source = "godoGoods.fixedPrice")
    @Mapping(target = "salesPrice", source = "godoGoods.salesPrice")
    @Mapping(target = "taxYn", source = "godoGoods.taxYn")
    @Mapping(target = "result", source = "goodsLog.result", qualifiedByName = "convertResultCode")
    GodoGoodsTransLog toEntity(GodoGoodsReq.GodoGoodsInfo godoGoods, GodoGoodsLog goodsLog);

    @Named("convertResultCode")
    default String convertResultCode(String resultCode) {
        if ("000".equals(resultCode)) {
            return "SUCCESS";
        } else {
            return "FAIL";
        }
    }
}
