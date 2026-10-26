package com.sfood.eai.external.component.document;

@FunctionalInterface
public interface XmlDocument {
    /**
     * XML 파일 저장
     * @return 생성 결과 true / false
     * @throws Exception 오류 발생 시 Exception 전달
     */
    boolean saveXml(final String fileFullName);

}
