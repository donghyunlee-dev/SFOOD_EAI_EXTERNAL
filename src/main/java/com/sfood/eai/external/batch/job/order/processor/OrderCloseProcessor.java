package com.sfood.eai.external.batch.job.order.processor;

import com.sfood.eai.external.dto.order.OrderCloseStatus;
import com.sfood.eai.external.entity.id.order.OrderGoodsId;
import com.sfood.eai.external.entity.order.OrderCloseInfo;
import com.sfood.eai.external.mapper.order.OrderCloseInfoMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class OrderCloseProcessor implements ItemProcessor<OrderCloseStatus, OrderCloseInfo> {

    @Override
    public OrderCloseInfo process(OrderCloseStatus orderCloseStatus) throws Exception {
        return OrderCloseInfoMapper.INSTANCE.toEntity(orderCloseStatus);
    }
}
