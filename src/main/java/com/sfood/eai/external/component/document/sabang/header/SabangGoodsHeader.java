package com.sfood.eai.external.component.document.sabang.header;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.sfood.eai.external.config.MallConfiguration;
import lombok.Getter;

@Getter
public class SabangGoodsHeader extends SabangHeader {
    @JacksonXmlProperty(localName = "SEND_GOODS_CD_RT")
    private final String sendGoodsCdRt = "Y";   // 상품등록/수정 성공시 결과에서 자체코드를 표시합니다. Y:반환, NULL:없음

    public SabangGoodsHeader(MallConfiguration config) {
        super(config);
    }
}
