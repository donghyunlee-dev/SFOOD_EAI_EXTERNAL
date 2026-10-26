package com.sfood.eai.external.service;

import com.sfood.eai.external.dto.order.DeliverySchedule;
import com.sfood.eai.external.entity.id.subscription.SubscriptionScheduleMasterId;
import com.sfood.eai.external.entity.subscription.SubscriptionDetailCalendar;
import com.sfood.eai.external.entity.subscription.SubscriptionDetailTurn;
import com.sfood.eai.external.entity.subscription.SubscriptionScheduleMaster;

import java.util.List;

public interface SubscriptionService {

    SubscriptionScheduleMaster getSchedule(SubscriptionScheduleMasterId id);

    List<SubscriptionDetailCalendar> findDetailCalendars(DeliverySchedule deliverySchedule);

    List<SubscriptionDetailTurn> findDetailTurns(DeliverySchedule deliverySchedule);

    DeliverySchedule getNewDeliverySchedule(SubscriptionScheduleMaster scheduleMaster);

    DeliverySchedule getNewDeliverySchedule(String companyCode, String goodsCode);

    List<String> findGoodsByScheduleType(DeliverySchedule deliverySchedule);
}
