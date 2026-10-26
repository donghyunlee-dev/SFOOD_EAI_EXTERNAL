package com.sfood.eai.external.mapper.order;

import com.sfood.eai.external.entity.order.OrderGoods;
import com.sfood.eai.external.entity.order.OrderGoodsStatusLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderGoodsStatusLogMapper {
    OrderGoodsStatusLogMapper INSTANCE = Mappers.getMapper(OrderGoodsStatusLogMapper.class);
    @Mapping(target = "preOrderStatusCode", source = "orderGoods.preOrderStatusCode")
    @Mapping(target = "orderStatusCode", source = "orderGoods.orderStatusCode")
    @Mapping(target = "createId", constant = "external")
    OrderGoodsStatusLog toEntity(OrderGoods orderGoods);
}
