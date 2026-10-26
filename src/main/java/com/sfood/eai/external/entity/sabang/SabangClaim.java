package com.sfood.eai.external.entity.sabang;

import com.sfood.eai.external.entity.id.sabang.SabangOrderId;
import com.sfood.eai.external.entity.order.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@IdClass(SabangOrderId.class)
@Entity
@Table(name="TBL_SABANG_CLAIM")
public class SabangClaim extends BaseEntity {
    @Id
    @Column(name = "COMPANY_CODE")
    private String companyCode; // 법인코드

    @Id
    @Column(name = "MARKET_MALL_CODE")
    private String marketMallCode; // 연동몰코드

    @Id
    @Column(name = "IDX")
    private String idx;

    @Column(name = "ORDER_ID")
    private String orderId;

    @Column(name = "MALL_ID")
    private String mallId;

    @Column(name = "MALL_USER_ID")
    private String mallUserId;

    @Column(name = "ORDER_STATUS")
    private String orderStatus;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "USER_TEL")
    private String userTel;

    @Column(name = "USER_CEL")
    private String userCel;

    @Column(name = "USER_EMAIL")
    private String userEmail;

    @Column(name = "RECEIVE_TEL")
    private String receiveTel;

    @Column(name = "RECEIVE_CEL")
    private String receiveCel;

    @Column(name = "RECEIVE_EMAIL")
    private String receiveEmail;

    @Column(name = "DELV_MSG")
    private String delvMsg;

    @Column(name = "RECEIVE_NAME")
    private String receiveName;

    @Column(name = "RECEIVE_ZIPCODE")
    private String receiveZipcode;

    @Column(name = "RECEIVE_ADDR")
    private String receiveAddr;

    @Column(name = "TOTAL_COST")
    private String totalCost;

    @Column(name = "PAY_COST")
    private String payCost;

    @Column(name = "ORDER_DATE")
    private String orderDate;

    @Column(name = "PARTNER_ID")
    private String partnerId;

    @Column(name = "DPARTNER_ID")
    private String dpartnerId;

    @Column(name = "MALL_PRODUCT_ID")
    private String mallProductId;

    @Column(name = "PRODUCT_ID")
    private String productId;

    @Column(name = "SKU_ID")
    private String skuId;

    @Column(name = "P_PRODUCT_NAME")
    private String pProductName;

    @Column(name = "P_SKU_VALUE")
    private String pSkuValue;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "SALE_COST")
    private String saleCost;

    @Column(name = "MALL_WON_COST")
    private String mallWonCost;

    @Column(name = "WON_COST")
    private String wonCost;

    @Column(name = "SKU_VALUE")
    private String skuValue;

    @Column(name = "SALE_CNT")
    private String saleCnt;

    @Column(name = "COMPAYNY_GOODS_CD")
    private String compaynyGoodsCd;

    @Column(name = "CLAME_STATUS_GUBUN")
    private String clameStatusGubun;

    @Column(name = "CLAME_CONTENT")
    private String clameContent;

    @Column(name = "CLAME_INS_DATE")
    private String clameInsDate;

    @Column(name = "CLAME_REG_DATE")
    private String clameRegDate;

    @Column(name = "CL_IDX")
    private String clIdx;

    @Column(name = "MALL_ORDER_ID")
    private String mallOrderId;

    @Column(name = "STATUS")
    private String status;
}

