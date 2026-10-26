package com.sfood.eai.external.batch.job.order;

import com.sfood.eai.external.batch.job.JobType;
import com.sfood.eai.external.batch.job.order.processor.OrderCloseProcessor;
import com.sfood.eai.external.batch.job.order.reader.OrderCloseReader;
import com.sfood.eai.external.batch.job.order.task.StockMovementClosingTask;
import com.sfood.eai.external.batch.job.order.writer.OrderCloseWriter;
import com.sfood.eai.external.dto.order.OrderCloseStatus;
import com.sfood.eai.external.entity.order.OrderCloseInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class OrderCloseConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final OrderCloseReader orderCloseReader;
    private final OrderCloseProcessor orderCloseProcessor;
    private final OrderCloseWriter orderCloseWriter;
    private final StockMovementClosingTask stockMovementClosingTask;

    @Bean("createOrderCloseJob")
    public Job createOrderCloseJob() {
        return new JobBuilder(JobType.ORDER_CLOSE.getName(), jobRepository)
                .start(batchOrderCloseStep())
                .next(sendStockMovementClosingStep())
                .build();
    }

    @Bean
    public Step batchOrderCloseStep() {
        return new StepBuilder("batchOrderCloseStep", jobRepository)
                .<OrderCloseStatus, OrderCloseInfo>chunk(1000, transactionManager)
                .reader(orderCloseReader)
                .processor(orderCloseProcessor)
                .writer(orderCloseWriter)
                .build();
    }

    @Bean
    public Step sendStockMovementClosingStep() {
        return new StepBuilder("sendStockMovementClosingStep", jobRepository)
                .tasklet(stockMovementClosingTask, transactionManager)
                .build();
    }
}
