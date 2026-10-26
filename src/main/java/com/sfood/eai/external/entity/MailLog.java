package com.sfood.eai.external.entity;

import com.sfood.eai.external.type.MailType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SequenceGenerator(
        name="MAIL_LOG",
        sequenceName = "MAIL_LOG_SEQ",
        initialValue = 1,
        allocationSize = 1)
@Entity
@Table(name="TBL_MAIL_LOG")
public class MailLog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MAIL_LOG_SEQ")
    @Column(name = "SEQ_NO")
    private Long seqNo;

    @CreatedDate
    @Column(name = "LOG_DATE" , updatable = false)
    private LocalDateTime logDate;

    @Column(name = "MAIL_TYPE")
    @Enumerated(EnumType.STRING)
    private MailType mailType;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "RESULT_CODE")
    private String resultCode;
}
