package com.shls.s3backup.config;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Properties;

/**
 * @author ：Killer
 * @date ：Created in 20-6-12 上午11:26
 * @description：${description}
 * @modified By：
 * @version: version
 */
@Configuration
public class SchedulerConfig {

    @Bean(name = "properties")
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    @Bean(name = "schedulerFactory")
    public SchedulerFactory SchedulerFactory(Properties properties) throws IOException, SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory(properties);
        return schedulerFactory;
    }

    /**
     * 通过SchedulerFactory获取Scheduler的实例
     */
    @Bean(name = "scheduler")
    public Scheduler scheduler(SchedulerFactory schedulerFactory) throws IOException, SchedulerException {
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.start();
        return scheduler;
    }
}