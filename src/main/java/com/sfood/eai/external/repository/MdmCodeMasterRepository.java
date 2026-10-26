package com.sfood.eai.external.repository;

import com.sfood.eai.external.entity.MdmCodeMaster;
import com.sfood.eai.external.entity.id.MdmCodeMstId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MdmCodeMasterRepository extends JpaRepository<MdmCodeMaster, MdmCodeMstId> {
}
