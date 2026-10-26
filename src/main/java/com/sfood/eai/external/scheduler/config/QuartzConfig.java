package com.sfood.eai.external.scheduler.config;

import com.sfood.eai.external.scheduler.listener.CustomJobListener;
import com.sfood.eai.external.scheduler.listener.CustomSchedulerListener;
import com.sfood.eai.external.scheduler.listener.CustomTriggerListener;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;

@DependsOn("DBConfig")
@Configuration
public class QuartzConfig {

    @Bean
    public CustomJobListener customJobListener() {
        return new CustomJobListener();
    }

    @Bean
    public CustomTriggerListener customTriggerListener() {
        return new CustomTriggerListener();
    }

    @Bean
    public CustomSchedulerListener customScheduleListener() {
        return new CustomSchedulerListener();
    }

    @Primary
    @Bean(name = "godoMallScheduler")
    public Scheduler GodoMallScheduler(ApplicationContext context
                                        , @Qualifier("quartzDataSource") DataSource dataSource
                                        , CustomJobListener jobListener
                                        , CustomTriggerListener triggerListener
                                        , CustomSchedulerListener schedulerListener) throws Exception {

        SchedulerFactoryBean factory = createSchedulerFactory("godoMallScheduler", context, dataSource, jobListener, triggerListener, schedulerListener);
        // SchedulerFactoryBean은 InitializingBean을 구현하므로 afterPropertiesSet()이 호출되지 않으면 내부적으로 Scheduler 인스턴스를 생성하지 않음
        factory.afterPropertiesSet();
        return factory.getScheduler();
    }

    @Bean(name = "sabangNetScheduler")
    public Scheduler SabangNetScheduler(ApplicationContext context
                                        , @Qualifier("quartzDataSource") DataSource dataSource
                                        , CustomJobListener jobListener
                                        , CustomTriggerListener triggerListener
                                        , CustomSchedulerListener schedulerListener) throws Exception {

        SchedulerFactoryBean factory = createSchedulerFactory("sabangNetScheduler", context, dataSource, jobListener, triggerListener, schedulerListener);
        factory.afterPropertiesSet();
        return factory.getScheduler();
    }

    @Bean(name = "retailScheduler")
    public Scheduler RetailScheduler(ApplicationContext context
                                    , @Qualifier("quartzDataSource") DataSource dataSource
                                    , CustomJobListener jobListener
                                    , CustomTriggerListener triggerListener
                                    , CustomSchedulerListener schedulerListener) throws Exception {

        SchedulerFactoryBean factory = createSchedulerFactory("retailScheduler", context, dataSource, jobListener, triggerListener, schedulerListener);
        factory.afterPropertiesSet();
        return factory.getScheduler();
    }

    private SchedulerFactoryBean createSchedulerFactory(String schedulerName
                                                        , ApplicationContext context
                                                        , DataSource dataSource
                                                        , CustomJobListener jobListener
                                                        , CustomTriggerListener triggerListener
                                                        , CustomSchedulerListener schedulerListener) {

        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setSchedulerName(schedulerName);    // Quartz 내부에서 식별하는 기준이기 때문에 필요
        factory.setJobFactory(getJobFactory(context));
        factory.setOverwriteExistingJobs(true);
        factory.setWaitForJobsToCompleteOnShutdown(true);
        factory.setConfigLocation(new ClassPathResource("quartz.properties"));
        factory.setDataSource(dataSource);
        factory.setGlobalJobListeners(jobListener);
        factory.setGlobalTriggerListeners(triggerListener);
        factory.setSchedulerListeners(schedulerListener);
        return factory;
    }

    private AutowiringSpringBeanJobFactory getJobFactory(ApplicationContext context) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(context);
        return jobFactory;
    }
}