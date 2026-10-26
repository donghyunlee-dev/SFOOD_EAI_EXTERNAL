package com.sfood.eai.external.mapper.sabang;

import com.sfood.eai.external.dto.sabang.SabangGoodsResult;
import com.sfood.eai.external.entity.sabang.SabangGoodsLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SabangGoodsLogMapper {
    SabangGoodsLogMapper INSTANCE = Mappers.getMapper(SabangGoodsLogMapper.class);
    @Mapping(target = "goodsCode", source = "resultData.companyGoodsCd")
    @Mapping(target = "result", source = "resultData.result")
    @Mapping(target = "message", source = "resultData.resultMsg")
    @Mapping(target = "modeType", source = "resultData.mode")
    @Mapping(target = "productId", source = "resultData.productId")
    SabangGoodsLog toEntity(String companyCode, SabangGoodsResult resultData);
}
