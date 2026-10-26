package com.sfood.eai.external.mapper.order;

import com.sfood.eai.external.entity.godo.GodoOrderClaim;
import com.sfood.eai.external.entity.id.order.OrderClaimId;
import com.sfood.eai.external.entity.order.OrderClaim;
import com.sfood.eai.external.entity.order.OrderGoods;
import com.sfood.eai.external.entity.sabang.SabangClaim;
import com.sfood.eai.external.util.CustomDateUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.sfood.eai.external.util.CustomDateUtils.PATTERN_LOCAL_DATE_TIME;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderClaimMapper {
    OrderClaimMapper INSTANCE = Mappers.getMapper(OrderClaimMapper.class);
    @Mapping(target = "id", source = "orderGoods")
    @Mapping(target = "claimDate", source = "claim.regDt", qualifiedByName = "convertLocalDate")
    @Mapping(target = "claimReasonMstCode", source = "orderGoods.orderStatusCode", qualifiedByName = "convertReasonCode")
    @Mapping(target = "claimReasonDetailCode", source = "orderGoods.orderStatusCode", qualifiedByName = "convertReasonDetailCode")
    @Mapping(target = "claimReasonContent", source = "claim.handleDetailReason")
    @Mapping(target = "marketClaimId", source = "claim.sno")
    @Mapping(target = "orderClaimReceiveDate", source = "claim.regDt", qualifiedByName = "convertLocalDateTime")
    @Mapping(target = "orderClaimAcceptDate", expression = "java(java.time.ZonedDateTime.now(java.time.ZoneId.of(\"Asia/Seoul\")).toLocalDateTime())")
    @Mapping(target = "marketStopYn", constant = "N")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target="createDate", ignore = true)
    @Mapping(target = "updateId", constant = "external")
    @Mapping(target="updateDate", ignore = true)
    OrderClaim toEntity(OrderGoods orderGoods, GodoOrderClaim claim);

    default OrderClaimId toId(OrderGoods goods) {
        return new OrderClaimId(goods.getCompanyCode(), goods.getMarketMallCode(), goods.getOrderMergeNo(), goods.getOrderDetailSeq());
    }

    @Named("convertReasonCode")
    default String convertReasonCode(String orderStatusCode) {

        if (orderStatusCode.startsWith("OS30")) {
            return "OS30";
        } else if (orderStatusCode.startsWith("OS40")) {
            return "OS40";
        } else if (orderStatusCode.startsWith("OS50")) {
            return "OS50";
        } else if (orderStatusCode.startsWith("OS60"))  {
            return "OS30";
        } else {
            return "";
        }
    }

    @Named("convertReasonDetailCode")
    default String convertReasonDetailCode(String orderStatusCode) {

        if (orderStatusCode.startsWith("OS30")) {
            return "OS30E";
        } else if (orderStatusCode.startsWith("OS40")) {
            return "OS40E";
        } else if (orderStatusCode.startsWith("OS50")) {
            return "OS50E";
        } else if (orderStatusCode.startsWith("OS60")) {
            return "OS30E";
        } else {
            return "";
        }
    }

    @Named("convertLocalDate")
    default LocalDateTime convertLocalDate(String date) {
        return CustomDateUtils.parseLocalDateTime(date, "yyyy-MM-dd HH:mm:ss");
    }

    @Named("convertLocalDateTime")
    default LocalDateTime convertLocalDateTime(String date) {
        if (PATTERN_LOCAL_DATE_TIME.matcher(date).matches()) {
            return CustomDateUtils.parseLocalDateTime(date, "yyyy-MM-dd HH:mm:ss");
        } else {
            return CustomDateUtils.parseLocalDateTime(date, "yyyyMMddHHmmss");
        }
    }
}
