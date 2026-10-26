package com.sfood.eai.external.component.document.godo;

import com.sfood.eai.external.component.document.XmlDocument;
import com.sfood.eai.external.component.document.godo.data.GodoGoodsData;
import com.sfood.eai.external.component.document.godo.data.GodoGoodsMustInfoData;
import com.sfood.eai.external.component.document.godo.data.GodoOptionData;
import com.sfood.eai.external.component.document.godo.data.GodoTextOptionData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class GodoDocumentTest {

    @Test
    public void createGodoGoodsXmlFileTest() {

        XmlDocument doc = new GodoDocument<>(createSampleData());
        try {
            doc.saveXml("GODO_GOODS.xml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<GodoGoodsData> createSampleData() {
         List<GodoGoodsData> goodsDataList = new ArrayList<>();

        GodoGoodsData goodsData = new GodoGoodsData();
        goodsData.setIdx("1");
        goodsData.setCateCd("001");
        goodsData.setAllCateCd("001|002");
        goodsData.setGoodsDisplayFl("y");
        goodsData.setGoodsDisplayMobileFl("y");
        goodsData.setGoodsSellFl("y");
        goodsData.setGoodsSellMobileFl("y");
        goodsData.setGoodsCd("");
        goodsData.setGoodsNmFl("d");
        goodsData.setGoodsNm("rrrrr");
        goodsData.setGoodsState("n");
        goodsData.setImageStorage("local");

        // GoodsMustInfoData 추가
        List<GodoGoodsMustInfoData> goodsMustInfoDataList = new ArrayList<>();

        GodoGoodsMustInfoData goodsMustInfo1 = new GodoGoodsMustInfoData();
        goodsMustInfo1.setIdx("1");

        GodoGoodsMustInfoData.GodoStepData stepData1 = new GodoGoodsMustInfoData.GodoStepData();
        stepData1.setIdx("1");
        stepData1.setInfoTitle("infoTitle1");
        stepData1.setInfoValue("infoValue1");

        GodoGoodsMustInfoData.GodoStepData stepData2 = new GodoGoodsMustInfoData.GodoStepData();
        stepData2.setIdx("2");
        stepData2.setInfoTitle("infoTitle2");
        stepData2.setInfoValue("infoValue2");

        goodsMustInfo1.setStepData(Arrays.asList(stepData1, stepData2));

        GodoGoodsMustInfoData goodsMustInfo2 = new GodoGoodsMustInfoData();
        goodsMustInfo2.setIdx("2");

        GodoGoodsMustInfoData.GodoStepData stepData3 = new GodoGoodsMustInfoData.GodoStepData();
        stepData3.setIdx("1");
        stepData3.setInfoTitle("infoTitle3");
        stepData3.setInfoValue("infoValue3");

        goodsMustInfo2.setStepData(Arrays.asList(stepData3));

        goodsMustInfoDataList.add(goodsMustInfo1);
        goodsMustInfoDataList.add(goodsMustInfo2);

        goodsData.setGoodsMustInfoData(goodsMustInfoDataList);


        List<GodoOptionData> optionDataList = new ArrayList<>();

        GodoOptionData option1 = new GodoOptionData();
        option1.setIdx("1");
        option1.setOptionNo("1");
        option1.setOptionValue1("봄");
        option1.setOptionPrice("6000");
        option1.setOptionViewFl("y");
        option1.setOptionSellFl("y");
        option1.setStockCnt(100);

        GodoOptionData option2 = new GodoOptionData();
        option2.setIdx("2");
        option2.setOptionNo("2");
        option2.setOptionValue1("봄");
        option2.setOptionPrice("6000");
        option2.setOptionViewFl("y");
        option2.setOptionSellFl("y");
        option2.setStockCnt(100);

        GodoOptionData option3 = new GodoOptionData();
        option3.setIdx("3");
        option3.setOptionNo("3");
        option3.setOptionValue1("여름");
        option3.setOptionPrice("6000");
        option3.setOptionViewFl("y");
        option3.setOptionSellFl("y");
        option3.setStockCnt(100);

        GodoOptionData option4 = new GodoOptionData();
        option4.setIdx("4");
        option4.setOptionNo("4");
        option4.setOptionValue1("여름");
        option4.setOptionPrice("6000");
        option4.setOptionViewFl("y");
        option4.setOptionSellFl("y");
        option4.setStockCnt(100);

        optionDataList.add(option1);
        optionDataList.add(option2);
        optionDataList.add(option3);
        optionDataList.add(option4);

        goodsData.setOptionData(optionDataList);

        // TextOptionData 추가
        List<GodoTextOptionData> textOptionDataList = new ArrayList<>();

        GodoTextOptionData textOption1 = new GodoTextOptionData();
        textOption1.setIdx("1");
        textOption1.setOptionName("텍스트옵션1");
        textOption1.setMustFl("n");

        GodoTextOptionData textOption2 = new GodoTextOptionData();
        textOption2.setIdx("2");
        textOption2.setOptionName("텍스트옵션2");
        textOption2.setMustFl("n");

        textOptionDataList.add(textOption1);
        textOptionDataList.add(textOption2);

        goodsData.setTextOptionData(textOptionDataList);

        // 이미지 정보 추가
        List<String> detailImageList = new ArrayList<>();
        detailImageList.add("http: //img.godo.co.kr/cache/1606/gnb_customize_toplogo.gif");

        List<String> listImageList = new ArrayList<>();
        listImageList.add("http: //img.godo.co.kr/cache/1606/gnb_customize_toplogo.gif");

        List<String> magnifyImageList = new ArrayList<>();
        magnifyImageList.add("http: //img.godo.co.kr/cache/1606/gnb_customize_toplogo.gif");

        List<String> mainImageList = new ArrayList<>();
        mainImageList.add("http: //img.godo.co.kr/cache/1606/gnb_customize_toplogo.gif");

        goodsDataList.add(goodsData);

        return goodsDataList;
    }
}