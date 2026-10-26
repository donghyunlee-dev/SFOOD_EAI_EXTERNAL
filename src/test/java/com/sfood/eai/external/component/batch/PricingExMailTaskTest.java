package com.sfood.eai.external.component.batch;

import com.sfood.eai.external.entity.pricing.ExpiryPricingEx;
import com.sfood.eai.external.service.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class PricingExMailTaskTest {

    @Autowired
    private MailService mailService;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Test
    public void sendMailTest() {

        ExpiryPricingEx priceEx = ExpiryPricingEx.builder()
                .partnerCode("110502")
                .partnerName("주식회사 엠에스에프디-테스트")
                .priceType("거래처별 예외단가")
                .goodsCode("GOAAA011535")
                .goodsName("R리얼 스모크햄_1000(냉동)")
                .endDate(LocalDate.of(2025, 06,21))
                .approvalId("donghyun.lee@s-food.com,")
                .approvalDate("2025-03-04")
                .description("오뗼 전환")
                .build();

        Context context = new Context();
        // html 내 List 이름과 setVariable name과 일치시켜야 함
        context.setVariable("list", List.of(priceEx));

        String to = "donghyun.lee@s-food.com";
        // 제목과 HTML 본문
        String subject = "[OMS] 예외단가 적용 종료일 1~7일전, 확인바랍니다.";
        String body = templateEngine.process("exception-pricing-mail", context); // html 파일명 일치시켜야 함

        // when
        mailService.send(to, subject, body);

    }

}