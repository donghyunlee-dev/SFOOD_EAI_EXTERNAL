package com.sfood.eai.external.entity.pricing;

import com.sfood.eai.external.entity.id.pricing.PartnerGroupId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TBL_PARTNER_GROUP_MST")
@Entity
public class PartnerGroup {

    @EmbeddedId
    private PartnerGroupId id;

    @Column(name = "PARTNER_GROUP_NAME")
    private String partnerGroupName;
}
