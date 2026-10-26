package com.sfood.eai.external.repository.subscription;

import com.sfood.eai.external.entity.id.subscription.SubscriptionDetailCalendarId;
import com.sfood.eai.external.entity.subscription.SubscriptionDetailCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SubscriptionDetailCalendarRepository extends JpaRepository<SubscriptionDetailCalendar, SubscriptionDetailCalendarId> {
    List<SubscriptionDetailCalendar> findByCompanyCodeAndScheduleCodeAndScheduleDateBetween(String companyCode, String scheduleCode, String firstDeliveryDate, String lastScheduleDate);
}
