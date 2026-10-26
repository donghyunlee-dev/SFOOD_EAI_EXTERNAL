package com.sfood.eai.external.entity.retail;

import com.sfood.eai.external.entity.id.retail.EdiOrderDataId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@IdClass(EdiOrderDataId.class)
@Table(name="TBL_EDI_ORDER_DATA")
public class EdiOrderData {
	@Id
	@Column(name = "COMPANY_CODE")
	private String companyCode;

	@Id
	@Column(name = "EDI_TYPE")
	private String ediType;

	@Id
	@Column(name = "MESG_NAME")
	private String mesgName; // 문서명, I/F 고유 문서명

	@Id
	@Column(name = "MESG_ID")
	private String mesgId; // 문서참조번호, EDI 메시지 고유 아이디, 주문번호

	@Id
	@Column(name = "MESG_FROM")
	private String mesgFrom; // 송신자ID, 기본 : 유통사 코드

	@Id
	@Column(name = "MESG_TO")
	private String mesgTo; // 수신자ID, 기본 : 각 유통사 VAN 접속 파트너사 ID

	@Id
	@Column(name = "MESG_NO")
	private String mesgNo;

	@Id
	@Column(name = "LINE_NO")
	private Integer lineNo;

	@Id
	@Column(name = "SO_DT")
	private String soDt; // 발주일시(발주날짜), 주문일자

	@Column(name = "DLVY_DT")
	private String dlvyDt;

	@Column(name = "PROC_DATE")
	private String procDate;

	@Column(name = "CUST_BP_CD")
	private String custBpCd;

	@Column(name = "CUST_BP_NM")
	private String custBpNm;

	@Column(name = "SOLD_TO_PARTY")
	private String soldToParty;

	@Column(name = "SOLD_TO_PARTY_NM")
	private String soldToPartyNm;

	@Column(name = "CUST_ITEM_CD")
	private String custItemCd;

	@Column(name = "CUST_ITEM_NM")
	private String custItemNm;

	@Column(name = "ITEM_CD")
	private String itemCd;

	@Column(name = "ITEM_NM")
	private String itemNm;

	@Column(name = "SO_UNIT")
	private String soUnit;

	@Column(name = "TRANS_RATE")
	private Integer transRate;

	@Column(name = "SO_PRICE")
	private BigDecimal soPrice;

	@Column(name = "CUST_SO_QTY")
	private Double custSoSqty;

	@Column(name = "SO_QTY")
	private Double soQty;

	@Column(name = "EDI_BP_CD")
	private String ediBpCd;

	@Column(name = "EDI_BP_NM")
	private String ediBpNm;

	@Column(name = "CENTER_NM")
	private String centerNm;

	@Column(name = "CUST_SO_DT")
	private String custSoDt;

	@Column(name = "SHIP_TO_PARTY")
	private String shipToParty;

	@Column(name = "SHIP_TO_PARTY_NM")
	private String shipToPartyNm;

	@Column(name = "E_CENTER_CD")
	private String eCenterCd;

	@Column(name = "E_CENTER_NM")
	private String eCenterNm;

	@Column(name = "CREATE_ID")
	private String createId;

	@Column(name = "CREATE_DATE")
	private LocalDateTime createDate;

	public EdiOrderDataId getId() {
		return new EdiOrderDataId(this.companyCode, this.ediType, this.lineNo, this.mesgFrom, this.mesgId, this.mesgName, this.mesgNo, this.mesgTo, this.soDt);
	}

	public record BranchGroupKey(String companyCode, String ediType, String mesgFrom, String mesgId, String mesgName, String mesgNo, String mesgTo, String soDt, String custBpCode) {}
}
