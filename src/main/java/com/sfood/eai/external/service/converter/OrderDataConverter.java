package com.sfood.eai.external.service.converter;

import com.sfood.eai.external.type.ChannelType;

public interface OrderDataConverter {

    ChannelType getChannelType();

    <T> T convertOrderEntity(Object rawData);
}
