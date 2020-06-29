package com.shls.s3backup.vo;

/**
 * @author ：Killer
 * @date ：Created in 20-6-28 上午11:30
 * @description：${description}
 * @modified By：
 * @version: version
 */
public class CephConfigVo {

    /**
     * 主键id
     */
    private Long id;

    /**
     * ceph名称
     */
    private String cephName;
    /**
     * 备份配置id
     */
    private Long backupConfigId;
    /**
     * 源access_id
     */
    private String srcAccessId;
    /**
     * 源secret_key
     */
    private String srcSecretKey;
    /**
     * 源endpoint
     */
    private String srcEndpoint;
    /**
     * 目的access_id
     */
    private String desAccessId;
    /**
     * 目的secret_key
     */
    private String desSecretKey;
    /**
     * 目的endpoint
     */
    private String desEndpoint;

    /**
     * 访问前缀
     */
    private String url;

    public String getCephName() {
        return cephName;
    }

    public void setCephName(String cephName) {
        this.cephName = cephName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBackupConfigId() {
        return backupConfigId;
    }

    public void setBackupConfigId(Long backupConfigId) {
        this.backupConfigId = backupConfigId;
    }

    public String getSrcAccessId() {
        return srcAccessId;
    }

    public void setSrcAccessId(String srcAccessId) {
        this.srcAccessId = srcAccessId;
    }

    public String getSrcSecretKey() {
        return srcSecretKey;
    }

    public void setSrcSecretKey(String srcSecretKey) {
        this.srcSecretKey = srcSecretKey;
    }

    public String getSrcEndpoint() {
        return srcEndpoint;
    }

    public void setSrcEndpoint(String srcEndpoint) {
        this.srcEndpoint = srcEndpoint;
    }

    public String getDesAccessId() {
        return desAccessId;
    }

    public void setDesAccessId(String desAccessId) {
        this.desAccessId = desAccessId;
    }

    public String getDesSecretKey() {
        return desSecretKey;
    }

    public void setDesSecretKey(String desSecretKey) {
        this.desSecretKey = desSecretKey;
    }

    public String getDesEndpoint() {
        return desEndpoint;
    }

    public void setDesEndpoint(String desEndpoint) {
        this.desEndpoint = desEndpoint;
    }
}
