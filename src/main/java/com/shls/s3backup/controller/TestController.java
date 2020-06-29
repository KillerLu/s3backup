package com.shls.s3backup.controller;

import com.shls.s3backup.dao.BackupRecordDao;
import com.shls.s3backup.dao.BackupTaskDao;
import com.shls.s3backup.view.BackupRecord;
import com.shls.s3backup.view.BackupTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ：Killer
 * @date ：Created in 20-6-8 上午11:16
 * @description：${description}
 * @modified By：
 * @version: version
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private BackupRecordDao backupRecordDao;
    @Autowired
    private BackupTaskDao backupTaskDao;

    @RequestMapping("/getAll")
    public List<BackupTask>  getAll() {
        List<BackupTask> list = backupTaskDao.selectList(null);
        return list;
    }
    @RequestMapping("/insert")
    public String insert() {
        BackupRecord record=new BackupRecord();
        record.setBucket("aaa");
        record.setUniqueKey("dsfsdf");
        backupRecordDao.insert(record);
        System.out.println(record.getId());
        return "123";
    }
}
