package com.sfood.eai.external.batch.service.impl;

import com.sfood.eai.external.batch.service.BatchJobService;
import com.sfood.eai.external.batch.service.JobFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class BatchJobServiceImpl implements BatchJobService {

    private final JobLauncher jobLauncher;
    private final JobFactory jobFactory;

    @Override
    public void run(String jobName) throws Exception {

        Job job = jobFactory.getJob(jobName);

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        try {
            log.info("Job 시작: {}", jobParameters);
            jobLauncher.run(job, jobParameters);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void run(String jobName, Map<String, String> parameters) throws Exception {

        Job job = jobFactory.getJob(jobName);

        JobParametersBuilder builder = new JobParametersBuilder();
        parameters.forEach(builder::addString);
        builder.addLong("time", System.currentTimeMillis());

        try {
            jobLauncher.run(job, builder.toJobParameters());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
