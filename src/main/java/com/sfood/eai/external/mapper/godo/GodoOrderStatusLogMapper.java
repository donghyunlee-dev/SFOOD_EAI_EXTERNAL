package com.sfood.eai.external.mapper.godo;

import com.sfood.eai.external.dto.godo.GodoOrderStatusRes;
import com.sfood.eai.external.dto.godo.GodoResponseHeader;
import com.sfood.eai.external.dto.godo.GodoStatusData;
import com.sfood.eai.external.entity.godo.GodoOrderStatusLog;
import com.sfood.eai.external.entity.id.order.OrderGoodsId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GodoOrderStatusLogMapper {
    GodoOrderStatusLogMapper INSTANCE = Mappers.getMapper(GodoOrderStatusLogMapper.class);

    @Mapping(target = "companyCode", source = "goodsId.companyCode")
    @Mapping(target = "marketMallCode", source = "goodsId.marketMallCode")
    @Mapping(target = "orderMergeNo", source = "goodsId.orderMergeNo")
    @Mapping(target = "orderDetailSeq", source = "goodsId.orderDetailSeq")
    @Mapping(target = "code", source = "godoStatus.code")
    @Mapping(target = "msg", source = "godoStatus.msg")
    @Mapping(target = "orderNo", source = "godoStatus.orderNo")
    @Mapping(target = "sno", source = "godoStatus.sno")
    @Mapping(target = "orderStatus", source = "godoStatus.orderStatus")
    @Mapping(target = "resCode", source = "header.code")
    @Mapping(target = "resMsg", source = "header.msg")
    GodoOrderStatusLog toEntity(OrderGoodsId goodsId, GodoResponseHeader header, GodoStatusData godoStatus);

    @Mapping(target = "companyCode", source = "orderGoods.companyCode")
    @Mapping(target = "marketMallCode", source = "orderGoods.marketMallCode")
    @Mapping(target = "orderMergeNo", source = "orderGoods.orderMergeNo")
    @Mapping(target = "orderDetailSeq", source = "orderGoods.orderDetailSeq")
    @Mapping(target = "code", source = "response.code")
    @Mapping(target = "msg", source = "response.msg")
    @Mapping(target = "resCode", source = "response.header.code")
    @Mapping(target = "resMsg", source = "response.header.msg")
    GodoOrderStatusLog toEntity(OrderGoodsId orderGoods, GodoOrderStatusRes response);

}
