package com.sfood.eai.external.service.impl;

import com.sfood.eai.external.config.AppConfig;
import com.sfood.eai.external.entity.MailLog;
import com.sfood.eai.external.repository.MailLogRepository;
import com.sfood.eai.external.service.MailService;
import com.sfood.eai.external.type.MailType;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class MailServiceImpl implements MailService {

    private static final String RESULT_SUCCESS = "SUCCESS";
    private static final String RESULT_FAILED = "FAILED";

    private final JavaMailSender mailSender;
    private final MailLogRepository mailLogRepository;

    @Override
    public void send(final String to, final String subject, final String body) {

        MimeMessage mailMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper mailMessageHelper = new MimeMessageHelper(mailMessage, true, "UTF-8");
            mailMessageHelper.setFrom(AppConfig.SEND_EMAIL);
            mailMessageHelper.setTo(to);
            mailMessageHelper.setSubject(subject);
            mailMessageHelper.setText(body, true);

            mailSender.send(mailMessage);
            saveLog(mailMessage, "메일전송완료", RESULT_SUCCESS);
        } catch (MessagingException e) {
            saveLog(mailMessage, "메일전송실패", RESULT_FAILED);
            throw new RuntimeException("메일 전송 중 오류 발생", e);
        }
    }

    private void saveLog(MimeMessage mailMessage, String message, String result) {
        try {
            MailLog log = MailLog.builder()
                    .mailType(MailType.PRICING_EX)
                    .email(String.valueOf(mailMessage.getRecipients(Message.RecipientType.TO)[0]))
                    .message(message)
                    .resultCode(result)
                    .build();
            mailLogRepository.save(log);
        } catch (Exception e) {
            log.error("메일 로그 저장 실패", e);
        }
    }
}
