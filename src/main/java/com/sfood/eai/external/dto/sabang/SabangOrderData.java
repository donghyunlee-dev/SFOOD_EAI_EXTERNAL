package com.sfood.eai.external.dto.sabang;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SabangOrderData {

	@JacksonXmlProperty(localName = "IDX")
	private String idx; // 주문번호(사방넷)
	
	@JacksonXmlProperty(localName = "ORDER_ID")
	private String orderId; // 주문번호(쇼핑몰)
	
	@JacksonXmlProperty(localName = "MALL_ID")
	private String mallId; // 쇼핑몰명
	
	@JacksonXmlProperty(localName = "MALL_USER_ID")
	private String mallUserId; // 쇼핑몰ID
	
	@JacksonXmlProperty(localName = "MALL_USER_ID2")
	private String mallUserId2; // 쇼핑몰ID(공급계약번호)
	
	@JacksonXmlProperty(localName = "ORDER_STATUS")
	private String orderStatus; // 	주문상태
	
	@JacksonXmlProperty(localName = "USER_ID")
	private String userId; // 주문자ID
	
	@JacksonXmlProperty(localName = "USER_NAME")
	private String userName; // 주문자명
	
	@JacksonXmlProperty(localName = "USER_TEL")
	private String userTel; // 주문자전화번호
	
	@JacksonXmlProperty(localName = "USER_CEL")
	private String userCel; // 주문자핸드폰번호
	
	@JacksonXmlProperty(localName = "USER_EMAIL")
	private String userEmail; // 주문자이메일주소
	
	@JacksonXmlProperty(localName = "RECEIVE_TEL")
	private String receiveTel; // 수취인전화번호
	
	@JacksonXmlProperty(localName = "RECEIVE_CEL")
	private String receiveCel; // 수취인핸드폰번호
	
	@JacksonXmlProperty(localName = "RECEIVE_EMAIL")
	private String receiveEmail; // 수취인이메일주소
	
	@JacksonXmlProperty(localName = "DELV_MSG")
	private String delvMsg; // 배송메세지
	
	@JacksonXmlProperty(localName = "RECEIVE_NAME")
	private String receiveName; // 수취인명
	
	@JacksonXmlProperty(localName = "RECEIVE_ZIPCODE")
	private String receiveZipcode; // 수취인우편번호
	
	@JacksonXmlProperty(localName = "RECEIVE_ADDR")
	private String receiveAddr; // 수취인주소
	
	@JacksonXmlProperty(localName = "TOTAL_COST")
	private String totalCost; // 주문금액
	
	@JacksonXmlProperty(localName = "PAY_COST")
	private String payCost; // 결제금액
	
	@JacksonXmlProperty(localName = "ORDER_DATE")
	private String orderDate; // 주문일자
	
	@JacksonXmlProperty(localName = "PARTNER_ID")
	private String partnerId; // 매입처명
	
	@JacksonXmlProperty(localName = "DPARTNER_ID")
	private String dpartnerId; // 물류처명
	
	@JacksonXmlProperty(localName = "MALL_PRODUCT_ID")
	private String mallProductId; // 상품코드(쇼핑몰)
	
	@JacksonXmlProperty(localName = "PRODUCT_ID")
	private String productId; // 품번코드(사방넷)
	
	@JacksonXmlProperty(localName = "SKU_ID")
	private String skuId; // 단품코드(사방넷)
	
	@JacksonXmlProperty(localName = "P_PRODUCT_NAME")
	private String pproductName; // 상품명(확정)
	
	@JacksonXmlProperty(localName = "P_SKU_VALUE")
	private String pskuValue; // 옵션(확정)
	
	@JacksonXmlProperty(localName = "PRODUCT_NAME")
	private String productName; // 상품명(수집)
	
	@JacksonXmlProperty(localName = "SALE_COST")
	private String saleCost; // 판매가(수집)
	
	@JacksonXmlProperty(localName = "MALL_WON_COST")
	private String mallWonCost; // 공급단가
	
	@JacksonXmlProperty(localName = "WON_COST")
	private String wonCost; // 원가
	
	@JacksonXmlProperty(localName = "SKU_VALUE")
	private String skuValue; // 옵션(수집)
	
	@JacksonXmlProperty(localName = "SALE_CNT")
	private String saleCnt; // 수량
	
	@JacksonXmlProperty(localName = "DELIVERY_METHOD_STR")
	private String deliveryMethodStr; // 배송구분
	
	@JacksonXmlProperty(localName = "DELV_COST")
	private String delvCost; // 배송비(수집)
	
	@JacksonXmlProperty(localName = "COMPAYNY_GOODS_CD")
	private String companyGoodsCd; // 자체상품코드
	
	@JacksonXmlProperty(localName = "SKU_ALIAS")
	private String skuAlias; // 옵션별칭
	
	@JacksonXmlProperty(localName = "BOX_EA")
	private String boxEa; // EA(상품)
	
	@JacksonXmlProperty(localName = "JUNG_CHK_YN")
	private String jungChkYn; // 정산대조확인여부
	
	@JacksonXmlProperty(localName = "MALL_ORDER_SEQ")
	private String mallOrderSeq; // 주문순번
	
	@JacksonXmlProperty(localName = "MALL_ORDER_ID")
	private String mallOrderId; // 부주문번호
	
	@JacksonXmlProperty(localName = "ETC_FIELD3")
	private String etcField3; // 수정된 수집옵션명
	
	@JacksonXmlProperty(localName = "ORDER_GUBUN")
	private String orderGubun; // 주문구분
	
	@JacksonXmlProperty(localName = "P_EA")
	private String pea; // EA(확정)
	
	@JacksonXmlProperty(localName = "REG_DATE")
	private String regDate; // 수집일자
	
	@JacksonXmlProperty(localName = "ORDER_ETC_1")
	private String orderEtc1; // 자사몰필드1
	
	@JacksonXmlProperty(localName = "ORDER_ETC_2")
	private String orderEtc2; // 자사몰필드2
	
	@JacksonXmlProperty(localName = "ORDER_ETC_3")
	private String orderEtc3; // 자사몰필드3
	
	@JacksonXmlProperty(localName = "ORDER_ETC_4")
	private String orderEtc4; // 자사몰필드4
	
	@JacksonXmlProperty(localName = "ORDER_ETC_5")
	private String orderEtc5; // 자사몰필드5
	
	@JacksonXmlProperty(localName = "ORDER_ETC_6")
	private String orderEtc6; // 자사몰필드6
	
	@JacksonXmlProperty(localName = "ORDER_ETC_7")
	private String orderEtc7; // 자사몰필드7
	
	@JacksonXmlProperty(localName = "ORDER_ETC_8")
	private String orderEtc8; // 자사몰필드8
	
	@JacksonXmlProperty(localName = "ORDER_ETC_9")
	private String orderEtc9; // 자사몰필드9
	
	@JacksonXmlProperty(localName = "ORDER_ETC_10")
	private String orderEtc10; // 자사몰필드10
	
	@JacksonXmlProperty(localName = "ORDER_ETC_11")
	private String orderEtc11; // 자사몰필드11
	
	@JacksonXmlProperty(localName = "ORDER_ETC_12")
	private String orderEtc12; // 자사몰필드12
	
	@JacksonXmlProperty(localName = "ORDER_ETC_13")
	private String orderEtc13; // 자사몰필드13
	
	@JacksonXmlProperty(localName = "ORDER_ETC_14")
	private String orderEtc14; // 자사몰필드14
	
	@JacksonXmlProperty(localName = "ord_field2")
	private String ordField2; // 세트분리주문구분
	
	@JacksonXmlProperty(localName = "copy_idx")
	private String copyIdx; // 원주문번호(사방넷)
	
	@JacksonXmlProperty(localName = "GOODS_NM_PR")
	private String goodsNmPr; // 출력상품명
	
	@JacksonXmlProperty(localName = "GOODS_KEYWORD")
	private String goodsKeyword; // 상품약어
	
	@JacksonXmlProperty(localName = "ORD_CONFIRM_DATE")
	private String ordConfirmDate; // 주문 확인일자
	
	@JacksonXmlProperty(localName = "RTN_DT")
	private String rtnDt; // 반품 완료일자
	
	@JacksonXmlProperty(localName = "CHNG_DT")
	private String chngDt; // 교환 완료일자
	
	@JacksonXmlProperty(localName = "DELIVERY_CONFIRM_DATE")
	private String deliveryConfirmDate; // 	출고 완료일자
	
	@JacksonXmlProperty(localName = "CANCEL_DT")
	private String cancelDt; // 취소 완료일자
	
	@JacksonXmlProperty(localName = "CLASS_CD1")
	private String classCd1; // 대분류코드
	
	@JacksonXmlProperty(localName = "CLASS_CD2")
	private String classCd2; // 중분류코드
	
	@JacksonXmlProperty(localName = "CLASS_CD3")
	private String classCd3; // 소분류코드
	
	@JacksonXmlProperty(localName = "CLASS_CD4")
	private String classCd4; // 세분류코드
	
	@JacksonXmlProperty(localName = "BRAND_NM")
	private String brandNm; // 브랜드명
	
	@JacksonXmlProperty(localName = "DELIVERY_ID")
	private String deliveryId; // 택배사코드
	
	@JacksonXmlProperty(localName = "INVOICE_NO")
	private String invoiceNo; // 송장번호
	
	@JacksonXmlProperty(localName = "HOPE_DELV_DATE")
	private String hopeDelvDate; // 배송희망일자
	
	@JacksonXmlProperty(localName = "FLD_DSP")
	private String fldDsp; // 주문엑셀용
	
	@JacksonXmlProperty(localName = "INV_SEND_MSG")
	private String invSendMsg; // 운송장 전송 결과 메시지
	
	@JacksonXmlProperty(localName = "MODEL_NO")
	private String modelNo; // 모델NO
	
	@JacksonXmlProperty(localName = "SET_GUBUN")
	private String setGubun; // 상품구분
	
	@JacksonXmlProperty(localName = "ETC_MSG")
	private String etcMsg; // 기타메세지
	
	@JacksonXmlProperty(localName = "DELV_MSG1")
	private String delvMsg1; // 배송메세지
	
	@JacksonXmlProperty(localName = "MUL_DELV_MSG")
	private String mulDelvMsg; // 물류메세지
	
	@JacksonXmlProperty(localName = "BARCODE")
	private String barcode; // 바코드
	
	@JacksonXmlProperty(localName = "INV_SEND_DM")
	private String invSendDm; // 송장전송일자
	
	@JacksonXmlProperty(localName = "DELIVERY_METHOD_STR2")
	private String deliveryMethodStr2; // 배송구분(배송비반영)
	
	@JacksonXmlProperty(localName = "FREE_GIFT")
	private String freeGift; // 사은품
	
	@JacksonXmlProperty(localName = "ACNT_REGS_SRNO")
	private String acntRegsSrno; // 계정등록순번
	
	@JacksonXmlProperty(localName = "MODEL_NAME")
	private String modelName; // 모델명
}
