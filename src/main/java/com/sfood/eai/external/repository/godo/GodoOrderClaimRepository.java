package com.sfood.eai.external.repository.godo;

import com.sfood.eai.external.entity.godo.GodoOrderClaim;
import com.sfood.eai.external.entity.id.godo.GodoOrderClaimId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GodoOrderClaimRepository extends JpaRepository <GodoOrderClaim, GodoOrderClaimId> {
    List<GodoOrderClaim> findByOrderNo(String orderNo);
}
