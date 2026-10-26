package com.sfood.eai.external.config;

import org.springframework.batch.core.configuration.BatchConfigurationException;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Isolation;

import javax.sql.DataSource;

@Configuration
public class BatchConfig extends DefaultBatchConfiguration {

    private final DataSource batchDataSource;
    private final PlatformTransactionManager transactionManager;

     public BatchConfig(@Qualifier("omsDataSource")  DataSource batchDataSource, PlatformTransactionManager transactionManager) {
        this.batchDataSource = batchDataSource;
        this.transactionManager = transactionManager;
    }

    @Override
    protected DataSource getDataSource() {
        return this.batchDataSource;
    }

    @Override
    protected PlatformTransactionManager getTransactionManager() {
        return this.transactionManager;
    }

    @Bean
    @Override
    public JobRepository jobRepository() throws BatchConfigurationException {
        JobRepositoryFactoryBean factoryBean = new JobRepositoryFactoryBean();
        factoryBean.setDataSource(getDataSource());
        factoryBean.setTransactionManager(getTransactionManager());
        factoryBean.setIsolationLevelForCreateEnum(Isolation.READ_COMMITTED);

        try {
            factoryBean.afterPropertiesSet();
            return factoryBean.getObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
