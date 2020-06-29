package com.shls.s3backup.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.shls.s3backup.job.ExecuteJob;
import com.shls.s3backup.service.SchedulerService;
import com.shls.s3backup.util.CloneUtil;
import com.shls.s3backup.util.ListAndCount;
import com.shls.s3backup.util.ResponseBuilder;
import com.shls.s3backup.view.SchedulingPlan;
import com.shls.s3backup.vo.BackupTaskVo;
import com.shls.s3backup.vo.SchedulingPlanVo;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

/**
 * @author ：Killer
 * @date ：Created in 19-7-17 下午5:43
 * @description：${description}
 * @modified By：
 * @version: version
 */
@RestController
@RequestMapping("/scheduler")
public class SchedulerController  {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerController.class);

    @Autowired
    public SchedulerService schedulerService;


    //初始化启动所有的Job
    @PostConstruct
    public void initialize() {
        try {
            reStartAllJobs();
            logger.info("INIT SUCCESS");
        } catch (SchedulerException e) {
            logger.info("INIT EXCEPTION : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/listSchedulingPlans",method = RequestMethod.GET)
    public Object listSchedulingPlans(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                       @RequestParam(value = "pageLength", defaultValue = "10") Integer pageLength) {
        ListAndCount<SchedulingPlan> listAndCount = schedulerService.listSchedulingPlans(page, pageLength);
        return new ResponseBuilder().success().data(listAndCount.getList()).add("total", listAndCount.getCount()).build();
    }

    @RequestMapping("/addSchedulingPlan")
    public Object addSchedulingPlan(SchedulingPlanVo schedulingPlanVo) throws SchedulerException {
        SchedulingPlan schedulingPlan = CloneUtil.clone(schedulingPlanVo, SchedulingPlan.class);
        schedulerService.addSchedulingPlan(schedulingPlan);
        return new ResponseBuilder().success().build();
    }

    @RequestMapping(value = "/suspendSchedulingPlan")
    public Object suspendSchedulingPlan(@RequestParam("id") Long id) throws SchedulerException {
        schedulerService.suspendSchedulingPlan(id);
        return new ResponseBuilder().success().build();
    }

    @RequestMapping(value = "/recoverySchedulingPlan",method = RequestMethod.POST)
    public Object recoverySchedulingPlan(Long id) throws SchedulerException {
        schedulerService.recoverySchedulingPlan(id);
        return new ResponseBuilder().success().build();
    }

    @RequestMapping("/deleteSchedulingPlan")
    public Object deleteSchedulingPlan(Long id) throws SchedulerException {
        schedulerService.deleteSchedulingPlan(id);
        return new ResponseBuilder().success().build();
    }

    @RequestMapping(value = "/updateSchedulingPlan",method = RequestMethod.POST)
    public Object updateSchedulingPlan(SchedulingPlanVo schedulingPlanVo) throws SchedulerException {
        SchedulingPlan schedulingPlan = CloneUtil.clone(schedulingPlanVo, SchedulingPlan.class);
        schedulerService.updateSchedulingPlan(schedulingPlan);
        return new ResponseBuilder().success().build();
    }

    @RequestMapping(value = "/runSchedulingPlan",method = RequestMethod.POST)
    public Object runSchedulingPlan(Long id) throws SchedulerException {
        schedulerService.runSchedulingPlan(id);
        return new ResponseBuilder().success().build();
    }

    /**
     * 重新启动所有的job
     */
    private synchronized void reStartAllJobs() throws SchedulerException {
        List<SchedulingPlan> schedulingPlanList=schedulerService.listAllSchedulingPlans();
        if (CollectionUtils.isNotEmpty(schedulingPlanList)) {
            for(SchedulingPlan schedulingPlan :schedulingPlanList) {
                schedulerService.addSchedulingPlan(schedulingPlan);
                if (schedulingPlan.getStatus() == SchedulerService.SCHEDULING_PLAN_PAUSE) {
                    schedulerService.suspendSchedulingPlan(schedulingPlan.getId());
                }
            }
        }
    }
}
