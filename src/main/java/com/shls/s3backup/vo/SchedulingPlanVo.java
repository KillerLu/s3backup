package com.shls.s3backup.vo;

/**
 * @author ：Killer
 * @date ：Created in 20-6-12 上午11:36
 * @description：${description}
 * @modified By：
 * @version: version
 */
public class SchedulingPlanVo {

    /**
     * 计划id
     */
    private Long id;

    /**
     * 计划名称
     */
    private String jobName;

    /**
     * 计划组
     */
    private String jobGroup;

    /**
     * Cron表达式
     */
    private String CronExpressions;

    /**
     * 开启关闭状态 0开启 1暂停
     */
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getCronExpressions() {
        return CronExpressions;
    }

    public void setCronExpressions(String cronExpressions) {
        CronExpressions = cronExpressions;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
