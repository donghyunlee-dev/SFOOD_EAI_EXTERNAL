package com.sfood.eai.external.repository;

import com.sfood.eai.external.entity.ApiCallLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiCallLogRepository extends JpaRepository<ApiCallLog, Long> {
    ApiCallLog findByLogId(String logId);
}
