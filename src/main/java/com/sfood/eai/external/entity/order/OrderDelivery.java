package com.sfood.eai.external.entity.order;

import com.sfood.eai.external.entity.id.order.OrderGoodsId;
import com.sfood.eai.external.util.CustomDateUtils;
import com.sfood.eai.external.util.CustomStringUtils;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 주문배송정보
 */
@EqualsAndHashCode(callSuper=false)
@Data
@IdClass(OrderGoodsId.class)
@Entity
@Table(name="TBL_ORDER_DELIVERY")
public class OrderDelivery extends BaseEntity {
	@Id
	@Column(name = "COMPANY_CODE")
	private String companyCode ; // 회사코드
	
	@Id
	@Column(name = "MARKET_MALL_CODE")
	private String marketMallCode; // 연동몰코드
	
	@Id
	@Column(name = "ORDER_MERGE_NO")
	private String orderMergeNo ; // 주문통합번호
	
	@Id
	@Column(name = "ORDER_DETAIL_SEQ")
	private Integer orderDetailSeq; // 주문상세번호
	
	@Column(name = "DELV_TYPE_CODE")
	private String delvTypeCode; // 배송유형코드(주문 데이터 정의.xlsx : 배송유형코드 ex : 택배, 직배)
	
	@Column(name = "HOPE_DELV_DATE")
	private LocalDateTime hopeDelvDate; // 배송희망일(주문 데이터 정의.xlsx : 배송희망일, 유의어 : 배송요청일, 납기요청일...)
	
	@Column(name = "START_DELV_DATE")
	private LocalDateTime startDelvDate; // 출고일(wms에서 입력)
	
	@Column(name = "END_DELV_DATE")
	private LocalDateTime endDelvDate; // 납기일(주문 데이터 정의.xlsx : 배송완료일)
	
	@Column(name = "DELV_MSG")
	private String delvMsg; // 배송메세지(주문 데이터 정의.xlsx : 배송메세지)
	
	@Column(name = "DELV_MSG2")
	private String delvMsg2; // 배송메세지2(주문 데이터 정의.xlsx : 배송메세지2)
	
	@Column(name = "INVOICE_COMPANY_SNO")
	private String invoiceCompanySno; // 택배사 번호(주문 데이터 정의.xlsx : 택배사, 사방넷에는 없음)
	
	@Column(name = "INVOICE_COMPANY")
	private String invoiceCompany; // 택배사명(주문 데이터 정의.xlsx : 택배사, 사방넷에는 없음)

	@Column(name = "ORDER_USER_NAME")
	private String orderUserName; // 주문자명(주문 데이터 정의.xlsx : 주문자 정보 > 주문자명)

	@Column(name = "ORDER_USER_CEL")
	private String orderUserCel; // 주문자 휴대폰번호(주문 데이터 정의.xlsx : 주문자 정보 > 휴대폰번호)

	@Column(name = "ORDER_USER_ADDRESS")
	private String orderUserAddress; // 주문자 주소(주문 데이터 정의.xlsx : 주문자 정보 > 주소)

	@Column(name = "ORDER_USER_ADDRESS_SUB")
	private String orderUserAddressSub; // 주문자 주소(주문 데이터 정의.xlsx : 주문자 정보 > 주소)

	@Column(name = "ORDER_USER_ZIPCODE")
	private String orderUserZipcode; // 주문자 우편번호(주문 데이터 정의.xlsx : 없으나 필요할 것 같아서 넣어둠)

	@Column(name = "RECEIVE_NAME")
	private String receiveName; // 수취인명(주문 데이터 정의.xlsx : 수취인 정보 > 수취인명)
	
	@Column(name = "RECEIVE_TEL")
	private String receiveTel; // 수취인 전화번호(주문 데이터 정의.xlsx : 수취인 정보 > 전화번호)
	
	@Column(name = "RECEIVE_CEL")
	private String receiveCel; // 수취인 휴대폰번호(주문 데이터 정의.xlsx : 수취인 정보 > 휴대폰번호)
	
	@Column(name = "RECEIVE_EMAIL")
	private String receiveEmail; // 수취인이메일주소(주문 데이터 정의.xlsx : 없으나 있어야 할 것 같아 넣어둠)
	
	@Column(name = "RECEIVE_ADDR")
	private String receiveAddr; // 수취인주소(주문 데이터 정의.xlsx : 수취인 정보 > 수취인주소)

	@Column(name = "RECEIVE_ADDR2")
	private String receiveAddr2; // 수취인주소(주문 데이터 정의.xlsx : 수취인 정보 > 수취인주소)
	
	@Column(name = "RECEIVE_ZIPCODE")
	private String receiveZipcode; // 수취인우편번호(주문 데이터 정의.xlsx : 없으나 있어야 할 것 같아 넣어둠)

	@Column(name = "RETURN_REQ_DATE")
	private LocalDate returnReqDate;// 반품요청일자

	@Transient
	private String deliveryReqDate;// 배송요청일자

	@Transient
	private boolean isChanged;

	public void setOrderDeliveryId(String orderNo, int seqNo) {
		this.orderMergeNo = orderNo;
		this.orderDetailSeq = seqNo;
	}

	public void update(OrderDelivery updateDelivery) {

		this.isChanged = false;

		if (CustomStringUtils.notEqualsIgnoreNullOrEmpty(this.delvMsg, updateDelivery.getDelvMsg())) {
			this.delvMsg = updateDelivery.getDelvMsg();
			this.isChanged = true;
		}

		if (CustomStringUtils.notEqualsIgnoreNullOrEmpty(this.delvMsg2, updateDelivery.getDelvMsg2())) {
			this.delvMsg2 = updateDelivery.getDelvMsg2();
			this.isChanged = true;
		}

		if (CustomStringUtils.notEqualsIgnoreNullOrEmpty(this.receiveName, updateDelivery.getReceiveName())) {
			this.receiveName = updateDelivery.getReceiveName();
			this.isChanged = true;
		}

		if (CustomStringUtils.notEqualsIgnoreNullOrEmpty(this.receiveTel, updateDelivery.getReceiveTel())) {
			this.receiveTel = updateDelivery.getReceiveTel();
			this.isChanged = true;
		}

		if (CustomStringUtils.notEqualsIgnoreNullOrEmpty(this.receiveCel, updateDelivery.getReceiveCel())) {
			this.receiveCel = updateDelivery.getReceiveCel();
			this.isChanged = true;
		}

		if (CustomStringUtils.notEqualsIgnoreNullOrEmpty(this.receiveEmail, updateDelivery.getReceiveEmail())) {
			this.receiveEmail = updateDelivery.getReceiveEmail();
			this.isChanged = true;
		}

		if (CustomStringUtils.notEqualsIgnoreNullOrEmpty(this.receiveAddr, updateDelivery.getReceiveAddr())) {
			this.receiveAddr = updateDelivery.getReceiveAddr();
			this.isChanged = true;
		}

		if (CustomStringUtils.notEqualsIgnoreNullOrEmpty(this.receiveAddr2, updateDelivery.getReceiveAddr2())) {
			this.receiveAddr2 = updateDelivery.getReceiveAddr2();
			this.isChanged = true;
		}

		if (CustomStringUtils.notEqualsIgnoreNullOrEmpty(this.receiveZipcode, updateDelivery.getReceiveZipcode())) {
			this.receiveZipcode = updateDelivery.getReceiveZipcode();
			this.isChanged = true;
		}

		// 신규주문에서 주문확정으로 변경될 때 배송요청일을 계산하기 때문에 넣었던 기능이지만 deliveryReqDate null 값으로 반복 업데이트 되어 주석처리함.
		/*if (CustomStringUtils.notEqualsIgnoreNullOrEmpty(this.deliveryReqDate, updateDelivery.getDeliveryReqDate())) {
			this.deliveryReqDate = updateDelivery.getDeliveryReqDate();
			this.isChanged = true;
		}*/

		if (isChanged) {
			super.setUpdateId(updateDelivery.getUpdateId());
		}
	}

	public void updateDeliveryDate(String hopeDelvDate) {

		LocalDateTime deliveryDate = CustomDateUtils.parseLocalDateTime(hopeDelvDate);

		if (ObjectUtils.isEmpty(this.hopeDelvDate) || this.hopeDelvDate.compareTo(deliveryDate) < 0) {
			this.hopeDelvDate = deliveryDate;
		}
	}
}
