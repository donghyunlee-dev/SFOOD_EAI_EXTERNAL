package com.sfood.eai.external.entity.id;

import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiUrlId implements Serializable {

	private static final long serialVersionUID = 1L;

	private String companyCode ; 	// 법인코드
	private String mallCode; 		// 몰코드
	private String requestUri; 		// 요청uri
}
