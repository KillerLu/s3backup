package com.shls.s3backup.controller;

import com.shls.s3backup.service.BackupService;
import com.shls.s3backup.util.CloneUtil;
import com.shls.s3backup.util.ListAndCount;
import com.shls.s3backup.util.ResponseBuilder;
import com.shls.s3backup.view.BackupTask;
import com.shls.s3backup.vo.BackupRecordVo;
import com.shls.s3backup.vo.BackupTaskVo;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author ：Killer
 * @date ：Created in 20-6-11 下午5:05
 * @description：${description}
 * @modified By：
 * @version: version
 */
@RestController
@RequestMapping("/backup")
public class BackupController {

    @Autowired
    private BackupService backupService;

    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
        //只要网页中传来的数据格式为yyyy-MM-dd 就会转化为Date类型
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"),
                true));
    }

    @RequestMapping(value = "/listBackupTaskDetail", method = RequestMethod.GET)
    public Object listBackupTaskDetail(@RequestParam(value = "backupCeph") String backupCeph,
                                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                                       @RequestParam(value = "pageLength", defaultValue = "10") Integer pageLength) {
        ListAndCount<BackupTaskVo> listAndCount = backupService.listBackupTaskDetail(backupCeph,page, pageLength);
        return new ResponseBuilder().success().data(listAndCount.getList()).add("total", listAndCount.getCount()).build();
    }

    @RequestMapping(value = "/listRecordByTaskId", method = RequestMethod.GET)
    public Object listRecordByTaskId(@RequestParam(value = "taskId") Long taskId,
                                     @RequestParam(value = "page", defaultValue = "1") Integer page,
                                     @RequestParam(value = "pageLength", defaultValue = "10") Integer pageLength) {
        ListAndCount<BackupRecordVo> listAndCount = backupService.listBackupRecordByTaskId(false,taskId, page, pageLength);
        return new ResponseBuilder().success().data(listAndCount.getList()).add("total", listAndCount.getCount()).build();
    }

    @RequestMapping(value = "/listFailRecordByTaskId", method = RequestMethod.GET)
    public Object listFailRecordByTaskId(@RequestParam(value = "taskId") Long taskId,
                                     @RequestParam(value = "page", defaultValue = "1") Integer page,
                                     @RequestParam(value = "pageLength", defaultValue = "10") Integer pageLength) {
        ListAndCount<BackupRecordVo> listAndCount = backupService.listBackupRecordByTaskId(true,taskId, page, pageLength);
        return new ResponseBuilder().success().data(listAndCount.getList()).add("total", listAndCount.getCount()).build();
    }

    @RequestMapping(value = "/createBackupTask", method = RequestMethod.POST)
    public Object createBackupTask(@RequestParam("backupCeph")String backupCeph,
                                   @RequestParam("backupType")Integer backupType,
                                   @RequestParam("backupDate")Date backupDate) {
        if (backupType == BackupService.BACKUP_BY_DATE && backupDate == null) {
            throw new RuntimeException("请指定日期");
        }
        if (StringUtils.isBlank(backupCeph)) {
            throw new RuntimeException("请指定备份ceph");
        }
        backupService.backupS3(backupType,backupCeph,backupDate);
        return new ResponseBuilder().success().build();
    }


}

