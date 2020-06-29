package com.shls.s3backup.view;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * 备份记录表
 *
 * @author killer
 * @date 2020-06-12
 */
@TableName("backup_record")
public class BackupRecord extends Model<BackupRecord> {

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
     * 是否删除
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
     * 备份bucket
     */
    private String bucket;
    /**
     * 备份key
     */
    private String uniqueKey;
    /**
     * 备份结果 0:备份中  1:备份成功  2:备份失败
     */
    private Integer backupResult;
    /**
     * 备份方式
     */
    private Integer backupType;
    /**
     * 备份日期
     */
    private String backupDate;
    /**
     * 备份文件的上传日期
     */
    private String sourceDate;
    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 文件大小
     */
    private Long size;


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


    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
    public String getBucket() {
        return this.bucket;
    }


    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }
    public String getUniqueKey() {
        return this.uniqueKey;
    }


    public void setBackupResult(Integer backupResult) {
        this.backupResult = backupResult;
    }
    public Integer getBackupResult() {
        return this.backupResult;
    }


    public void setBackupType(Integer backupType) {
        this.backupType = backupType;
    }
    public Integer getBackupType() {
        return this.backupType;
    }


    public void setBackupDate(String backupDate) {
        this.backupDate = backupDate;
    }
    public String getBackupDate() {
        return this.backupDate;
    }


    public void setSourceDate(String sourceDate) {
        this.sourceDate = sourceDate;
    }
    public String getSourceDate() {
        return this.sourceDate;
    }


    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
    public Long getTaskId() {
        return this.taskId;
    }


    public void setSize(Long size) {
        this.size = size;
    }
    public Long getSize() {
        return this.size;
    }

}
