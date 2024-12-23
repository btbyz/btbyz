package com.ruoyi.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * 定时任务配置
 * 
 * @author ruoyi
 */
@Configuration
public class ScheduleConfig
{
    @Resource
    private ScheduleProperties scheduleProperties;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource)
    {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        // 设置自动启动，默认为true
        factory.setAutoStartup(scheduleProperties.isAutoStartup());
        return factory;
    }
}
