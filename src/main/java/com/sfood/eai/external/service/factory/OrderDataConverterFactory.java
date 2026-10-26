package com.sfood.eai.external.service.factory;

import com.sfood.eai.external.service.converter.OrderDataConverter;
import com.sfood.eai.external.type.ChannelType;

@FunctionalInterface
public interface OrderDataConverterFactory {
    OrderDataConverter getConverter(ChannelType channelType);
}
