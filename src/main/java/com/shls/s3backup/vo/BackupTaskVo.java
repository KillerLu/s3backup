package com.shls.s3backup.vo;

import java.util.Date;

/**
 * @author ：Killer
 * @date ：Created in 20-6-11 下午5:08
 * @description：${description}
 * @modified By：
 * @version: version
 */
public class BackupTaskVo {

    private Long id;

    private Integer taskStatus;

    private String startTime;

    private String endTime;

    private Long successCount;

    private Long failCount;

    private Long size;

    private String readableSize;


    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getReadableSize() {
        return readableSize;
    }

    public void setReadableSize(String readableSize) {
        this.readableSize = readableSize;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Long successCount) {
        this.successCount = successCount;
    }

    public Long getFailCount() {
        return failCount;
    }

    public void setFailCount(Long failCount) {
        this.failCount = failCount;
    }
}
