package com.sfood.eai.external.repository.partner;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sfood.eai.external.entity.partner.MallPartnerInfo;
import com.sfood.eai.external.entity.id.partner.MallPartnerInfoId;

@Repository
public interface MallPartnerInfoRepository extends JpaRepository <MallPartnerInfo, MallPartnerInfoId> {

	List<MallPartnerInfo> findByCompanyCodeAndMallCode(String companyCode, String mallCode);

	MallPartnerInfo findByCompanyCodeAndInMallCode(String companyCode, String inMallCode);

	Optional<MallPartnerInfo> findByCompanyCodeAndMallCodeAndInMallCode(String companyCode, String mallCode, String inMallCode);

}
