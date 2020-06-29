package com.shls.s3backup.vo;

/**
 * @author ：Killer
 * @date ：Created in 20-6-11 下午8:42
 * @description：${description}
 * @modified By：
 * @version: version
 */
public class BackupRecordVo {

    private Long id;

    private String bucket;

    private String uniqueKey;

    private Integer backupResult;

    private Integer backupType;

    private String backupDate;

    private String sourceDate;

    private Long taskId;

    private Long size;

    private String readableSize;

    public String getReadableSize() {
        return readableSize;
    }

    public void setReadableSize(String readableSize) {
        this.readableSize = readableSize;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public Integer getBackupResult() {
        return backupResult;
    }

    public void setBackupResult(Integer backupResult) {
        this.backupResult = backupResult;
    }

    public Integer getBackupType() {
        return backupType;
    }

    public void setBackupType(Integer backupType) {
        this.backupType = backupType;
    }

    public String getBackupDate() {
        return backupDate;
    }

    public void setBackupDate(String backupDate) {
        this.backupDate = backupDate;
    }

    public String getSourceDate() {
        return sourceDate;
    }

    public void setSourceDate(String sourceDate) {
        this.sourceDate = sourceDate;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
