package com.sfood.eai.external.batch.job.pricing.task;

import com.sfood.eai.external.entity.pricing.ExpiryPricingEx;
import com.sfood.eai.external.repository.pricing.PricingExPartnerGroupRepository;
import com.sfood.eai.external.repository.pricing.PricingExPartnerRepository;
import com.sfood.eai.external.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class PricingExMailTask implements Tasklet {

    private static final String MAIL_SUBJECT = "[OMS] 예외단가 적용 종료일 1~7일전, 확인바랍니다.";
    private static final String MAIL_TEMPLATE_FILE_NAME = "exception-pricing-mail";  // resources > templates > file name
    private static final String MAIL_BIND_DATA_ID = "list";

    private final MailService mailService;
    private final SpringTemplateEngine templateEngine;
    private final PricingExPartnerRepository pricingExPartnerRepository;
    private final PricingExPartnerGroupRepository pricingExPartnerGroupRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        List<ExpiryPricingEx> expiryPricingExList = getExpiryPricingExAll();

        if (CollectionUtils.isEmpty(expiryPricingExList)) {
            return RepeatStatus.FINISHED;
        }

        expiryPricingExList.stream()
                .collect(Collectors.groupingBy(ExpiryPricingEx::getApprovalId))
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey() != null)
                .forEach(this::sendMail);

        return RepeatStatus.FINISHED;
    }

    private List<ExpiryPricingEx> getExpiryPricingExAll() {
        List<ExpiryPricingEx> expiryPricingExList = new ArrayList<>();
        expiryPricingExList.addAll(pricingExPartnerRepository.findExpiryPricingExPartner());
        expiryPricingExList.addAll(pricingExPartnerGroupRepository.findExpiryPricingExPartnerGroup());
        return expiryPricingExList;
    }

    private void sendMail(Map.Entry<String, List<ExpiryPricingEx>> bindData) {
        String to = bindData.getKey();
        String subject = MAIL_SUBJECT;
        String body;

        Context context = new Context();
        context.setVariable(MAIL_BIND_DATA_ID, bindData.getValue());
        body = templateEngine.process(MAIL_TEMPLATE_FILE_NAME, context);

        mailService.send(to, subject, body);
    }
}
