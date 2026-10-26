package com.sfood.eai.external.component.document.godo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sfood.eai.external.component.document.DocumentWriter;
import com.sfood.eai.external.component.document.XmlDocument;
import com.sfood.eai.external.component.document.XmlSerializer;
import com.sfood.eai.external.component.document.godo.data.GodoGoodsData;
import com.sfood.eai.external.component.document.godo.data.GodoOrderStatusData;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

@ToString
@Slf4j
@AllArgsConstructor
public final class GodoDocument<T> implements XmlDocument {

    private T xmlData;

    @Override
    public boolean saveXml(final String fileName) {
        try {
            Document doc = write(this.xmlData);
            String saveFileName = DocumentWriter.saveFile(doc, fileName, "UTF-8");
            log.info("XML 파일 생성 : {}", saveFileName);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    private Document write(T data) throws Exception {
        SimpleModule module = new SimpleModule();
        module.addSerializer(GodoGoodsData.class, new XmlSerializer("goods_data"));
        module.addSerializer(GodoOrderStatusData.class, new XmlSerializer("statusData"));

        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.registerModule(module);
        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        ByteArrayInputStream input = new ByteArrayInputStream(xmlMapper.writeValueAsString(new GodoXmlTemplate<>(data)).getBytes());
        return builder.parse(input);
    }
}





