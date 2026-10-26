package com.sfood.eai.external.entity.godo;

import com.sfood.eai.external.entity.id.godo.GodoOrderInfoId;
import com.sfood.eai.external.entity.order.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@IdClass(GodoOrderInfoId.class)
@Entity
@Table(name="TBL_GODO_ORDER_INFO")
public class GodoOrderInfo extends BaseEntity {
    @Id
    @Column(name = "COMPANY_CODE")
    private String companyCode; // 법인코드

    @Id
    @Column(name = "MARKET_MALL_CODE")
    private String marketMallCode; // 연동몰코드

    @Id
    @Column(name = "ORDER_NO")
    private String orderNo;

    @Id
    @Column(name = "SNO")
    private Integer sno = 0;

    @Column(name = "ORDER_INFO_CD")
    private Integer orderInfoCd;

    @Column(name = "ORDER_NAME")
    private String orderName;

    @Column(name = "ORDER_EMAIL")
    private String orderEmail;

    @Column(name = "ORDER_PHONE")
    private String orderPhone;

    @Column(name = "ORDER_CELL_PHONE")
    private String orderCellPhone;

    @Column(name = "ORDER_ZIPCODE")
    private String orderZipcode;

    @Column(name = "ORDER_ZONECODE")
    private String orderZonecode;

    @Column(name = "ORDER_ADDRESS")
    private String orderAddress;

    @Column(name = "ORDER_ADDRESS_SUB")
    private String orderAddressSub;

    @Column(name = "RECEIVER_NAME")
    private String receiverName;

    @Column(name = "RECEIVER_PHONE")
    private String receiverPhone;

    @Column(name = "RECEIVER_CELL_PHONE")
    private String receiverCellPhone;

    @Column(name = "RECEIVER_ZIPCODE")
    private String receiverZipcode;

    @Column(name = "RECEIVER_ZONECODE")
    private String receiverZonecode;

    @Column(name = "RECEIVER_ADDRESSS")
    private String receiverAddress;

    @Column(name = "RECEIVER_ADDRESS_SUB")
    private String receiverAddressSub;

    @Column(name = "CUSTOM_ID_NUMBER")
    private String customIdNumber;

    @Column(name = "ORDER_MEMO")
    private String orderMemo;

    @Column(name = "RECEIVER_USE_SAFE_NUMBER_FL")
    private String receiverUseSafeNumberFl;

    @Column(name = "RECEIVER_SAFE_NUMBER_DT")
    private String receiverSafeNumberDt;

    @Column(name = "RECEIVER_SAFE_NUMBER")
    private String receiverSafeNumber;

    @Column(name = "ORDER_PHONE_PREFIX_CODE")
    private String orderPhonePrefixCode;

    @Column(name = "ORDER_PHONE_PREFIX")
    private Integer orderPhonePrefix;

    @Column(name = "ORDER_CELL_PHONE_PREFIX_CODE")
    private String orderCellPhonePrefixCode;

    @Column(name = "ORDER_CELL_PHONE_PREFIX")
    private Integer orderCellPhonePrefix;

    @Column(name = "ORDER_STATE")
    private String orderState;

    @Column(name = "ORDER_CITY")
    private String orderCity;

    @Column(name = "RECEIVER_COUNTRY_CODE")
    private String receiverCountryCode;

    @Column(name = "RECEIVER_PHONE_PREFIX_CODE")
    private String receiverPhonePrefixCode;

    @Column(name = "RECEIVER_PHONE_PREFIX")
    private Integer receiverPhonePrefix;

    @Column(name = "RECEIVER_CELL_PHONE_PREFIX_CODE")
    private String receiverCellPhonePrefixCode;

    @Column(name = "RECEIVER_CELL_PHONE_PREFIX")
    private Integer receiverCellPhonePrefix;

    @Column(name = "RECEIVER_COUNTRY")
    private String receiverCountry;

    @Column(name = "RECEIVER_STATE")
    private String receiverState;

    @Column(name = "RECEIVER_CITY")
    private String receiverCity;

    @Column(name = "DELIVERY_VISIT")
    private String deliveryVisit;

    @Column(name = "VISIT_ADDRESS")
    private String visitAddress;

    @Column(name = "VISIT_NAME")
    private String visitName;

    @Column(name = "VISIT_PHONE")
    private String visitPhone;

    @Column(name = "VISIT_MEMO")
    private String visitMemo;

    @Column(name = "PACKET_CODE")
    private String packetCode;

    @Column(name = "SMS_FL")
    private String smsFl;

    public String getFullAddress() {
        return orderAddress + " " + orderAddressSub;
    }
}
