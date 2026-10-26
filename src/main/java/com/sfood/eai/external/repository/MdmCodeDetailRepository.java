package com.sfood.eai.external.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sfood.eai.external.entity.MdmCodeDetail;
import com.sfood.eai.external.entity.id.MdmCodeDetailId;

@Repository
public interface MdmCodeDetailRepository extends JpaRepository <MdmCodeDetail, MdmCodeDetailId> {
	List<MdmCodeDetail> findByCompanyCodeAndMdmGroupCode(String companyCode, String sabangMatchCode);
}
