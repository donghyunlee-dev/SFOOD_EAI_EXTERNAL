package com.sfood.eai.external.dto.godo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GodoOrderGoodsData {	
	
	@JacksonXmlProperty(localName = "sno")
	private Integer sno;	// INTEGER 주문상품 고유번호
	
	@JacksonXmlProperty(localName = "orderNo")
	private String orderNo; // INTEGER 주문번호
	
	@JacksonXmlProperty(localName = "orderCd")
	private Integer orderCd; // INTEGER 주문순서
	
	@JacksonXmlProperty(localName = "orderGroupCd")
	private String orderGroupCd; //	INTEGER	수량 부분취소시 그룹번호
	
	@JacksonXmlProperty(localName = "orderStatus")
	private String orderStatus; // STRING 주문상태 코드 코드표 참조
	
	@JacksonXmlProperty(localName = "invoiceCompanySno")
	private Integer invoiceCompanySno; // INTEGER 택배사 번호
	
	@JacksonXmlProperty(localName = "invoiceCompany")
	private String invoiceCompany; // STRING	택배사명
	
	@JacksonXmlProperty(localName = "invoiceNo")
	private String invoiceNo; // STRING	송장번호
	
	@JacksonXmlProperty(localName = "scmNo")
	private String scmNo; // INTEGER	공급사 번호
	
	@JacksonXmlProperty(localName = "commission")
	private String commission; // FLOAT	공급사 수수료율
	
	@JacksonXmlProperty(localName = "goodsNo")
	private String goodsNo; // INTEGER	상품번호
	
	@JacksonXmlProperty(localName = "goodsCd")
	private String goodsCd; // INTEGER	상품코드
	
	@JacksonXmlProperty(localName = "listImageData")
	private String listImageData; // STRING	상품 썸네일 이미지 URL
	
	@JacksonXmlProperty(localName = "goodsModelNo")
	private String goodsModelNo; //	STRING	모델명
	
	@JacksonXmlProperty(localName = "goodsNm")
	private String goodsNm; // STRING	상품명
	
	@JacksonXmlProperty(localName = "goodsCnt")
	private String goodsCnt; //	INTEGER	구매수량
	
	@JacksonXmlProperty(localName = "goodsPrice")
	private Double goodsPrice; // FLOAT	상품가격
	
	@JacksonXmlProperty(localName = "divisionUseMileage")
	private Double divisionUseMileage; // FLOAT	주문할인 금액의 안분된 마일리지
	
	@JacksonXmlProperty(localName = "divisionGoodsDeliveryUseDeposit")
	private Double divisionGoodsDeliveryUseDeposit; // FLOAT	주문할인 금액의 안분된 배송비 에치금
	
	@JacksonXmlProperty(localName = "divisionGoodsDeliveryUseMileage")
	private Double divisionGoodsDeliveryUseMileage; // FLOAT	주문할인 금액의 안분된 배송비 마일리지
	
	@JacksonXmlProperty(localName = "divisionCouponOrderDcPrice")
	private Double divisionCouponOrderDcPrice; // FLOAT	주문할인 금액의 안분된 주문쿠폰
	
	@JacksonXmlProperty(localName = "divisionUseDeposit")
	private Double divisionUseDeposit; // FLOAT	주문할인 금액의 안분된 예치금
	
	@JacksonXmlProperty(localName = "divisionCouponOrderMileage")
	private Double divisionCouponOrderMileage; // FLOAT	주문에 지급될 마일리지의 안분된 마일리지
	
	@JacksonXmlProperty(localName = "addGoodsPrice")
	private Double addGoodsPrice; // FLOAT	추가상품금액
	
	@JacksonXmlProperty(localName = "optionPrice")
	private Double optionPrice; // FLOAT	옵션금액
	
	@JacksonXmlProperty(localName = "optionTextPrice")
	private Double optionTextPrice; // FLOAT	입력옵션금액
	
	@JacksonXmlProperty(localName = "fixedPrice")
	private Double fixedPrice; // FLOAT	정가
	
	@JacksonXmlProperty(localName = "costPrice")
	private Double costPrice; // FLOAT	매입가
	
	@JacksonXmlProperty(localName = "goodsDcPrice")
	private Double goodsDcPrice; //	FLOAT	쿠폰할인금액
	
	@JacksonXmlProperty(localName = "memberDcPrice")
	private Double memberDcPrice; // FLOAT	회원할인금액(추가상품제외)
	
	@JacksonXmlProperty(localName = "memberOverlapDcPrice")
	private Double memberOverlapDcPrice; //	FLOAT	회원그룹 중복할인 금액(추가상품제외)
	
	@JacksonXmlProperty(localName = "couponGoodsDcPrice")
	private Double couponGoodsDcPrice; // FLOAT	상품쿠폰 할인 금액
	
	@JacksonXmlProperty(localName = "goodsMileage")
	private Double goodsMileage; //	FLOAT	적립마일리지(추가상품제외)
	
	@JacksonXmlProperty(localName = "memberMileage")
	private Double memberMileage; // FLOAT	회원 적립마일리지(추가상품제외)
	
	@JacksonXmlProperty(localName = "couponGoodsMileage")
	private Double couponGoodsMileage; // FLOAT	상품쿠폰 적립 마일리지(추가상품 제외)
	
	@JacksonXmlProperty(localName = "minusDepositFl")
	private String minusDepositFl; // STRING	사용 예치금 차감여부 코드표 참조
	
	@JacksonXmlProperty(localName = "minusRestoreDepositFl")
	private String minusRestoreDepositFl; // STRING	사용 예치금 복원여부 코드표 참조
	
	@JacksonXmlProperty(localName = "minusMileageFl")
	private String minusMileageFl; // STRING	사용 마일리지 차감 여부 코드표 참조
	
	@JacksonXmlProperty(localName = "minusRestoreMileageFl")
	private String minusRestoreMileageFl; // STRING	사용 마일리지 복원여부 코드표 참조
	
	@JacksonXmlProperty(localName = "plusMileageFl")
	private String plusMileageFl; // STRING	적립 마일리지 차감 여부 코드표 참조
	
	@JacksonXmlProperty(localName = "plusRestoreMileageFl")
	private String plusRestoreMileageFl; //	STRING	적립 마일리지 복원여부 코드표 참조
	
	@JacksonXmlProperty(localName = "minusStockFl")
	private String minusStockFl; //	STRING	재고차감 여부 코드표 참조
	
	@JacksonXmlProperty(localName = "minusRestoreStockFl")
	private String minusRestoreStockFl; // STRING	재고복원 여부 코드표 참조
	
	@JacksonXmlProperty(localName = "optionSno")
	private Integer optionSno; // INTEGER	상품옵션 고유번호
	
	@JacksonXmlProperty(localName = "optionInfo")
	private String optionInfo; // STRING	옵션정보 (json 형식)

	@JacksonXmlProperty(localName = "optionTextInfo")
	private String optionTextInfo; // STRING	입력옵션정보 (json 형식)
	
	@JacksonXmlProperty(localName = "cancelDt")
	private String cancelDt; //	DATETIME	배송일자
	
	@JacksonXmlProperty(localName = "paymentDt")
	private String paymentDt; // DATETIME	입금일자
	
	@JacksonXmlProperty(localName = "invoiceDt")
	private String invoiceDt; // DATETIME	송장번호 등록일
	
	@JacksonXmlProperty(localName = "deliveryDt")
	private String deliveryDt; // DATETIME	배송일자
	
	@JacksonXmlProperty(localName = "deliveryCompleteDt")
	private String deliveryCompleteDt; // DATETIME	배송완료일자
	
	@JacksonXmlProperty(localName = "finishDt")
	private String finishDt; //	DATETIME	구매확정일자
	
	@JacksonXmlProperty(localName = "claimData")
	private GodoClaimData claimData; // 클레임 정보
	
	@JacksonXmlProperty(localName = "mallSno")
	private String mallSno; // INTEGER	상점번호 코드표 참조
	
	@JacksonXmlProperty(localName = "goodsNmStandard")
	private String goodsNmStandard; // STRING	기준몰 상품명
	
	@JacksonXmlProperty(localName = "myappDcPrice")
	private String myappDcPrice; //	FLOAT	마이앱 할인 금액 (추가상품 제외)
	
	@JacksonXmlProperty(localName = "apiOrderGoodsNo")
	private String apiOrderGoodsNo; // INTEGER	외부채널품목고유번호
	
	@JacksonXmlProperty(localName = "optionCostPrice")
	private String optionCostPrice; // FLOAT	옵션 매입가
	
	@JacksonXmlProperty(localName = "goodsDeliveryCollectPrice")
	private String goodsDeliveryCollectPrice; // FLOAT	상품별 착불시 발생된 배송비
	
	@JacksonXmlProperty(localName = "orderDeliverySno")
	private String orderDeliverySno; //	INTEGER	배송테이블 번호
	
	@JacksonXmlProperty(localName = "timeSalePrice")
	private String timeSalePrice; // FLOAT	타임세일 할인 금액 (상품에만 적용)
	
	@JacksonXmlProperty(localName = "goodsDeliveryCollectFl")
	private String goodsDeliveryCollectFl; // STRING	상품별배송비 결제방법 (pre - 선불, later - 착불)
	
	@JacksonXmlProperty(localName = "purchaseNo")
	private String purchaseNo; // INTEGER	매입처 고유번호
	
	@JacksonXmlProperty(localName = "brandBankSalePrice")
	private String brandBankSalePrice; // FLOAT	브랜드 무통장결제 세일 할인 금액 (상품에만 적용)
	
	@JacksonXmlProperty(localName = "cateAllCd")
	private String cateAllCd; // STRING	상품에 연결된 전체 카테고리 코드
	
	@JacksonXmlProperty(localName = "scmAdjustAfterNo")
	private String scmAdjustAfterNo; //	INTEGER	공급사 정산 후 환불의 정산 고유 번호
	
	@JacksonXmlProperty(localName = "hscode")
	private String hscode; // STRING	hscode
	
	@JacksonXmlProperty(localName = "goodsType")
	private String goodsType; // STRING	주문상품종류
	
	@JacksonXmlProperty(localName = "mileageGiveDt")
	private String mileageGiveDt; // DATE	마일리지 지급 유예에 따른 실 지급일
	
	@JacksonXmlProperty(localName = "timeSaleFl")
	private String timeSaleFl; // STRING	타임세일 구매 여부
	
	@JacksonXmlProperty(localName = "checkoutData")
	private String checkoutData; //	STRING	간편구매 추가데이터
	
	@JacksonXmlProperty(localName = "parentMustFl")
	private String parentMustFl; //	STRING	추가상품 종속성 여부
	
	@JacksonXmlProperty(localName = "statisticsOrderFl")
	private String statisticsOrderFl; // STRING	주문/매출 통계 처리 상태
	
	@JacksonXmlProperty(localName = "parentGoodsNo")
	private String parentGoodsNo; // INTEGER	부모상품의 상품코드
	
	@JacksonXmlProperty(localName = "statisticsGoodsFl")
	private String statisticsGoodsFl; // STRING	상품 통계 처리 상태
	
	@JacksonXmlProperty(localName = "sendSmsFl")
	private String sendSmsFl; // JSON	문자발송여부
	
	@JacksonXmlProperty(localName = "deliveryMethodFl")
	private String deliveryMethodFl; //	STRING	배송방식
	
	@JacksonXmlProperty(localName = "goodsDiscountInfo")
	private String goodsDiscountInfo; // STRING	주문당시상품할인정보
	
	@JacksonXmlProperty(localName = "goodsMileageAddInfo")
	private String goodsMileageAddInfo; // STRING	주문당시상품적립정보
	
	@JacksonXmlProperty(localName = "inflow")
	private String inflow; // STRING	외부 인입 플랫폼
	
	@JacksonXmlProperty(localName = "linkMainTheme")
	private String linkMainTheme; // STRING	메인 상품 진열에서 장바구니 담은 정보
	
	@JacksonXmlProperty(localName = "visitAddress")
	private String visitAddress; //	STRING	방문 수령지 주소
	
	@JacksonXmlProperty(localName = "goodsVolume")
	private String goodsVolume; // FLOAT	상품 용량
	
	@JacksonXmlProperty(localName = "couponMileageFl")
	private String couponMileageFl; // STRING	쿠폰으로 적립되는 마일리지 품목별 지급현황
	
	@JacksonXmlProperty(localName = "easypayScmReceiptFl")
	private String easypayScmReceiptFl; // STRING	이지페이 하위가맹점 등록여부
	
	@JacksonXmlProperty(localName = "deliveryScheduleFl")
	private String deliveryScheduleFl; // STRING	배송일정 사용여부
	
	@JacksonXmlProperty(localName = "enuri")
	private Double enuri; // INTEGER	에누리

}
