package com.sfood.eai.external.mapper.godo;

import com.sfood.eai.external.dto.CommonSearchReq;
import com.sfood.eai.external.dto.godo.GodoGoodsRes;
import com.sfood.eai.external.entity.godo.GodoGoodsLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GodoGoodsLogMapper {
    GodoGoodsLogMapper INSTANCE = Mappers.getMapper(GodoGoodsLogMapper.class);

    @Mapping(target = "companyCode", source = "req.companyCode")
    @Mapping(target = "mallCode", source = "req.mallCode")
    @Mapping(target = "goodsCode", source = "goods.data.goodsCd")
    @Mapping(target = "modeType", source = "mode")
    @Mapping(target = "goodsNo", source = "goods.data.goodsNo")
    @Mapping(target = "result", source = "goods.resultCode")
    @Mapping(target = "message", source = "goods.message")
    GodoGoodsLog toEntity(CommonSearchReq req, String mode, GodoGoodsRes.GoodsData goods);

}
