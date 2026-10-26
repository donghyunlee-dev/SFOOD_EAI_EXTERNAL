package com.sfood.eai.external.repository.sabang;

import com.sfood.eai.external.entity.id.sabang.SabangOrderId;
import com.sfood.eai.external.entity.sabang.SabangClaim;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SabangClaimRepository extends JpaRepository<SabangClaim, SabangOrderId> {
}
