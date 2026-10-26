package com.sfood.eai.external.entity.id.godo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GodoBaseId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private String companyCode;
    private String marketMallCode;
    private String orderNo ; // 주문통합번호
}
