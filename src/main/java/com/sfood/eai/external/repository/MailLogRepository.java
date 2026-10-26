package com.sfood.eai.external.repository;

import com.sfood.eai.external.entity.MailLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailLogRepository extends JpaRepository<MailLog, Long> {
}
