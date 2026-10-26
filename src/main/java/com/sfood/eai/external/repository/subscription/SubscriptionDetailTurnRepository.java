package com.sfood.eai.external.repository.subscription;

import com.sfood.eai.external.entity.id.subscription.SubscriptionDetailTurnId;
import com.sfood.eai.external.entity.subscription.SubscriptionDetailTurn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionDetailTurnRepository extends JpaRepository<SubscriptionDetailTurn, SubscriptionDetailTurnId> {
    List<SubscriptionDetailTurn> findByCompanyCodeAndScheduleCode(String companyCode, String scheduleCode);
}
