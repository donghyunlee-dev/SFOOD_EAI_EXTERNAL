package com.sfood.eai.external.dto.godo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GodoGiftData {

    @JacksonXmlProperty(localName = "sno")
    private Integer sno; //	주문-사은품 매칭 고유번호

    @JacksonXmlProperty(localName = "presentTitle")
    private String presentTitle;     // 사은품지급조건명

    @JacksonXmlProperty(localName = "giftNo")
    private String giftNo;          // 사은품번호

    @JacksonXmlProperty(localName = "giftCd")
    private String giftCd;           // 사은품코드

    @JacksonXmlProperty(localName = "giftNm")
    private String giftNm;           // 사은품명

    @JacksonXmlProperty(localName = "giftCnt")
    private Integer giftCnt;	    // 사은품 지급수량
}
