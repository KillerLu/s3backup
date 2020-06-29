package com.shls.s3backup.job;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.TriggerKey;

/**
 * @author ：Killer
 * @date ：Created in 20-6-12 上午11:31
 * @description：${description}
 * @modified By：
 * @version: version
 */
public class JobInformation {
    // 作业key
    private JobKey jobKey;
    // 作业
    private JobDetail jobDetail;
    // 触发器key
    private TriggerKey triggerKey;
    // Cron触发器
    private CronTrigger trigger;


    public JobKey getJobKey() {
        return jobKey;
    }

    public void setJobKey(JobKey jobKey) {
        this.jobKey = jobKey;
    }

    public JobDetail getJobDetail() {
        return jobDetail;
    }

    public void setJobDetail(JobDetail jobDetail) {
        this.jobDetail = jobDetail;
    }

    public TriggerKey getTriggerKey() {
        return triggerKey;
    }

    public void setTriggerKey(TriggerKey triggerKey) {
        this.triggerKey = triggerKey;
    }

    public CronTrigger getTrigger() {
        return trigger;
    }

    public void setTrigger(CronTrigger trigger) {
        this.trigger = trigger;
    }

}
