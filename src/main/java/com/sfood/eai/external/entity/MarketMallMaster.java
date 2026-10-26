package com.sfood.eai.external.entity;

import com.sfood.eai.external.entity.id.MarketMallMasterId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name="TBL_MARKET_MALL_MST")
@IdClass(MarketMallMasterId.class)
public class MarketMallMaster {
	
	@Id
	@Column(name = "COMPANY_CODE")
	private String companyCode; // 회사코드
	
	@Id
	@Column(name = "CHANNEL_CODE")
	private String channelCode; // 채널코드 - 코드테이블에 채널명존재
	
	@Id
	@Column(name = "MALL_CODE")
	private String mallCode; // 연동몰코드
	
	@Column(name = "MALL_NAME")
	private String mallName; // 연동몰명
	
	@Column(name = "INTERFACE_METHOD")
	private String interfaceMethod; // 인터페이스방식	
	
	@Column(name = "DESCRIPTION")
	private String description; // 몰설명
	
	@Column(name = "MALL_URL")
	private String mallUrl; // 연동몰주소
	
	@Column(name = "API_URL")
	private String apiUrl; // 연동몰API주소

	@Column(name = "EXTERNAL_START_URI")
	private String externalStartUri;

	@Column(name = "USE_YN")
	private String useYn;

	@Column(name = "IN_MALL_USE_YN")
	private String inMallUseYn;

	@Column(name = "GOODS_REPLACE_AVAIL_YN")
	private String goodsReplaceAvailYn;

	@Column(name = "CREATE_ID")
	private String createId;
	
	@Column(name = "CREATE_DATE")
	private Date createDate;
	
	@Column(name = "UPDATE_ID")
	private String updateId;
	
	@Column(name = "UPDATE_DATE")
	private Date updateDate;

}
