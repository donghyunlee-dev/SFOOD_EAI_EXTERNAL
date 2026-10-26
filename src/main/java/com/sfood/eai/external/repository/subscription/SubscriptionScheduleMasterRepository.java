package com.sfood.eai.external.repository.subscription;

import com.sfood.eai.external.entity.id.subscription.SubscriptionScheduleMasterId;
import com.sfood.eai.external.entity.subscription.SubscriptionScheduleMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionScheduleMasterRepository extends JpaRepository<SubscriptionScheduleMaster, SubscriptionScheduleMasterId> {

    Optional<SubscriptionScheduleMaster> findByCompanyCodeAndScheduleCodeAndUseYn(String companyCode, String scheduleCode, String useYn);

    Optional<SubscriptionScheduleMaster> findByCompanyCodeAndGoodsCodeAndUseYn(String companyCode, String goodsCode, String useYn);
}
