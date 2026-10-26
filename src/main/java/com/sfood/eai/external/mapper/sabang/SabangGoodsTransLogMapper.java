package com.sfood.eai.external.mapper.sabang;

import com.sfood.eai.external.dto.sabang.SabangGoodsReq;
import com.sfood.eai.external.entity.sabang.SabangGoodsLog;
import com.sfood.eai.external.entity.sabang.SabangGoodsTransLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SabangGoodsTransLogMapper {
    SabangGoodsTransLogMapper INSTANCE = Mappers.getMapper(SabangGoodsTransLogMapper.class);

    @Mapping(target = "seqNo", source = "goodsLog.seqNo")
    @Mapping(target = "companyCode", source = "goodsLog.companyCode")
    @Mapping(target = "goodsCode", source = "goodsLog.goodsCode")
    @Mapping(target = "mallGoodsCode", source = "goodsLog.productId")
    @Mapping(target = "mallGoodsName", source = "goodsInfo.goodsName")
    @Mapping(target = "supplier", source = "goodsInfo.supplier")
    @Mapping(target = "origin", source = "goodsInfo.origin")
    @Mapping(target = "searchKeyword", source = "goodsInfo.search")
    @Mapping(target = "goodsStatus", source = "goodsInfo.status")
    @Mapping(target = "cost", source = "goodsInfo.cost")
    @Mapping(target = "salesPrice", source = "goodsInfo.salesPrice")
    @Mapping(target = "consumerPrice", source = "goodsInfo.consumerPrice")
    @Mapping(target = "taxYn", source = "goodsInfo.taxYn")
    @Mapping(target = "result", source = "goodsLog.result")
    SabangGoodsTransLog toEntity(SabangGoodsReq.SabangGoodsInfo goodsInfo, SabangGoodsLog goodsLog);
}
