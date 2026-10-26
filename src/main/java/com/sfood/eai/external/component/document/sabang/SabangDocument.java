package com.sfood.eai.external.component.document.sabang;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sfood.eai.external.component.document.DocumentWriter;
import com.sfood.eai.external.component.document.XmlDocument;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

@ToString
@Slf4j
@AllArgsConstructor
@RequiredArgsConstructor
public final class SabangDocument<T> implements XmlDocument {

    private T xmlData;

    @Override
    public boolean saveXml(final String fileName) {
        try {
            Document doc = write(this.xmlData);
            String saveFileName = DocumentWriter.saveFile(doc, fileName, "EUC-KR");
            log.info("XML 파일 생성 : {}", saveFileName);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return false;
    }

    private Document write(T data) throws Exception {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        ByteArrayInputStream input = new ByteArrayInputStream(xmlMapper.writeValueAsString(data).getBytes());
        return builder.parse(input);
    }
}
