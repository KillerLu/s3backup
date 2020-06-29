package com.shls.s3backup.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.shls.s3backup.service.BackupService;
import com.shls.s3backup.service.ConfigService;
import com.shls.s3backup.service.SchedulerService;
import com.shls.s3backup.util.CloneUtil;
import com.shls.s3backup.util.ListAndCount;
import com.shls.s3backup.util.ResponseBuilder;
import com.shls.s3backup.view.CephConfig;
import com.shls.s3backup.vo.CephConfigVo;
import com.shls.s3backup.vo.SchedulingPlanVo;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

/**
 * @author ：Killer
 * @date ：Created in 19-7-17 下午5:43
 * @description：${description}
 * @modified By：
 * @version: version
 */
@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @RequestMapping(value = "/listCephConfig", method = RequestMethod.GET)
    public Object listCephConfig() {
        List<CephConfig> configs = configService.listCephConfigs();
        List<CephConfigVo> vos = CloneUtil.batchClone(configs, CephConfigVo.class);
        return new ResponseBuilder().success().data(vos).add("total", vos.size()).build();
    }

    @RequestMapping(value = "/createCephConfig", method = RequestMethod.POST)
    public Object createCephConfig(CephConfigVo vo) {
        CephConfig config = CloneUtil.clone(vo, CephConfig.class);
        configService.addOrUpdateCephConfig(config);
        return new ResponseBuilder().success().build();
    }

    @RequestMapping(value = "/getCephConfigDetail", method = RequestMethod.GET)
    public Object getCephConfigDetail(@RequestParam("id") Long id) {
        CephConfig config = configService.getCephConfigDetail(id);
        CephConfigVo vo = CloneUtil.clone(config, CephConfigVo.class);
        return new ResponseBuilder().success().data(vo).build();
    }
}
