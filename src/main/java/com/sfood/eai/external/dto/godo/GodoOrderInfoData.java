package com.sfood.eai.external.dto.godo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GodoOrderInfoData {
	
	@JacksonXmlProperty(localName = "sno")
	private Integer sno; // INTEGER	배송지 정보 고유번호, ※ order_data > multiShippingFl 가 y (복수배송지 사용) 인 경우 변수 존재
	
	@JacksonXmlProperty(localName = "orderInfoCd")
	private Integer orderInfoCd; // INTEGER	배송지 정보 순번, ※ 기본값 : 1, 복수배송지인 경우 값이 증가함
	
	@JacksonXmlProperty(localName = "orderName")
	private String orderName; // STRING	주문자 이름
	
	@JacksonXmlProperty(localName = "orderEmail")
	private String orderEmail; // STRING	주문자 이메일
	
	@JacksonXmlProperty(localName = "orderPhone")
	private String orderPhone; // STRING	주문자 전화번호
	
	@JacksonXmlProperty(localName = "orderCellPhone")
	private String orderCellPhone; // STRING	주문자 핸드폰 번호
	
	@JacksonXmlProperty(localName = "orderZipcode")
	private String orderZipcode; //	STRING	주문자 우편번호
	
	@JacksonXmlProperty(localName = "orderZonecode")
	private String orderZonecode; // STRING	주문자 새우편번호
	
	@JacksonXmlProperty(localName = "orderAddress")
	private String orderAddress; //	STRING	주문자 주소
	
	@JacksonXmlProperty(localName = "orderAddressSub")
	private String orderAddressSub; // STRING	주문자 나머지 주소
	
	@JacksonXmlProperty(localName = "receiverName")
	private String receiverName; //	STRING	수취인 이름
	
	@JacksonXmlProperty(localName = "receiverPhone")
	private String receiverPhone; // STRING	수취인 전화번호
	
	@JacksonXmlProperty(localName = "receiverCellPhone")
	private String receiverCellPhone; // STRING	수취인 핸드폰 번호
	
	@JacksonXmlProperty(localName = "receiverZipcode")
	private String receiverZipcode; // STRING	수취인 우편번호
	
	@JacksonXmlProperty(localName = "receiverZonecode")
	private String receiverZonecode; //	STRING	수취인 새우편번호
	
	@JacksonXmlProperty(localName = "receiverAddress")
	private String receiverAddress; // STRING	수취인 주소
	
	@JacksonXmlProperty(localName = "receiverAddressSub")
	private String receiverAddressSub; // STRING	수취인 나머지 주소
	
	@JacksonXmlProperty(localName = "customIdNumber")
	private String customIdNumber; // STRING	개인통관고유번호
	
	@JacksonXmlProperty(localName = "orderMemo")
	private String orderMemo; // STRING	주문시 남기는글
	
	@JacksonXmlProperty(localName = "receiverUseSafeNumberFl")
	private String receiverUseSafeNumberFl; // STRING	안심번호 상태, n = 사용안함, y = 사용, c = 사용해지, w = 발급대기
	
	@JacksonXmlProperty(localName = "receiverSafeNumberDt")
	private String receiverSafeNumberDt; //	DATETIME	안심번호 발급일시
	
	@JacksonXmlProperty(localName = "receiverSafeNumber")
	private String receiverSafeNumber; // STRING	안심번호
	
	@JacksonXmlProperty(localName = "orderPhonePrefixCode")
	private String orderPhonePrefixCode; //	STRING	주문자 전화번호 국가코드
	
	@JacksonXmlProperty(localName = "orderPhonePrefix")
	private String orderPhonePrefix; //	INTEGER	주문자 전화번호 국가번호
	
	@JacksonXmlProperty(localName = "orderCellPhonePrefixCode")
	private String orderCellPhonePrefixCode; //	STRING	주문자 휴대폰 국가코드
	
	@JacksonXmlProperty(localName = "orderCellPhonePrefix")
	private String orderCellPhonePrefix; //	INTEGER	주문자 휴대폰 국가번호
	
	@JacksonXmlProperty(localName = "orderState")
	private String orderState; // STRING	주문자 주/지방/지역
	
	@JacksonXmlProperty(localName = "orderCity")
	private String orderCity; // STRING	주문자 도시
	
	@JacksonXmlProperty(localName = "receiverCountryCode")
	private String receiverCountryCode;	// STRING	수취인 국가 코드
	
	@JacksonXmlProperty(localName = "receiverPhonePrefixCode")
	private String receiverPhonePrefixCode;	// STRING	수취인 전화번호 국가코드
	
	@JacksonXmlProperty(localName = "receiverPhonePrefix")
	private String receiverPhonePrefix;	// INTEGER	수취인 전화번호 국가번호
	
	@JacksonXmlProperty(localName = "receiverCellPhonePrefixCode")
	private String receiverCellPhonePrefixCode; // STRING	수취인 휴대폰 국가코드
	
	@JacksonXmlProperty(localName = "receiverCellPhonePrefix")
	private String receiverCellPhonePrefix; // INTEGER	수취인 핸드폰 국가번호
	
	@JacksonXmlProperty(localName = "receiverCountry")
	private String receiverCountry; // STRING	수취인 국가
	
	@JacksonXmlProperty(localName = "receiverState")
	private String receiverState; // STRING	수취인 주/지방/지역
	
	@JacksonXmlProperty(localName = "receiverCity")
	private String receiverCity; //	STRING	수취인 도시
	
	@JacksonXmlProperty(localName = "deliveryVisit")
	private String deliveryVisit; // STRING	배송방법(y:방문배송, n:방문배송제외, a:방문배송포함)
	
	@JacksonXmlProperty(localName = "visitAddress")
	private String visitAddress; // STRING	방문 수령지 주소
	
	@JacksonXmlProperty(localName = "visitName")
	private String visitName; // STRING	방문자명
	
	@JacksonXmlProperty(localName = "visitPhone")
	private String visitPhone; // STRING	방문자연락처
	
	@JacksonXmlProperty(localName = "visitMemo")
	private String visitMemo; // STRING	방문수령 메모
	
	@JacksonXmlProperty(localName = "packetCode")
	private String packetCode; // STRING	묶음배송코드
	
	@JacksonXmlProperty(localName = "smsFl")
	private String smsFl; // STRING	SMS 수신동의 여부

}
