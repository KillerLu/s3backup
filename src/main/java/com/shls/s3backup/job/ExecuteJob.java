package com.shls.s3backup.job;

import com.shls.s3backup.service.BackupService;
import com.shls.s3backup.util.DateUtil;
import com.shls.s3backup.util.SpringContextUtil;
import com.shls.s3backup.view.SchedulingPlan;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ：Killer
 * @date ：Created in 20-6-12 上午11:28
 * @description：${description}
 * @modified By：
 * @version: version
 */
@Component
public class ExecuteJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(ExecuteJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        SchedulingPlan schedulingPlan = (SchedulingPlan) jobExecutionContext.getMergedJobDataMap().get("key");
        logger.info("定时任务开始执行，任务名称[" + schedulingPlan.getJobName() + "]::::::执行时间" + DateUtil.getNow());
        BackupService backupService= SpringContextUtil.getBean("backupService");
        backupService.backupS3(BackupService.REGULAR_BACKUP);
    }
}

