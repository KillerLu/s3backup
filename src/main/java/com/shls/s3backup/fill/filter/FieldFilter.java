package com.shls.s3backup.fill.filter;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

/**
 * 字段填充接口
 *
 * @author mq
 */
public interface FieldFilter {

    /**
     * 字段填充
     *
     * @param metaObject 字段
     */
    void doFiller(MetaObjectHandler metaObjectHandler, MetaObject metaObject);
}
