package com.sfood.eai.external.entity.id;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MdmCodeDetailId implements Serializable {

	private static final long serialVersionUID = 1L;

	private String mdmDetailCode ; 	// 공통코드
	private String companyCode ; 	// 법인코드
	private String mdmGroupCode ; 	// 공통그룹코드
}
