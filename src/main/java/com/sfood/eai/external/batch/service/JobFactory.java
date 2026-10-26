package com.sfood.eai.external.batch.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.batch.core.Job;

public interface JobFactory {
    Job getJob(@NotNull String jobName);
}
