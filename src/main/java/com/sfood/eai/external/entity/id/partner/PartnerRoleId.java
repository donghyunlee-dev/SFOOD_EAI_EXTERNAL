package com.sfood.eai.external.entity.id.partner;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PartnerRoleId implements Serializable {

	private static final long serialVersionUID = 1L;

	private String companyCode;
	private String partnerCode;
	private String roleCode;
	private String rolePartnerCode;
}
