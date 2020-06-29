package com.shls.s3backup.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shls.s3backup.dao.SchedulingPlanDao;
import com.shls.s3backup.job.ExecuteJob;
import com.shls.s3backup.job.JobInformation;
import com.shls.s3backup.util.CloneUtil;
import com.shls.s3backup.util.ListAndCount;
import com.shls.s3backup.view.BackupRecord;
import com.shls.s3backup.view.BackupTask;
import com.shls.s3backup.view.SchedulingPlan;
import com.shls.s3backup.vo.BackupRecordVo;
import com.shls.s3backup.vo.BackupTaskVo;
import com.shls.s3backup.vo.SchedulingPlanVo;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author ：Killer
 * @date ：Created in 20-6-12 上午11:32
 * @description：${description}
 * @modified By：
 * @version: version
 */
@Service
public class SchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerService.class);


    @Autowired
    private Scheduler scheduler;
    @Autowired
    private SchedulingPlanDao schedulingPlanDao;
    @Autowired
    private BackupService backupService;

    /**
     * 调度任务状态
     */
    public static final int SCHEDULING_PLAN_RUNNING=0;

    public static final int SCHEDULING_PLAN_PAUSE=1;

    /**
     * 修改调度计划
     * *
     * withMisfireHandlingInstructionIgnoreMisfires（所有misfire的任务会马上执行）
     * 如 原调度计划是1秒执行一次，，暂停10秒，当调度计划恢复时 会先执行10次
     * <p>
     * withMisfireHandlingInstructionDoNothing(所有的misfire不管，执行下一个周期的任务)
     * 如 原调度计划是1秒执行一次，，暂停10秒，当调度计划恢复时 会在下个周期在执行（中间暂停的10次会放弃）
     * <p>
     * withMisfireHandlingInstructionFireAndProceed（会合并部分的misfire,正常执行下一个周期的任务）
     * 当修改计划时，会先执行一次计划
     * 恢复计划时，会执行一次
     */
    public void updateSchedulingPlan(SchedulingPlan plan) throws SchedulerException {
        if (plan == null || plan.getId() == null) {
            throw new RuntimeException("调度计划id不能为空");
        }
        SchedulingPlan schedulingPlan = schedulingPlanDao.selectById(plan.getId());
        if (schedulingPlan == null) {
            throw new RuntimeException("id:" + schedulingPlan.getId() + "的调度计划不存在");
        }
        //中断所有进行中的定时文件备份 TODO
        //backupService.suspendRunningTask();
        CloneUtil.copyPropertiesIgnoreNull(plan, schedulingPlan);
        logger.info("修改调度计划----" + schedulingPlan.getJobName());
        //将调度计划转化为jobInformation
        JobInformation jobInformation = check(schedulingPlan);
        TriggerKey triggerKey = jobInformation.getTriggerKey();
        CronTrigger trigger = jobInformation.getTrigger();
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(schedulingPlan.getCronExpressions()).withMisfireHandlingInstructionDoNothing();
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
        scheduler.rescheduleJob(triggerKey, trigger);
        schedulingPlanDao.updateById(schedulingPlan);
    }

    /**
     * 添加调度计划
     */
    public void addSchedulingPlan(SchedulingPlan schedulingPlan) throws SchedulerException {
        logger.info("开始向任务调度中添加定时任务---" + schedulingPlan.getJobName());
        JobDetail jobDetail = JobBuilder.newJob(ExecuteJob.class)
                .withIdentity(schedulingPlan.getJobName(), schedulingPlan.getJobGroup()).build();
        jobDetail.getJobDataMap().put("key", schedulingPlan);
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(schedulingPlan.getCronExpressions()).withMisfireHandlingInstructionDoNothing();
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(schedulingPlan.getJobName(), schedulingPlan.getJobGroup())
                .withSchedule(scheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, trigger);
        //将该定时任务添加到数据库,在重启的时候不需要重新插入
        if (schedulingPlan.getId() == null) {
            schedulingPlanDao.insert(schedulingPlan);
        }
    }


    /**
     * 运行指定计划
     */
    public void runSchedulingPlan(Long id) throws SchedulerException {
        //从数据库查询该调度计划
        SchedulingPlan schedulingPlan = schedulingPlanDao.selectById(id);
        if (schedulingPlan == null) {
            throw new RuntimeException("id:" + id + "的调度计划不存在");
        }
        logger.info("运行调度计划---" + schedulingPlan.getJobName());
        JobInformation jobInformation = check(schedulingPlan);
        scheduler.triggerJob(jobInformation.getJobKey());
    }

    /**
     * 暂停指定调度计划
     */
    public void suspendSchedulingPlan(Long id) throws SchedulerException{
        //从数据库查询该调度计划
        SchedulingPlan schedulingPlan = schedulingPlanDao.selectById(id);
        if (schedulingPlan == null) {
            throw new RuntimeException("id:" + id + "的调度计划不存在");
        }
        logger.info("暂停调度计划---" + schedulingPlan.getJobName());
        JobInformation jobInformation = check(schedulingPlan);
        JobKey jobKey = jobInformation.getJobKey();
        scheduler.pauseJob(jobKey);
        schedulingPlan.setStatus(SCHEDULING_PLAN_PAUSE);
        schedulingPlanDao.updateById(schedulingPlan);
    }


    /**
     * 恢复调度计划
     */
    public void recoverySchedulingPlan(Long id) throws SchedulerException{
        //从数据库查询该调度计划
        SchedulingPlan schedulingPlan = schedulingPlanDao.selectById(id);
        if (schedulingPlan == null) {
            throw new RuntimeException("id:" + id + "的调度计划不存在");
        }
        logger.info("恢复调度计划---" + schedulingPlan.getJobName());
        JobInformation jobInformation = check(schedulingPlan);
        JobKey jobKey = jobInformation.getJobKey();
        scheduler.resumeJob(jobKey);
        schedulingPlan.setStatus(SCHEDULING_PLAN_RUNNING);
        schedulingPlanDao.updateById(schedulingPlan);
    }

    /**
     * 删除指定调度计划
     */
    public void deleteSchedulingPlan(Long id) throws SchedulerException {
        //从数据库查询该调度计划
        SchedulingPlan schedulingPlan = schedulingPlanDao.selectById(id);
        if (schedulingPlan == null) {
            throw new RuntimeException("id:" + id + "的调度计划不存在");
        }
        logger.info("删除指定调度计划---" + schedulingPlan.getJobName());
        JobInformation jobInformation = check(schedulingPlan);
        JobKey jobKey = jobInformation.getJobKey();
        scheduler.deleteJob(jobKey);
        schedulingPlanDao.deleteById(id);
    }


    /**
     * 将调度计划转化为jobInformation
     */
    public JobInformation check(SchedulingPlan schedulingPlan) throws SchedulerException {
        JobInformation jobInformation = new JobInformation();
        JobKey jobKey = JobKey.jobKey(schedulingPlan.getJobName(), schedulingPlan.getJobGroup());
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        TriggerKey triggerKey = TriggerKey.triggerKey(schedulingPlan.getJobName(), schedulingPlan.getJobGroup());
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        jobInformation.setJobKey(jobKey);
        jobInformation.setJobDetail(jobDetail);
        jobInformation.setTriggerKey(triggerKey);
        jobInformation.setTrigger(trigger);
        return jobInformation;
    }

    public ListAndCount<SchedulingPlan> listSchedulingPlans(Integer page, Integer pageLength) {
        IPage<SchedulingPlan> pages = schedulingPlanDao.selectPage(new Page<SchedulingPlan>(page, pageLength),null);
        ListAndCount<SchedulingPlan> listAndCount = new ListAndCount<SchedulingPlan>();
        listAndCount.setList(pages.getRecords());
        listAndCount.setCount(pages.getTotal());
        return listAndCount;
    }

    public List<SchedulingPlan> listAllSchedulingPlans() {
        return schedulingPlanDao.selectList(null);
    }

}
