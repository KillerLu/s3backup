package com.shls.s3backup.fill;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.shls.s3backup.fill.filter.FieldFilter;
import com.shls.s3backup.fill.filter.impl.CreateTimeFieldFilter;
import com.shls.s3backup.fill.filter.impl.DeletedFieldFilter;
import com.shls.s3backup.fill.filter.impl.ModifyTimeFieldFilter;
import org.apache.ibatis.reflection.MetaObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：Killer
 * @date ：Created in 20-6-8 上午11:56
 * @description：${description}
 * @modified By：
 * @version: version
 */
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        List<FieldFilter> fillers = new ArrayList<FieldFilter>();
        fillers.add(new DeletedFieldFilter());
        fillers.add(new CreateTimeFieldFilter());
        fillers.add(new ModifyTimeFieldFilter());
        for (FieldFilter filler : fillers) {
            filler.doFiller(this, metaObject);
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        List<FieldFilter> fillers = new ArrayList<FieldFilter>();
        fillers.add(new ModifyTimeFieldFilter());
        for (FieldFilter filler : fillers) {
            filler.doFiller(this, metaObject);
        }
    }
}
