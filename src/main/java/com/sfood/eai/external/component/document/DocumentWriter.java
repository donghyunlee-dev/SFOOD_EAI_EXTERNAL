package com.sfood.eai.external.component.document;

import com.sfood.eai.external.config.AppConfig;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.w3c.dom.Document;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DocumentWriter {
    /**
     * 파일 생성
     * @param document 저장 객체
     * @param fileName 저장 파일 명
     * @return 저장한 파일 명
     * @throws Exception 예외 처리
     */
    public static String saveFile(final Document document, final String fileName, final String encodeType) throws Exception {
        // Transformer 설정
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.setOutputProperty(OutputKeys.ENCODING, encodeType);
        transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
        // XML 파일 저장
        DOMSource domSource = new DOMSource(document);

        File file = new File(AppConfig.LOCAL_FILE_PATH + fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            StreamResult streamResult = new StreamResult(fos);
            transformer.transform(domSource, streamResult);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return file.getName();
    }
}
