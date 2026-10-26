package com.sfood.eai.external.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class GwApprovalId {

    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Column(name = "DOCUMENT_REQ_NO")
    private String documentReqNo;

    @Column(name = "DOCUMENT_TYPE")
    private String documentType;
}
