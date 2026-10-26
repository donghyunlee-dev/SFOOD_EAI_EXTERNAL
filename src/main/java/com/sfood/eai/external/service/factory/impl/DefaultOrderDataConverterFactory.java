package com.sfood.eai.external.service.factory.impl;

import com.sfood.eai.external.service.converter.OrderDataConverter;
import com.sfood.eai.external.service.factory.OrderDataConverterFactory;
import com.sfood.eai.external.type.ChannelType;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DefaultOrderDataConverterFactory implements OrderDataConverterFactory {
    private final Map<ChannelType, OrderDataConverter> converterMap = new HashMap<>();

    @Autowired
    private DefaultOrderDataConverterFactory(List<OrderDataConverter> converters) {
        for (OrderDataConverter converter : converters) {
            this.converterMap.put(converter.getChannelType(), converter);
        }
    }

    @Override
    public OrderDataConverter getConverter(ChannelType channelType) {
         OrderDataConverter converter = converterMap.get(channelType);

         if(ObjectUtils.isEmpty(converter)) {
             throw new IllegalArgumentException("No converter registered for type : " + channelType);
         }

         return converter;
    }
}
