package com.sfood.eai.external.component.document;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;

public final class XmlSerializer extends StdSerializer<Object>  {
    private final String elementName;

    public XmlSerializer(String elementName) {
        super(Object.class);
        this.elementName = elementName;
    }

    @Override
    public void serialize(Object objects, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        String xmlString = xmlMapper.writeValueAsString(objects).replace("GoodsData", elementName); // 고도몰 품목 전송 시 변환
        jsonGenerator.writeRaw(xmlString);
    }
}
