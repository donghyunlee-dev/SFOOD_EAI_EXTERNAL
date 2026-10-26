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
public class MdmCodeMstId implements Serializable {

    public static final long serialVersionUID = 1L;

    private String mdmGroupCode;
    private String companyCode;
}
