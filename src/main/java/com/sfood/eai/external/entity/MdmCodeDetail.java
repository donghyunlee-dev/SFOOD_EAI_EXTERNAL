package com.sfood.eai.external.entity;

import com.sfood.eai.external.entity.id.MdmCodeDetailId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name="TBL_MDM_CODE_DETAIL")
@IdClass(MdmCodeDetailId.class)
public class MdmCodeDetail {
	
	@Id
	@Column(name = "MDM_DETAIL_CODE")
	private String mdmDetailCode ; // 공통코드
	
	@Id
	@Column(name = "COMPANY_CODE")
	private String companyCode ; // 법인코드
	
	@Id
	@Column(name = "MDM_GROUP_CODE")
	private String mdmGroupCode ; // 공통그룹코드
	
	@Column(name = "SUB_CODE1")
	private String subCode1; // 서브코드1
	
	@Column(name = "SUB_CODE2")
	private String subCode2; // 서브코드2
	
	@Column(name = "MDM_DETAIL_NAME")
	private String mdmDetailName; // 코드명
	
	@Column(name = "SORT_NO")
	private Integer sortNo; // 정렬순서
	
	@Column(name = "USE_YN")
	private String useYn; // 사용여부
	
	@Column(name = "CREATE_ID")
	private String createId;
	
	@Column(name = "CREATE_DATE")
	private Date createDate;
	
	@Column(name = "UPDATE_ID")
	private String updateId;
	
	@Column(name = "UPDATE_DATE")
	private Date updateDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "MDM_GROUP_CODE"),
			@JoinColumn(name = "COMPANY_CODE")
	})
	private MdmCodeMaster mdmCodeMaster;

}
