package com.sfood.eai.external.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sfood.eai.external.dto.sabang.SabangGoodsRes;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.MessageFormat;

public class CustomStringUtilsTest {

    @Test
    @DisplayName("문자열 숫자 추출 테스트")
    public void extractDateNumbersTest() {

        String str = "새벽배송(20250506)";

        int result = CustomStringUtils.extractNumbers(str)[0];
        System.out.println("result = " + result);
        Assertions.assertEquals(20250506, result);
    }

    @Test
    @DisplayName("사방넷 품목 응답 xml Convert 테스트")
    public void extractXmlFileTest() {
        String xml = "<?xml version=\"1.0\" encoding=\"euc-kr\"?>\n" +
                "\t<SABANG_RESULT>\n" +
                "\t\t<HEADER>\n" +
                "\t\t\t<SEND_COMPAYNY_ID>sfood1987</SEND_COMPAYNY_ID>\n" +
                "\t\t\t<SEND_DATE><![CDATA[20250312]]></SEND_DATE>\n" +
                "\t\t\t<TOTAL_COUNT>1</TOTAL_COUNT>\n" +
                "\t\t</HEADER>\n" +
                "\t\t<DATA>\n" +
                "\t\t\t<RESULT>FAIL</RESULT>\n" +
                "\t\t\t<RESULT_MSG><![CDATA[상품설명 재고관리사용여부 누락]]></RESULT_MSG>\n" +
                "\t\t\t<COMPAYNY_GOODS_CD><![CDATA[GO20241226AAA0044]]></COMPAYNY_GOODS_CD>\n" +
                "\t\t</DATA>\n" +
                "\t</SABANG_RESULT>";

        XmlMapper xmlMapper = new XmlMapper();
        SabangGoodsRes result = null;
        try {
            result = xmlMapper.readValue(xml, SabangGoodsRes.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println(result);
    }

    @Test
    @DisplayName("고도몰 상태 문자 제거 테스트")
    public void extractStatusNumbersTest() {
        // given
        String internalStatusCode = "S5";
        // when
        String godoMallStatusCode = String.valueOf(CustomStringUtils.extractNumbers(internalStatusCode)[0]);
        //then
        String expectStatusCode = "5";
        Assertions.assertEquals(expectStatusCode, godoMallStatusCode);
        System.out.println(MessageFormat.format("given = {0},. when = {1}, then = {2}", internalStatusCode, godoMallStatusCode, expectStatusCode));
    }

    @Test
    @DisplayName("SPLIT 정규식 사용 테스트")
    public void splitTest() {

        String group = "C00000001.CN0002";

        // String + 정규식 사용
        String stringCodes[] = group.split("\\.");
        // StringUtils - 문자로 처리
        String utilCodes[] = StringUtils.split(group, ".");
 
        Assertions.assertEquals("C00000001", stringCodes[0]);
        Assertions.assertEquals("CN0002", utilCodes[1]);
    }

    @Test
    @DisplayName("최대값 구하기. 가용재고 -수량 체크로직에 사용")
    public void mathMaxTest() {
        double avableCount = -1999;
        double orderCount = 2995;
        double assignCount = 0;

        assignCount = avableCount >= orderCount ? orderCount : Math.max(avableCount, 0);

        Assertions.assertEquals(assignCount, 0);
        System.out.println("Assign count = " + assignCount);
    }
}
