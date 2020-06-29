package com.shls.s3backup.view;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * ceph配置表
 *
 * @author killer
 * @date 2020-06-28
 */
@TableName("ceph_config")
public class CephConfig extends Model<CephConfig> {

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
     * ceph名称标识
     */
    private String cephName;
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


    public void setCephName(String cephName) {
        this.cephName = cephName;
    }
    public String getCephName() {
        return this.cephName;
    }


    public void setSrcAccessId(String srcAccessId) {
        this.srcAccessId = srcAccessId;
    }
    public String getSrcAccessId() {
        return this.srcAccessId;
    }


    public void setSrcSecretKey(String srcSecretKey) {
        this.srcSecretKey = srcSecretKey;
    }
    public String getSrcSecretKey() {
        return this.srcSecretKey;
    }


    public void setSrcEndpoint(String srcEndpoint) {
        this.srcEndpoint = srcEndpoint;
    }
    public String getSrcEndpoint() {
        return this.srcEndpoint;
    }


    public void setDesAccessId(String desAccessId) {
        this.desAccessId = desAccessId;
    }
    public String getDesAccessId() {
        return this.desAccessId;
    }


    public void setDesSecretKey(String desSecretKey) {
        this.desSecretKey = desSecretKey;
    }
    public String getDesSecretKey() {
        return this.desSecretKey;
    }


    public void setDesEndpoint(String desEndpoint) {
        this.desEndpoint = desEndpoint;
    }
    public String getDesEndpoint() {
        return this.desEndpoint;
    }


    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return this.url;
    }

}
