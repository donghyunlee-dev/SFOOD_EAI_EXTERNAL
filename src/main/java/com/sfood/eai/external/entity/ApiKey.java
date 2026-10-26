package com.sfood.eai.external.entity;

import com.sfood.eai.external.entity.id.ApiKeyId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name="TBL_API_KEY_MST")
@IdClass(ApiKeyId.class)
public class ApiKey {
	
	@Id
	@Column(name = "COMPANY_CODE")
	private String companyCode ; // 회사코드
	
	@Id
	@Column(name = "MALL_CODE")
	private String mallCode; // 몰코드
	
	@Id
	@Column(name = "KEY_NAME")
	private String keyName; // 연동키이름
	
	@Column(name = "KEY_VALUE")
	private String keyValue; // 연동키값
	
	@Column(name = "DESCRIPTION")
	private String description; // 연동키설명
	
	@Column(name = "CREATE_ID")
	private String createId;
	
	@Column(name = "CREATE_DATE")
	private Date createDate;
	
	@Column(name = "UPDATE_ID")
	private String updateId;
	
	@Column(name = "UPDATE_DATE")
	private Date updateDate;

}
