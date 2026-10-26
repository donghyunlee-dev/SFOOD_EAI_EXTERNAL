package com.sfood.eai.external.batch.service.impl;

import com.sfood.eai.external.batch.service.JobFactory;
import jakarta.validation.constraints.NotNull;
import org.springframework.batch.core.Job;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DefaultJobFactory implements JobFactory {

    private final Map<String, Job> jobMap;

    public DefaultJobFactory(Map<String, Job> jobMap) {
        this.jobMap = jobMap;
    }

    @Override
    public Job getJob(@NotNull String jobName) {
        try {
            return jobMap.get(jobName);
        } catch (Exception e) {
            throw new IllegalArgumentException("Not a valid job name: " + jobName, e);
        }
    }
}
