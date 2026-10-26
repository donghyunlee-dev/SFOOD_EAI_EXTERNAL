package com.sfood.eai.external.batch.service;

import java.util.Map;

public interface BatchJobService {

    void run(String jobName) throws Exception;

    void run(String jobName, Map<String, String> parameters) throws Exception;
}
