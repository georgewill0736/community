package com.nowcoder.community.config;

import com.nowcoder.community.quartz.AlphaJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * @Author 杜俊宏
 * Date on 2021/3/1 0:08
 */
//配置->数据库->调用
@Configuration
public class QuartzConfig {

    //FactoryBean的作用是简化Bean的实例化过程：
    //1，通过FactoryBean封装Bean的实例化过程
    //2，将FactoryBean装配到Spring容器里
    //3，将FactoryBean注入给其他的Bean，那么该Bean得到的是FactoryBean所管理的对象实例

    //配置JobDetail
    @Bean
    public JobDetailFactoryBean AlphaJobDetail() {
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setJobClass(AlphaJob.class);
        jobDetailFactoryBean.setName("alphaJob");
        jobDetailFactoryBean.setGroup("alphaJobGroup");
        jobDetailFactoryBean.setDurability(true);
        jobDetailFactoryBean.setRequestsRecovery(true);
        return jobDetailFactoryBean;
    }


    //配置Trigger（SimpleTriggerFactoryBean，CronTriggerFactoryBean）
    @Bean
    public SimpleTriggerFactoryBean AlphaTrigger(JobDetail alphaJobDetail) {
        SimpleTriggerFactoryBean simpleTriggerFactoryBean = new SimpleTriggerFactoryBean();
        simpleTriggerFactoryBean.setJobDetail(alphaJobDetail);
        simpleTriggerFactoryBean.setName("alphaTrigger");
        simpleTriggerFactoryBean.setGroup("alphaTriggerGroup");
        simpleTriggerFactoryBean.setRepeatInterval(3000);
        simpleTriggerFactoryBean.setJobDataMap(new JobDataMap());
        return simpleTriggerFactoryBean;
    }

}
