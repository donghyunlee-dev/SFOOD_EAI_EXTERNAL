package com.sfood.eai.external.mapper.order;


import com.sfood.eai.external.entity.godo.GodoOrderGoods;
import com.sfood.eai.external.entity.godo.GodoOrderInfo;
import com.sfood.eai.external.entity.order.OrderDelivery;
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
public interface OrderDeliveryMapper {

    OrderDeliveryMapper INSTANCE = Mappers.getMapper(OrderDeliveryMapper.class);
    @Mapping(target = "companyCode", source = "order.companyCode")
    @Mapping(target = "marketMallCode", source = "order.marketMallCode")
    @Mapping(target = "orderMergeNo", source = "order.orderMergeNo")
    @Mapping(target = "orderDetailSeq", source = "order.maxOrderDetailSeq")
    @Mapping(target = "orderUserName", source = "orderDelivery.orderUserName")
    @Mapping(target = "orderUserCel", source = "orderDelivery.orderUserCel")
    @Mapping(target = "orderUserAddress", source = "orderDelivery.orderUserAddress")
    @Mapping(target = "orderUserZipcode", source = "orderDelivery.orderUserZipcode")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target="createDate", ignore = true)
    @Mapping(target = "updateId", constant = "external")
    @Mapping(target="updateDate", ignore = true)
    OrderDelivery toEntity(OrderMaster order, OrderDelivery orderDelivery);

    @Mapping(target = "companyCode", source = "order.companyCode")
    @Mapping(target = "marketMallCode", source = "order.marketMallCode")
    @Mapping(target = "orderDetailSeq", source = "order.maxOrderDetailSeq")
    @Mapping(target = "delvTypeCode", constant = "PC")
    @Mapping(target = "hopeDelvDate", source = "goods.deliveryDt", qualifiedByName = "convertGodoLocalDateTime")
    @Mapping(target = "invoiceCompanySno", source = "goods.invoiceCompanySno")
    @Mapping(target = "invoiceCompany", source = "goods.invoiceCompany")
    @Mapping(target = "orderUserName", source = "info.orderName")
    @Mapping(target = "orderUserCel", source = "info.orderCellPhone")
    @Mapping(target = "orderUserAddress", source = "info.orderAddress")
    @Mapping(target = "orderUserAddressSub", source = "info.orderAddressSub")
    @Mapping(target = "orderUserZipcode", source = "info", qualifiedByName = "getOrderZipCode")
    @Mapping(target = "receiveName", source = "info.receiverName")
    @Mapping(target = "receiveTel", source = "info.receiverPhone")
    @Mapping(target = "receiveCel", source = "info.receiverCellPhone")
    @Mapping(target = "receiveAddr", source = "info.receiverAddress")
    @Mapping(target = "receiveAddr2", source = "info.receiverAddressSub")
    @Mapping(target = "receiveZipcode", source = "info", qualifiedByName = "getReceiveZipCode")
    @Mapping(target = "delvMsg", source = "info.orderMemo")
    @Mapping(target = "deliveryReqDate", source = "info.customIdNumber")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target="createDate", ignore = true)
    @Mapping(target = "updateId", constant = "external")
    @Mapping(target="updateDate", ignore = true)
    OrderDelivery toEntity(OrderMaster order, GodoOrderGoods goods, GodoOrderInfo info);

    @Mapping(target = "companyCode", source = "order.companyCode")
    @Mapping(target = "marketMallCode", source = "order.marketMallCode")
    @Mapping(target = "orderDetailSeq", source = "order.maxOrderDetailSeq")
    @Mapping(target = "delvTypeCode", constant = "PC")
    @Mapping(target = "hopeDelvDate", source = "sabangOrder.hopeDelvDate", qualifiedByName = "convertLocalDateTime")
    @Mapping(target = "invoiceCompanySno", source = "sabangOrder.deliveryId")
    @Mapping(target = "orderUserName", ignore = true)
    @Mapping(target = "orderUserCel", ignore = true)
    @Mapping(target = "orderUserAddress", ignore = true)
    @Mapping(target = "orderUserZipcode", ignore = true)
    @Mapping(target = "receiveName", source = "sabangOrder.receiveName")
    @Mapping(target = "receiveTel", source = "sabangOrder.receiveTel")
    @Mapping(target = "receiveCel", source = "sabangOrder.receiveCel")
    @Mapping(target = "receiveEmail", source = "sabangOrder.receiveEmail")
    @Mapping(target = "receiveAddr", source = "sabangOrder.receiveAddr")
    @Mapping(target = "receiveZipcode", source = "sabangOrder.receiveZipcode")
    @Mapping(target = "delvMsg", source = "sabangOrder.delvMsg1")
    @Mapping(target = "delvMsg2", source = "sabangOrder.mulDelvMsg")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target="createDate", ignore = true)
    @Mapping(target = "updateId", constant = "external")
    @Mapping(target="updateDate", ignore = true)
    OrderDelivery toEntity(OrderMaster order, SabangOrder sabangOrder);

    @Mapping(target = "companyCode", source = "order.companyCode")
    @Mapping(target = "marketMallCode", source = "order.marketMallCode")
    @Mapping(target = "orderDetailSeq", source = "order.maxOrderDetailSeq")
    @Mapping(target = "delvTypeCode", constant = "LD")
    @Mapping(target = "hopeDelvDate", source = "ediOrder.dlvyDt", qualifiedByName = "convertLocalDateTime")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target="createDate", ignore = true)
    @Mapping(target = "updateId", constant = "external")
    @Mapping(target="updateDate", ignore = true)
    OrderDelivery toEntity(OrderMaster order, EdiOrderData ediOrder);

    @Named("getReceiveZipCode")
    default String getReceiveZipCode(GodoOrderInfo info) {
        if (!StringUtils.hasText(info.getReceiverZipcode()) || info.getReceiverZipcode().replace("-", "").isEmpty()) {
            return info.getReceiverZonecode();
        } else {
            return info.getReceiverZipcode();
        }
    }

    @Named("getOrderZipCode")
    default String getOrderZipCode(GodoOrderInfo info) {
        if (!StringUtils.hasText(info.getOrderZipcode()) || info.getOrderZipcode().replace("-", "").isEmpty()) {
            return info.getOrderZonecode();
        } else {
            return info.getOrderZipcode();
        }
    }

    @Named("convertGodoLocalDateTime")
    default LocalDateTime convertGodoLocalDateTime(String date) {

        if (!StringUtils.hasText(date)) {
            return null;
        }

        if ("0000-00-00 00:00:00".equals(date)) {
            return null;
        }

        if(PATTERN_LOCAL_DATE_TIME.matcher(date).matches()) {
            return CustomDateUtils.parseLocalDateTime(date, "yyyy-MM-dd HH:mm:ss");
        } else {
            return CustomDateUtils.parseLocalDateTime(date + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        }
    }

    @Named("convertLocalDateTime")
    default LocalDateTime convertLocalDateTime(String date) {

        if (!StringUtils.hasText(date)) {
            return null;
        }

        return CustomDateUtils.parseLocalDateTime(date.replace("-","") + "000000", "yyyyMMddHHmmss");
    }
}
