package com.sfood.eai.external.batch.job.order.task;

import com.sfood.eai.external.component.http.AppHttpClient;
import com.sfood.eai.external.dto.stock.StockMovementCloseReq;
import com.sfood.eai.external.dto.stock.StockMovementClosing;
import com.sfood.eai.external.repository.order.OrderGoodsRepository;
import com.sfood.eai.external.util.CustomDateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.sfood.eai.external.type.Constants.EXTERNAL_USER_ID;

@Slf4j
@RequiredArgsConstructor
@Component
public class StockMovementClosingTask implements Tasklet {

    private final AppHttpClient appHttpClient;
    private final OrderGoodsRepository orderGoodsRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<StockMovementClosing> stockMovementCloseByCompany = getStockMovementLatestTransDate();

        stockMovementCloseByCompany.forEach(stockMovementClosing ->
                sendStockMovementClosing(stockMovementClosing)
                );

        return RepeatStatus.FINISHED;
    }

    private List<StockMovementClosing> getStockMovementLatestTransDate() {
        return orderGoodsRepository.getStockMovementMaxTransDate();
    }

    private void sendStockMovementClosing(StockMovementClosing stockMovementClosing) {

        try {
            StockMovementCloseReq request = StockMovementCloseReq.builder()
                    .companyCode(stockMovementClosing.getCompanyCode())
                    .maxDate(CustomDateUtils.formatDateTimeString(stockMovementClosing.getMaxTransDate(), "yyyy-MM-dd"))
                    .userId(EXTERNAL_USER_ID)
                    .build();

            ResponseEntity<?> response = appHttpClient.closeStockMovement(request);
            log.info("Run Close StockMovement Job : {}", response);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
