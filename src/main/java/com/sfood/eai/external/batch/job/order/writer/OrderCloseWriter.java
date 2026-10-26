package com.sfood.eai.external.batch.job.order.writer;

import com.sfood.eai.external.entity.order.OrderCloseInfo;
import com.sfood.eai.external.repository.order.OrderCloseInfoJdbcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderCloseWriter implements ItemWriter<OrderCloseInfo> {

    private final OrderCloseInfoJdbcRepository orderCloseInfoJdbcRepository;

    @Transactional
    @Override
    public void write(Chunk<? extends OrderCloseInfo> chunk) throws Exception {

        if (CollectionUtils.isEmpty(chunk.getItems())) {
            return;
        }

        // Chunk 단위로 Read, Processor 가 반복적으로 끝난 다음 호출되어 데이터 DB 저장
        List<OrderCloseInfo> orderCloseInfoList = chunk.getItems().stream()
                .filter(Objects::nonNull)
                .map(OrderCloseInfo.class::cast)
                .toList();

        try {
            orderCloseInfoJdbcRepository.saveAll(orderCloseInfoList);
        } catch (Exception e) {
            log.error("error when save order close info", e);
            throw e;
        }
    }
}
