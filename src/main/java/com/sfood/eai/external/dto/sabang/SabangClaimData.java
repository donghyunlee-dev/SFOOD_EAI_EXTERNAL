package com.sfood.eai.external.dto.sabang;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SabangClaimData {

	@JacksonXmlProperty(localName = "IDX")
	private String idx; // 주문번호(사방넷)

	@JacksonXmlProperty(localName = "ORDER_ID")
	private String orderId;

	@JacksonXmlProperty(localName = "MALL_ID")
	private String mallId;

	@JacksonXmlProperty(localName = "MALL_USER_ID")
	private String mallUserId;

	@JacksonXmlProperty(localName = "ORDER_STATUS")
	private String orderStatus;

	@JacksonXmlProperty(localName = "USER_ID")
	private String userId;

	@JacksonXmlProperty(localName = "USER_NAME")
	private String userName;

	@JacksonXmlProperty(localName = "USER_TEL")
	private String userTel;

	@JacksonXmlProperty(localName = "USER_CEL")
	private String userCel;

	@JacksonXmlProperty(localName = "USER_EMAIL")
	private String userEmail;

	@JacksonXmlProperty(localName = "RECEIVE_TEL")
	private String receiveTel;

	@JacksonXmlProperty(localName = "RECEIVE_CEL")
	private String receiveCel;

	@JacksonXmlProperty(localName = "RECEIVE_EMAIL")
	private String receiveEmail;

	@JacksonXmlProperty(localName = "DELV_MSG")
	private String delvMsg;

	@JacksonXmlProperty(localName = "RECEIVE_NAME")
	private String receiveName;

	@JacksonXmlProperty(localName = "RECEIVE_ZIPCODE")
	private String receiveZipcode;

	@JacksonXmlProperty(localName = "RECEIVE_ADDR")
	private String receiveAddr;

	@JacksonXmlProperty(localName = "TOTAL_COST")
	private String totalCost;

	@JacksonXmlProperty(localName = "PAY_COST")
	private String payCost;

	@JacksonXmlProperty(localName = "ORDER_DATE")
	private String orderDate;

	@JacksonXmlProperty(localName = "PARTNER_ID")
	private String partnerId;

	@JacksonXmlProperty(localName = "DPARTNER_ID")
	private String dpartnerId;

	@JacksonXmlProperty(localName = "MALL_PRODUCT_ID")
	private String mallProductId;

	@JacksonXmlProperty(localName = "PRODUCT_ID")
	private String productId;

	@JacksonXmlProperty(localName = "SKU_ID")
	private String skuId;

	@JacksonXmlProperty(localName = "P_PRODUCT_NAME")
	private String pProductName;

	@JacksonXmlProperty(localName = "P_SKU_VALUE")
	private String pSkuValue;

	@JacksonXmlProperty(localName = "PRODUCT_NAME")
	private String productName;

	@JacksonXmlProperty(localName = "SALE_COST")
	private String saleCost;

	@JacksonXmlProperty(localName = "MALL_WON_COST")
	private String mallWonCost;

	@JacksonXmlProperty(localName = "WON_COST")
	private String wonCost;

	@JacksonXmlProperty(localName = "SKU_VALUE")
	private String skuValue;

	@JacksonXmlProperty(localName = "SALE_CNT")
	private String saleCnt;

	@JacksonXmlProperty(localName = "COMPAYNY_GOODS_CD")
	private String compaynyGoodsCd;

	@JacksonXmlProperty(localName = "CLAME_STATUS_GUBUN")
	private String clameStatusGubun;

	@JacksonXmlProperty(localName = "CLAME_CONTENT")
	private String clameContent;

	@JacksonXmlProperty(localName = "CLAME_INS_DATE")
	private String clameInsDate;

	@JacksonXmlProperty(localName = "CLAME_REG_DATE")
	private String clameRegDate;

	@JacksonXmlProperty(localName = "CL_IDX")
	private String clIdx;

	@JacksonXmlProperty(localName = "MALL_ORDER_ID")
	private String mallOrderId;
}
