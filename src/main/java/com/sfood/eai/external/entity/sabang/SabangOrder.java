package com.sfood.eai.external.entity.sabang;

import com.sfood.eai.external.entity.id.sabang.SabangOrderId;
import com.sfood.eai.external.entity.order.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@Getter
@Setter
@NoArgsConstructor
@IdClass(SabangOrderId.class)
@Entity
@Table(name="TBL_SABANG_ORDER")
public class SabangOrder extends BaseEntity {
	@Id
	@Column(name = "COMPANY_CODE")
	private String companyCode; // 법인코드

	@Id
	@Column(name = "MARKET_MALL_CODE")
	private String marketMallCode; // 연동몰코드

	@Id
	@Column(name = "IDX")
	private String idx; // 주문번호(사방넷) - 주문 등록(수집시) 사방넷에서 부여되는 유니크번호
	
	@Column(name = "ORDER_CHANNEL_CODE")
	private String orderChannelCode; // 주문채널코드

	@Column(name = "IN_MALL_CODE")
	private String inMallCode;

	@Column(name = "ORDER_ID")
	private String orderId; // 주문번호(2차쇼핑몰) - 주문수집시 쇼핑몰(2차)의 주문번호

	@Column(name = "MALL_ID")
	private String mallId; // 쇼핑몰명 - 수집된 주문의 쇼핑몰명, API 문서기준으로 MALL_ID
	
	@Column(name = "MALL_USER_ID")
	private String mallUserId; // 쇼핑몰ID - 수집된 주문의 쇼핑몰 로그인아이디
	
	@Column(name = "MALL_USER_ID2")
	private String mallUserId2; // 쇼핑몰ID - 쇼핑몰ID(공급계약번호)
	
	@Column(name = "ORDER_STATUS")
	private String orderStatus; // 사방넷 주문상태
	
	@Column(name = "USER_ID")
	private String userId; // 주문자ID
	
	@Column(name = "USER_NAME")
	private String userName; // 주문자명
	
	@Column(name = "USER_TEL")
	private String userTel; // 주문자전화번호
	
	@Column(name = "USER_CEL")
	private String userCel; // 주문자핸드폰번호
	
	@Column(name = "USER_EMAIL")
	private String userEmail; // 주문자이메일주소
	
	@Column(name = "RECEIVE_TEL")
	private String receiveTel; // 수취인전화번호
	
	@Column(name = "RECEIVE_CEL")
	private String receiveCel; // 수취인핸드폰번호
	
	@Column(name = "RECEIVE_EMAIL")
	private String receiveEmail; // 수취인이메일주소
	
	@Column(name = "DELV_MSG")
	private String delvMsg; // 배송메세지 : 쇼핑몰에서 수집된 배송메세지 + 수집된 주문의 기타 메시지
	
	@Column(name = "RECEIVE_NAME")
	private String receiveName; // 수취인명
	
	@Column(name = "RECEIVE_ZIPCODE")
	private String receiveZipcode; // 수취인우편번호
	
	@Column(name = "RECEIVE_ADDR")
	private String receiveAddr; // 수취인주소
	
	@Column(name = "TOTAL_COST")
	private BigDecimal totalCost; // 주문금액
	
	@Column(name = "PAY_COST")
	private BigDecimal payCost; // 결제금액
	
	@Column(name = "ORDER_DATE")
	private String orderDate; // 주문일자
	
	@Column(name = "PARTNER_ID")
	private String partnerId; // 매입처명
	
	@Column(name = "DPARTNER_ID")
	private String dpartnerId; // 물류처명
	
	@Column(name = "MALL_PRODUCT_ID")
	private String mallProductId; // 상품코드(쇼핑몰)
	
	@Column(name = "PRODUCT_ID")
	private String productId; // 	품번코드(사방넷)
	
	@Column(name = "SKU_ID")
	private String skuId; // 단품코드(사방넷)
	
	@Column(name = "P_PRODUCT_NAME")
	private String pProductName; // 상품명(확정)
	
	@Column(name = "P_SKU_VALUE")
	private String pSkuValue; // 	옵션(확정)
	
	@Column(name = "PRODUCT_NAME")
	private String productName; // 	상품명(수집)
	
	@Column(name = "SALE_COST")
	private BigDecimal saleCost; // 판매가(수집)
	
	@Column(name = "MALL_WON_COST")
	private BigDecimal mallWonCost; // 공급단가	쇼핑몰에서 업체에게 정산해야할 상품 1개 금액
	
	@Column(name = "WON_COST")
	private BigDecimal wonCost; // 	원가	상품등록시 입력한 상품원가
	
	@Column(name = "SKU_VALUE")
	private String skuValue; // 옵션(수집)
	
	@Column(name = "SALE_CNT")
	private Double saleCnt; // 수량
	
	@Column(name = "DELIVERY_METHOD_STR")
	private String deliveryMethodStr; // 배송구분
	
	@Column(name = "DELV_COST")
	private BigDecimal delvCost; // 	배송비(수집)
	
	@Column(name = "COMPAYNY_GOODS_CD")
	private String companyGoodsCd; // 	자체상품코드	상품등록시에 입력한 자사 상품코드
	
	@Column(name = "SKU_ALIAS")
	private String skuAlias; // 옵션별칭	상품등록시에 옵션에 입력한 별칭
	
	@Column(name = "BOX_EA")
	private String boxEa; // 	EA(상품)	상품등록시에 옵션에 입력한 묶음 개수
	
	@Column(name = "JUNG_CHK_YN")
	private String jungChkYn; // 정산대조확인여부
	
	@Column(name = "MALL_ORDER_SEQ")
	private String mallOrderSeq; // 주문순번
	
	@Column(name = "MALL_ORDER_ID")
	private String mallOrderId; // 부주문번호
	
	@Column(name = "ETC_FIELD3")
	private String etcField3; // 수정된 수집옵션명
	
	@Column(name = "ORDER_GUBUN")
	private String orderGubun; // 주문구분
	
	@Column(name = "P_EA")
	private String pea; // EA(확정)
	
	@Column(name = "REG_DATE")
	private String regDate; // 수집일자
	
	@Column(name = "ORD_FIELD2")
	private String ordField2; // 세트분리주문구분
	
	@Column(name = "COPY_IDX")
	private String copyIdx; // 원주문번호(사방넷)
	
	@Column(name = "GOODS_NM_PR")
	private String goodsNmPr; // 출력상품명
	
	@Column(name = "GOODS_KEYWORD")
	private String goodsKeyword; // 상품약어
	
	@Column(name = "ORD_CONFIRM_DATE")
	private String ordConfirmDate; // 주문 확인일자
	
	@Column(name = "RTN_DT")
	private String rtnDt; // 반품 완료일자
	
	@Column(name = "CHNG_DT")
	private String chngDt; // 교환 완료일자
	
	@Column(name = "DELIVERY_CONFIRM_DATE")
	private String deliveryConfirmDate; // 	출고 완료일자
	
	@Column(name = "CANCEL_DT")
	private String cancelDt; // 취소 완료일자
	
	@Column(name = "CLASS_CD1")
	private String classCd1; // 대분류코드
	
	@Column(name = "CLASS_CD2")
	private String classCd2; // 중분류코드
	
	@Column(name = "CLASS_CD3")
	private String classCd3; // 소분류코드
	
	@Column(name = "CLASS_CD4")
	private String classCd4; // 세분류코드
	
	@Column(name = "BRAND_NM")
	private String brandNm; // 브랜드명
	
	@Column(name = "DELIVERY_ID")
	private String deliveryId; // 택배사코드
	
	@Column(name = "INVOICE_NO")
	private String invoiceNo; // 송장번호
	
	@Column(name = "HOPE_DELV_DATE")
	private String hopeDelvDate; // 배송희망일자
	
	@Column(name = "FLD_DSP")
	private String fldDsp; // 주문엑셀용
	
	@Column(name = "INV_SEND_MSG")
	private String invSendMsg; // 운송장 전송 결과 메시지
	
	@Column(name = "MODEL_NO")
	private String modelNo; // 모델NO
	
	@Column(name = "SET_GUBUN")
	private String setGubun; // 상품구분
	
	@Column(name = "ETC_MSG")
	private String etcMsg; // 기타메세지
	
	@Column(name = "DELV_MSG1")
	private String delvMsg1; // 배송메세지
	
	@Column(name = "MUL_DELV_MSG")
	private String mulDelvMsg; // 물류메세지
	
	@Column(name = "BARCODE")
	private String barcode; // 바코드
	
	@Column(name = "INV_SEND_DM")
	private String invSendDm; // 송장전송일자
	
	@Column(name = "DELIVERY_METHOD_STR2")
	private String deliveryMethodStr2; // 배송구분(배송비반영)
	
	@Column(name = "FREE_GIFT")
	private String freeGift; // 사은품
	
	@Column(name = "ACNT_REGS_SRNO")
	private String acntRegsSrno; // 계정등록순번

	@Column(name = "MODEL_NAME")
	private String modelName;

	@Column(name = "STATUS")
	private String status;
}
