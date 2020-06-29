package com.shls.s3backup.fill.filter.impl;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.shls.s3backup.fill.filter.FieldFilter;
import com.shls.s3backup.util.DateUtil;
import org.apache.ibatis.reflection.MetaObject;

/**
 * @author ：Killer
 * @date ：Created in 19-7-2 下午3:05
 * @description：${description}
 * @modified By：
 * @version: version
 */
public class ModifyTimeFieldFilter implements FieldFilter {

    /**
     * 属性名称
     */
    private static final String FIELD_NAME = "modifyTime";

    @Override
    public void doFiller(MetaObjectHandler metaObjectHandler, MetaObject metaObject) {
        if (metaObject.hasSetter(FIELD_NAME)) {
            metaObjectHandler.setFieldValByName(FIELD_NAME, DateUtil.getNow(), metaObject);
        }
    }
}
