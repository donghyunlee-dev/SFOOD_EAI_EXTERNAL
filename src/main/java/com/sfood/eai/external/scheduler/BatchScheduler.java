package com.sfood.eai.external.scheduler;

import com.sfood.eai.external.batch.job.JobType;
import com.sfood.eai.external.batch.service.BatchJobService;
import com.sfood.eai.external.component.http.AppServerManager;
import com.sfood.eai.external.dto.order.OrderRequest;
import com.sfood.eai.external.service.OrderService;
import com.sfood.eai.external.util.CustomDateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class BatchScheduler {

    private final BatchJobService batchJobService;
    private final OrderService orderService;
    private final AppServerManager appServerManager;

    @Scheduled(cron = "0 1 0 * * *", zone = "Asia/Seoul")
    private void runInitOrder() {

        if (!appServerManager.isActive()) return;

        OrderRequest orderRequest = new OrderRequest(CustomDateUtils.now());
        orderService.initOrder(orderRequest);
    }

    @Scheduled(cron = "10 1 0 * * *", zone = "Asia/Seoul")
    private void runBatchOrderClose() throws Exception {
        log.info(">>>>> runBatchOrderClose 실행 {}", System.identityHashCode(this));
        if (!appServerManager.isActive()) return;

        batchJobService.run(JobType.ORDER_CLOSE.getName());
    }

    @Scheduled(cron = "0 0 8 ? * SUN-FRI", zone = "Asia/Seoul")
    public void runPricingExMailBatch() throws Exception {
        log.info(">>>>> runPricingExMailBatch 실행 {}", System.identityHashCode(this));
        if (!appServerManager.isActive()) return;

        batchJobService.run(JobType.PRICING_EXCEPTION.getName());
    }
}
