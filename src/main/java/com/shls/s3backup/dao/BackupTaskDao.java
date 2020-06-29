package com.shls.s3backup.dao;

import com.shls.s3backup.view.BackupTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shls.s3backup.vo.BackupTaskVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 备份任务表 mapper
 * 
 * @author killer
 * @date 2020-06-11
 */
public interface BackupTaskDao extends BaseMapper<BackupTask> {

    List<BackupTaskVo> listBackupTaskDetail(@Param("backupCeph") String backupCeph,@Param("offset") Integer offset, @Param("length") Integer length);

}
