package com.sfood.eai.external.entity.id.pricing;

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
public class PartnerGroupId {

    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Column(name = "PARTNER_GROUP_CODE")
    private String partnerGroupCode;
}
