package com.sfood.eai.external.repository.partner;

import com.sfood.eai.external.entity.id.partner.PartnerId;
import com.sfood.eai.external.entity.partner.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerMasterRepository extends JpaRepository<Partner, PartnerId> {
}
