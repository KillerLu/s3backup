package com.shls.s3backup.view;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * 调度计划(定时任务)
 *
 * @author killer
 * @date 2020-06-12
 */
@TableName("scheduling_plan")
public class SchedulingPlan extends Model<SchedulingPlan> {

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
     * 是否删除标记
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
     * 计划名称
     */
    private String jobName;
    /**
     * 计划组名
     */
    private String jobGroup;
    /**
     * cron表达式
     */
    private String cronExpressions;
    /**
     * 调度计划状态:0:开启 1:暂停
     */
    private Integer status;


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


    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
    public String getJobName() {
        return this.jobName;
    }


    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }
    public String getJobGroup() {
        return this.jobGroup;
    }


    public void setCronExpressions(String cronExpressions) {
        this.cronExpressions = cronExpressions;
    }
    public String getCronExpressions() {
        return this.cronExpressions;
    }


    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getStatus() {
        return this.status;
    }

}
