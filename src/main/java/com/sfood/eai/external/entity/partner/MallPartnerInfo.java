package com.sfood.eai.external.entity.partner;

import java.util.Date;

import com.sfood.eai.external.entity.id.partner.MallPartnerInfoId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="TBL_MALL_PARTNER_INFO")
@IdClass(MallPartnerInfoId.class)
public class MallPartnerInfo {
	@Id
	@Column(name = "COMPANY_CODE")
	private String companyCode; // 회사코드
	
	@Id
	@Column(name = "MALL_CODE")
	private String mallCode; // 연동몰코드
	
	@Id
	@Column(name = "IN_MALL_CODE")
	private String inMallCode; // 연동거래처코드
	
	@Id
	@Column(name = "PARTNER_CODE")
	private String partnerCode; // 거래처코드
	
	@Id
	@Column(name = "STORE_CODE")
	private String storeCode; // 점포코드
	
	@Column(name = "IN_MALL_NAME")
	private String inMallName; // 연동거래처명
	
	@Column(name = "STORE_NAME")
	private String storeName; // 점포명
	
	@Column(name = "SALES_TYPE")
	private String salesType; // 판매구분코드
	
	@Column(name = "EXT1")
	private String ext1; // 기타1
	
	@Column(name = "CREATE_ID")
	private String createId;
	
	@Column(name = "CREATE_DATE")
	private Date createDate;
	
	@Column(name = "UPDATE_ID")
	private String updateId;
	
	@Column(name = "UPDATE_DATE")
	private Date updateDate;
}
