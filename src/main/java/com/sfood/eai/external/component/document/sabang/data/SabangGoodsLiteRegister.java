package com.sfood.eai.external.component.document.sabang.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sfood.eai.external.component.document.sabang.SabangXmlTemplate;
import com.sfood.eai.external.component.document.sabang.header.SabangGoodsHeader;
import com.sfood.eai.external.config.MallConfiguration;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JacksonXmlRootElement(localName = "SABANG_GOODS_REGI")
public class SabangGoodsLiteRegister extends SabangXmlTemplate<List<SabangGoodsLiteRegister.Data>> {

    @Builder
    public SabangGoodsLiteRegister(MallConfiguration config, List<Data> goodsData) {
        super(new SabangGoodsHeader(config), goodsData);
    }

    @Setter
    @Getter
    public static class Data {
        @JacksonXmlProperty(localName = "GOODS_NM")
        private String goodsNm;             // 상품명

        @JacksonXmlProperty(localName = "COMPAYNY_GOODS_CD")
        private String companyGoodsCd;     // 자체 상품 코드

        @JacksonXmlProperty(localName = "STATUS")
        private String status;              // 상품상태 1.대기중 2.공급중 3.일시중지 4.완전품절 5.미사용 6.삭제

        @JacksonXmlProperty(localName = "GOODS_COST")
        private String goodsCost;           // 원가

        @JacksonXmlProperty(localName = "GOODS_PRICE")
        private String goodsPrice;          // 판매가

        @JacksonXmlProperty(localName = "GOODS_CONSUMER_PRICE")
        private String goodsConsumerPrice;  // Tag(소비자가)

        @JacksonXmlProperty(localName = "SKU_INFO")
        private List<String> skuInfo;       // 옵션 수정 여부
    }
}
