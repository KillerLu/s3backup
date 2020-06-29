package com.shls.s3backup.view;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * 备份任务表
 *
 * @author killer
 * @date 2020-06-29
 */
@TableName("backup_task")
public class BackupTask extends Model<BackupTask> {

    private static final long serialVersionUID = 1L;


    //重写这个方法，return当前类的主键
    @Override
    protected Serializable pkVal() {
        return id;
    }


    /**
     * 主键id
     */
    @TableId
    private Long id;
    /**
     * 删除标记
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Boolean isDelete;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date modifyTime;
    /**
     * 备份类型 1:全部文件增备份 2：立即备份当天  3：定时备份昨天
     */
    private Integer backupType;
    /**
     * 任务状态 0:进行中 1:完成
     */
    private Integer taskStatus;
    /**
     * 任务开始时间
     */
    private Date startTime;
    /**
     * 任务结束时间
     */
    private Date endTime;
    /**
     * 备份的ceph
     */
    private String backupCeph;


    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return this.id;
    }


    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }
    public Boolean getIsDelete() {
        return this.isDelete;
    }


    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getCreateTime() {
        return this.createTime;
    }


    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
    public Date getModifyTime() {
        return this.modifyTime;
    }


    public void setBackupType(Integer backupType) {
        this.backupType = backupType;
    }
    public Integer getBackupType() {
        return this.backupType;
    }


    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }
    public Integer getTaskStatus() {
        return this.taskStatus;
    }


    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public Date getStartTime() {
        return this.startTime;
    }


    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    public Date getEndTime() {
        return this.endTime;
    }


    public void setBackupCeph(String backupCeph) {
        this.backupCeph = backupCeph;
    }
    public String getBackupCeph() {
        return this.backupCeph;
    }

}
