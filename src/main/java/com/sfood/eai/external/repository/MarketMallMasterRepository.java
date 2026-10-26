package com.sfood.eai.external.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sfood.eai.external.entity.MarketMallMaster;
import com.sfood.eai.external.entity.id.MarketMallMasterId;

import java.util.List;

@Repository
public interface MarketMallMasterRepository extends JpaRepository <MarketMallMaster, MarketMallMasterId> {

	@Query("select a from MarketMallMaster a where a.useYn = 'Y' and a.inMallUseYn = 'Y'")
	List<MarketMallMaster> findAllInUse();
}
