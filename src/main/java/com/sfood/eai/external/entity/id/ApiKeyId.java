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
public class ApiKeyId implements Serializable {

	private static final long serialVersionUID = 1L;

	private String companyCode ; 	// 법인코드
	private String mallCode; 		// 몰코드
	private String keyName; 		// 연동키이름

}
