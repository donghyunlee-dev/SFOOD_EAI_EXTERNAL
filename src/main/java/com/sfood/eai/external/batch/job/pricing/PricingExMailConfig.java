package com.sfood.eai.external.batch.job.pricing;

import com.sfood.eai.external.batch.job.JobType;
import com.sfood.eai.external.batch.job.pricing.task.PricingExMailTask;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class PricingExMailConfig {
    // 설정 및 메타 데이터 저장 (DB 테이블 저장)
    private final JobRepository jobRepository;
    private final PricingExMailTask pricingExMailTasklet;
    private final PlatformTransactionManager transactionManager;

    @Primary
    @Bean("sendPricingExMailJob")
    public Job sendPricingExMailJob() {
        return new JobBuilder(JobType.PRICING_EXCEPTION.getName(), jobRepository)
                .start(sendPricingExMailStep())
                .build();
    }

    @Bean
    public Step sendPricingExMailStep() {
        return new StepBuilder("sendPricingExMailStep", jobRepository)
                .tasklet(pricingExMailTasklet, transactionManager)
                .build();
    }
}
