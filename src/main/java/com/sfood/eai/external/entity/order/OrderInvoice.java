package com.sfood.eai.external.entity.order;

import java.util.Date;

import com.sfood.eai.external.entity.id.order.OrderInvoiceId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name="TBL_ORDER_INVOICE")
@IdClass(OrderInvoiceId.class)
public class OrderInvoice extends BaseEntity {
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
	
	@Id
	@Column(name = "INVOICE_NO")
	private String invoiceNo; // 송장번호(주문 데이터 정의.xlsx : 송장번호)
	
	@Column(name = "INVOICE_SEND_DATE")
	private Date invoiceSendDate;// 송장등록일시(사방넷 : 송장전송일자, 고도몰 : 송장번호 등록일)
	
	@Column(name = "DELIVERY_CONFIRM_DATE")
	private Date deliveryConfirmDate; // 배송확인일시(사방넷 : 출고 완료일자, 고도몰 : 배송일자)
	
	@Column(name = "DELIVERY_COMPLETE_DATE") // *** 배송준비중일시, 배송중일시는 무엇임? API 어느곳에도 없음
	private Date deliveryCompleteDt; // 배송완료일시(주문 데이터 정의.xlsx : 배송완료일시, 사방넷에는 없음, 고도몰 : 배송완료일자 )
	
	@Column(name = "WMS_UPDATE_DATE") // 
	private Date wmsUpdateDate; // wms업데이트일시
}
