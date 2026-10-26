package com.sfood.eai.external.batch.job.order.reader;

import com.sfood.eai.external.dto.order.OrderCloseStatus;
import com.sfood.eai.external.entity.id.order.OrderGoodsId;
import com.sfood.eai.external.repository.order.OrderGoodsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
public class OrderCloseReader implements ItemReader<OrderCloseStatus> {

    private final OrderGoodsRepository orderGoodsRepository;
    private Iterator<OrderCloseStatus> dataIterator;

    public OrderCloseReader(OrderGoodsRepository orderGoodsRepository) {
        this.orderGoodsRepository = orderGoodsRepository;
    }

    @Override
    public OrderCloseStatus read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        // 초기 데이터가 없을 경우에만 조회 후 저장
        if (dataIterator == null) {
            List<OrderCloseStatus> batchTargetData = orderGoodsRepository.findClosedOrderGoodsIds();
            this.dataIterator = batchTargetData.iterator();
        }

        // 저장된 데이터가 있으면 하나씩 데이터 리턴
        if (dataIterator.hasNext()) {
            return dataIterator.next();
        } else {
            dataIterator = null;
            return null;
        }
    }

}
