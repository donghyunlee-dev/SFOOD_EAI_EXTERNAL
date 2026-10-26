package com.sfood.eai.external.entity;

import com.sfood.eai.external.entity.id.ApiUrlId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name="TBL_API_URL_MST")
@IdClass(ApiUrlId.class)
public class ApiUrl {
	
	@Id
	@Column(name = "COMPANY_CODE")
	private String companyCode ; // 회사코드
	
	@Id
	@Column(name = "MALL_CODE")
	private String mallCode; // 몰코드
	
	@Id
	@Column(name = "REQUEST_URI")
	private String requestUri; // 요청uri
	
	@Column(name = "REQUEST_DESCRIPTION")
	private String requestDescription; // 요청uri설명
	
	@Column(name = "API_URI")
	private String apiUri; // API 연동 URI
	
	@Column(name = "SUB_INFO1")
	private String subInfo1; // 참조정보1
	
	@Column(name = "SUB_INFO2")
	private String subInfo2; // 참조정보2
	
	@Column(name = "CREATE_ID")
	private String createId;
	
	@Column(name = "CREATE_DATE")
	private Date createDate;
	
	@Column(name = "UPDATE_ID")
	private String updateId;
	
	@Column(name = "UPDATE_DATE")
	private Date updateDate;

}
