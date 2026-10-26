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
public class SabangGoodsRegister extends SabangXmlTemplate<List<SabangGoodsRegister.Data>> {

    @Builder
    public SabangGoodsRegister(MallConfiguration config, List<Data> goodsData) {
        super(new SabangGoodsHeader(config), goodsData);
    }

    @Setter
    @Getter
    public static class Data {
        @JacksonXmlProperty(localName = "GOODS_NM")
        private String goodsNm; // 상품명

        @JacksonXmlProperty(localName = "COMPAYNY_GOODS_CD")
        private String companyGoodsCod;     // 자체 상품 코드

        @JacksonXmlProperty(localName = "GOODS_SEARCH")
        private String goodsSearch;     // 사이트 검색

        @JacksonXmlProperty(localName = "GOODS_GUBUN")
        private String goodsGubun;      // 상품구분

        @JacksonXmlProperty(localName = "MAKER")
        private String maker;           // 제조사

        @JacksonXmlProperty(localName = "ORIGIN")
        private String origin;          // 원산지

        @JacksonXmlProperty(localName = "GOODS_SEASON")
        private String goodsSeason;     // 시즌 1.봄

        @JacksonXmlProperty(localName = "SEX")
        private String sex;             // 남녀구분 1.남성용 2.여름 3.가을 4.겨울 5.FW 6.SS 7.해당없음

        @JacksonXmlProperty(localName = "STATUS")
        private String status;          // 상품상태 1.대기중 2.공급중 3.일시중지 4.완전품절 5.미사용 6.삭제

        @JacksonXmlProperty(localName = "TAX_YN")
        private String taxYn;           // 세금구분 1.과세 2.면세 3.자료없음 4.비과세

        @JacksonXmlProperty(localName = "DELV_TYPE")
        private String delvType;        // 배송비 구분 1.무료 2.착불 3.선결제 4.착불/선결제

        @JacksonXmlProperty(localName = "GOODS_COST")
        private String goodsCost;       // 원가

        @JacksonXmlProperty(localName = "GOODS_PRICE")
        private String goodsPrice;      // 판매가

        @JacksonXmlProperty(localName = "GOODS_CONSUMER_PRICE")
        private String goodsConsumerPrice; // Tag(소비자가)

        @JacksonXmlProperty(localName = "IMG_PATH")
        private String imgPath;         // 대표이미지

        @JacksonXmlProperty(localName = "IMG_PATH1")
        private String imgPath1;        // 종합몰이미지

        @JacksonXmlProperty(localName = "IMG_PATH3")
        private String imgPath3;        // 부가이미지 3

        @JacksonXmlProperty(localName = "GOODS_REMARKS")
        private String goodsRemarks;    // 상품상세설명 상품상세(HTML)

        @JacksonXmlProperty(localName = "STOCK_USE_YN")
        private String stockUseYn;      // 재고관리사용여부 재고관리 사용여부를 Y or N로 입력

        @JacksonXmlProperty(localName = "OPT_TYPE")
        private String optType;         // 옵션 수정 여부

        public Data() {
            // Setup default value
            goodsGubun = "2";
            sex = "4";
            delvType = "3";
            goodsCost = "'0";
            goodsSeason = "7";
            optType = "2";
            imgPath = "";
            imgPath1 = "";
            imgPath3 = "";
            stockUseYn = "N";
            goodsRemarks = "<html></html>";
        }
    }
}
