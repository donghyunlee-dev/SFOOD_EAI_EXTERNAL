package com.sfood.eai.external.entity;

import com.sfood.eai.external.dto.ApiCallLogRes;
import com.sfood.eai.external.util.CustomStringUtils;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@ToString
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SequenceGenerator(
        name = "API_CALL_LOG",
        sequenceName = "API_CALL_LOG_SEQ",
        initialValue = 1,
        allocationSize = 1)
@Table(name = "TBL_API_CALL_LOG")
@Entity
public class ApiCallLog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "API_CALL_LOG_SEQ")
    @Column(name = "SEQ_NO")
    private Long seqNo;

    @Column(name = "LOG_ID")
    private String logId;

    @Column(name = "LOG_TYPE")
    private String logType;

    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Column(name = "MALL_CODE")
    private String mallCode;

    @Column(name = "MALL_ID")
    private String mallId;

    @Column(name = "INTERFACE_ID")
    private String interfaceId;

    @Column(name = "STATUS_CODE")
    private String statusCode;

    @Column(name = "STATUS_NAME")
    private String statusName;

    @Column(name = "REQ_DATA")
    private String reqData;

    @Column(name = "RES_DATA")
    private String resData;

    @Column(name = "REQ_TIME")
    private Long reqTime;

    @Column(name = "RES_TIME")
    private Long resTime;

    @Column(name = "ELAPSED_TIME")
    private Long elapsedTime;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "CLIENT_IP")
    private String clientIp;

    @CreatedDate
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    public void setResponseData(ApiCallLogRes responseData) {
        this.resData = CustomStringUtils.substringByBytes(responseData.getData(), 0, 4000);
        this.resTime = responseData.getResTime();
        this.elapsedTime = this.resTime - this.reqTime;
        this.status = responseData.getStatus();
        this.message = responseData.getMessage();
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = ZonedDateTime.now(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .toLocalDateTime();
    }
}
