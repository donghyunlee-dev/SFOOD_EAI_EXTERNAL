package com.sfood.eai.external.repository.partner;

import java.util.List;

import com.sfood.eai.external.entity.id.partner.PartnerRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sfood.eai.external.entity.partner.PartnerRole;

@Repository
public interface PartnerRoleRepository extends JpaRepository <PartnerRole, PartnerRoleId> {

	List<PartnerRole> findByCompanyCodeAndPartnerCode(String companyCode, String partnerCode);

}
