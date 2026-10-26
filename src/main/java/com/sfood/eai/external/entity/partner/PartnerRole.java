package com.sfood.eai.external.entity.partner;

import java.util.Date;

import com.sfood.eai.external.entity.id.partner.PartnerRoleId;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="TBL_PARTNER_ROLE")
@IdClass(PartnerRoleId.class)
public class PartnerRole {
	@Id
	@Column(name = "COMPANY_CODE")
	private String companyCode;
	
	@Id
	@Column(name = "PARTNER_CODE")
	private String partnerCode;
	
	@Id
	@Column(name = "ROLE_CODE")
	private String roleCode;
	
	@Id
	@Column(name = "ROLE_PARTNER_CODE")
	private String rolePartnerCode;
	
	@Column(name = "DEFAULT_IN_ROLE_YN")
	private String defaultInRoleYn;
	
	@Column(name = "USE_YN")
	private String useYn;
	
	@Column(name = "CREATE_ID")
	private String createId;
	
	@Column(name = "CREATE_DATE")
	private Date createDate;

	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "COMPANY_CODE", referencedColumnName = "COMPANY_CODE"),
			@JoinColumn(name = "PARTNER_CODE", referencedColumnName = "PARTNER_CODE")
	})
	private Partner partner;
}
