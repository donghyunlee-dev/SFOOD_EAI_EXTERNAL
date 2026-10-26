package com.sfood.eai.external.mapper.order;

import com.sfood.eai.external.dto.order.OrderCloseStatus;
import com.sfood.eai.external.entity.id.order.OrderCloseInfoId;
import com.sfood.eai.external.entity.id.order.OrderGoodsId;
import com.sfood.eai.external.entity.order.OrderCloseInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderCloseInfoMapper {

    OrderCloseInfoMapper INSTANCE = Mappers.getMapper(OrderCloseInfoMapper.class);

    default OrderCloseInfoId toId(OrderCloseStatus source) {
        return new OrderCloseInfoId(source.getCompanyCode(), source.getOrderMergeNo(), source.getOrderDetailSeq());
    }
    @Mapping(target = "id", source = "orderCloseStatus")
    @Mapping(target = "closeMethod", constant = "B")
    @Mapping(target = "closeYn", constant = "Y")
    @Mapping(target = "orderType", source = "orderCloseStatus.orderType")
    @Mapping(target = "closeDate", expression = "java(java.time.ZonedDateTime.now(java.time.ZoneId.of(\"Asia/Seoul\")).toLocalDateTime())")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target = "createDate", expression = "java(java.time.ZonedDateTime.now(java.time.ZoneId.of(\"Asia/Seoul\")).toLocalDateTime())")
    @Mapping(target = "updateId", constant = "external")
    @Mapping(target = "updateDate", expression = "java(java.time.ZonedDateTime.now(java.time.ZoneId.of(\"Asia/Seoul\")).toLocalDateTime())")
    OrderCloseInfo toEntity(OrderCloseStatus orderCloseStatus);
}
