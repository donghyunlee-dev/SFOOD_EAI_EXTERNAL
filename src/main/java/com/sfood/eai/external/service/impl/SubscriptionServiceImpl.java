package com.sfood.eai.external.service.impl;

import com.sfood.eai.external.dto.order.DeliverySchedule;
import com.sfood.eai.external.entity.id.subscription.SubscriptionScheduleMasterId;
import com.sfood.eai.external.entity.subscription.SubscriptionDetailCalendar;
import com.sfood.eai.external.entity.subscription.SubscriptionDetailTurn;
import com.sfood.eai.external.entity.subscription.SubscriptionScheduleMaster;
import com.sfood.eai.external.repository.subscription.SubscriptionDetailCalendarRepository;
import com.sfood.eai.external.repository.subscription.SubscriptionDetailTurnRepository;
import com.sfood.eai.external.repository.subscription.SubscriptionScheduleMasterRepository;
import com.sfood.eai.external.service.SubscriptionService;
import com.sfood.eai.external.util.CustomDateUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.sfood.eai.external.dto.order.DeliverySchedule.ScheduleType.CALENDAR;

@RequiredArgsConstructor
@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionScheduleMasterRepository subscriptionScheduleMasterRepository;
    private final SubscriptionDetailCalendarRepository subscriptionDetailCalendarRepository;
    private final SubscriptionDetailTurnRepository subscriptionDetailTurnRepository;

    @Override
    public SubscriptionScheduleMaster getSchedule(SubscriptionScheduleMasterId id) {
        return subscriptionScheduleMasterRepository.findByCompanyCodeAndScheduleCodeAndUseYn(id.getCompanyCode(), id.getScheduleCode(), "Y").orElse(null);
    }

    @Override
    public List<SubscriptionDetailCalendar> findDetailCalendars(DeliverySchedule deliverySchedule) {
        return subscriptionDetailCalendarRepository.findByCompanyCodeAndScheduleCodeAndScheduleDateBetween(
                deliverySchedule.getCompanyCode()
                , deliverySchedule.getScheduleCode()
                , CustomDateUtils.formatDateString(deliverySchedule.getFirstDeliveryDate())
                , CustomDateUtils.formatDateString(deliverySchedule.getLastDeliveryDate())
        );
    }

    @Override
    public List<SubscriptionDetailTurn> findDetailTurns(DeliverySchedule deliverySchedule) {
        return subscriptionDetailTurnRepository.findByCompanyCodeAndScheduleCode(deliverySchedule.getCompanyCode(), deliverySchedule.getScheduleCode());
    }

    @Override
    public DeliverySchedule getNewDeliverySchedule(SubscriptionScheduleMaster scheduleMaster) {
        DeliverySchedule deliverySchedule = new DeliverySchedule();
        deliverySchedule.setCompanyCode(scheduleMaster.getCompanyCode());
        deliverySchedule.setScheduleCode(scheduleMaster.getScheduleCode());
        deliverySchedule.setScheduleName(scheduleMaster.getScheduleName());
        deliverySchedule.setScheduleType(DeliverySchedule.ScheduleType.valueOf(scheduleMaster.getScheduleType()));
        deliverySchedule.setWeek(scheduleMaster.getDeliveryPeriod());
        deliverySchedule.setDeliveryTerm(scheduleMaster.getDeliveryTerm());
        return deliverySchedule;
    }

    @Override
    public DeliverySchedule getNewDeliverySchedule(String companyCode, String scheduleCode) {

        SubscriptionScheduleMaster subscriptionSchedule = getSchedule(companyCode, scheduleCode);

        if (ObjectUtils.isEmpty(subscriptionSchedule)) {
            return null;
        }

        return getNewDeliverySchedule(subscriptionSchedule);
    }

    @Override
    public List<String> findGoodsByScheduleType(DeliverySchedule deliverySchedule) {

        List<String> detailGoodsCode = new ArrayList<>();

        if (CALENDAR.equals(deliverySchedule.getScheduleType())) {
            List<SubscriptionDetailCalendar> calendars = subscriptionDetailCalendarRepository.findByCompanyCodeAndScheduleCodeAndScheduleDateBetween(
                    deliverySchedule.getCompanyCode()
                    , deliverySchedule.getScheduleCode()
                    , CustomDateUtils.formatDateString(deliverySchedule.getFirstDeliveryDate())
                    , CustomDateUtils.formatDateString(deliverySchedule.getFirstDeliveryDate()));

            detailGoodsCode.addAll(calendars.stream().map(SubscriptionDetailCalendar::getGoodsCode).toList());
        } else {
            List<SubscriptionDetailTurn> turns = subscriptionDetailTurnRepository.findByCompanyCodeAndScheduleCode(
                    deliverySchedule.getCompanyCode()
                    , deliverySchedule.getScheduleCode()
            );

            detailGoodsCode.addAll(turns.stream().map(SubscriptionDetailTurn::getGoodsCode).toList());
        }

        return detailGoodsCode;
    }

    private SubscriptionScheduleMaster getSchedule(String companyCode, String goodsCode) {
        return subscriptionScheduleMasterRepository.findByCompanyCodeAndGoodsCodeAndUseYn(companyCode, goodsCode, "Y").orElse(null);
    }
}
