package com.sfood.eai.external.mapper.order;

import com.sfood.eai.external.dto.retail.RetailOrderSearchReq;
import com.sfood.eai.external.dto.sabang.SabangOrderSearchReq;
import com.sfood.eai.external.entity.godo.GodoDataEntity;
import com.sfood.eai.external.entity.godo.GodoOrderInfo;
import com.sfood.eai.external.entity.order.OrderMaster;
import com.sfood.eai.external.entity.retail.EdiOrderData;
import com.sfood.eai.external.entity.sabang.SabangOrder;
import com.sfood.eai.external.util.CustomDateUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

import static com.sfood.eai.external.util.CustomDateUtils.PATTERN_LOCAL_DATE_TIME;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMasterMapper {

    OrderMasterMapper INSTANCE = Mappers.getMapper(OrderMasterMapper.class);

    @Mapping(target = "companyCode", source = "godoData.companyCode")
    @Mapping(target = "marketMallCode", source = "godoData.mallCode")
    @Mapping(target = "orderDate", source = "godoData.order.orderDate", qualifiedByName = "convertLocalDateTime")
    @Mapping(target = "marketOrderNo", source = "godoData.order.orderNo")
    @Mapping(target = "orderChannelCode", source = "godoData.channelCode")
    @Mapping(target = "orderMergeTitle", source = "godoData.order.orderGoodsNm")
    @Mapping(target = "inMallCode", source = "godoData.order.memId")
    @Mapping(target = "emergencyYn", constant = "N")
    @Mapping(target = "orderUserName", source = "info.orderName")
    @Mapping(target = "orderUserTel", source = "info.orderPhone")
    @Mapping(target = "orderUserCel", source = "info.orderCellPhone")
    @Mapping(target = "orderUserEmail", source = "info.orderEmail")
    @Mapping(target = "orderUserAddress", source = "info.fullAddress")
    @Mapping(target = "orderUserZipcode", source = "info", qualifiedByName = "selectZipCodeType")
    @Mapping(target = "maxOrderDetailSeq", constant = "0")
    @Mapping(target = "orderMstMsg", source = "info.orderMemo")
    @Mapping(target = "receiptFl", source = "godoData.order.receiptFl")
    @Mapping(target = "settleKindCode", source = "godoData.order.settleKind")
    @Mapping(target = "orderStatus", source = "godoData.order.orderStatus")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target="createDate", ignore = true)
    @Mapping(target = "updateId", constant = "external")
    @Mapping(target="updateDate", ignore = true)
    OrderMaster toEntity(GodoDataEntity godoData, GodoOrderInfo info);

    @Mapping(target = "companyCode", source = "orderSearch.companyCode")
    @Mapping(target = "marketMallCode", source = "orderSearch.mallCode")
    @Mapping(target = "orderDate", source = "sabangOrder.orderDate", qualifiedByName = "convertLocalDateTime")
    @Mapping(target = "marketOrderNo", source = "sabangOrder.orderId")
    @Mapping(target = "orderChannelCode", source = "orderSearch.channelCode")
    @Mapping(target = "inMallCode", source = "sabangOrder.inMallCode")
    @Mapping(target = "emergencyYn", constant = "N")
    @Mapping(target = "orderUserName", source = "sabangOrder.userName")
    @Mapping(target = "orderUserTel", source = "sabangOrder.userTel")
    @Mapping(target = "orderUserCel", source = "sabangOrder.userCel")
    @Mapping(target = "orderUserEmail", source = "sabangOrder.userEmail")
    @Mapping(target = "orderStatus", source = "sabangOrder.orderStatus")
    @Mapping(target = "maxOrderDetailSeq", constant = "0")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target = "updateId", constant = "external")
    OrderMaster toEntity(SabangOrderSearchReq orderSearch, SabangOrder sabangOrder);

    @Mapping(target = "companyCode", source = "orderSearch.companyCode")
    @Mapping(target = "marketMallCode", source = "orderSearch.mallCode")
    @Mapping(target = "orderDate", source = "ediOrderData.custSoDt", qualifiedByName = "convertLocalDateTime")
    @Mapping(target = "marketOrderNo", source = "ediOrderData.mesgNo")
    @Mapping(target = "orderChannelCode", source = "orderSearch.channelCode")
    @Mapping(target = "inMallCode", source = "ediOrderData.ediBpCd" )
    @Mapping(target = "orderPartnerCode", source = "ediOrderData", qualifiedByName = "replaceEdiOrderPartnerCode")
    @Mapping(target = "sshPartnerCode", source = "ediOrderData.shipToParty")
    @Mapping(target = "delvStoreCode", source = "ediOrderData.custBpCd")
    @Mapping(target = "emergencyYn", constant = "N")
    @Mapping(target = "maxOrderDetailSeq", constant = "0")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target = "updateId", constant = "external")
    OrderMaster toEntity(RetailOrderSearchReq orderSearch, EdiOrderData ediOrderData);

    @Named("selectZipCodeType")
    default String selectZipCodeType(GodoOrderInfo info) {
        if (!StringUtils.hasText(info.getOrderZipcode()) || info.getOrderZipcode().replace("-", "").isEmpty()) {
            return info.getOrderZonecode();
        } else {
            return info.getOrderZipcode();
        }
    }

    @Named("convertLocalDateTime")
    default LocalDateTime convertLocalDateTime(String date) {
        if (!StringUtils.hasText(date)) {
            return CustomDateUtils.parseLocalDateTime(CustomDateUtils.now() + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        }

        if (PATTERN_LOCAL_DATE_TIME.matcher(date).matches()) {
            return CustomDateUtils.parseLocalDateTime(date, "yyyy-MM-dd HH:mm:ss");
        } else {
            return CustomDateUtils.parseLocalDateTime(date, "yyyyMMddHHmmss");
        }
    }

    @Named("replaceEdiOrderPartnerCode")
    default String replaceOrderPartnerCode(EdiOrderData ediOrderData) {
        return StringUtils.hasText(ediOrderData.getSoldToParty()) ? ediOrderData.getSoldToParty() : ediOrderData.getCustBpCd();
    }
}
