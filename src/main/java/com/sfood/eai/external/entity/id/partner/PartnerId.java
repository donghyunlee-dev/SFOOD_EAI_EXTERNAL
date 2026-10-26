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
public class PartnerId implements Serializable {

    public static final long serialVersionUID = 1L;

    private String companyCode ; // 회사코드
    private String partnerCode ; // 거래처코드
}
