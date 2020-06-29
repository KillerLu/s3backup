package com.shls.s3backup.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shls.s3backup.dao.CephConfigDao;
import com.shls.s3backup.view.CephConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author ：Killer
 * @date ：Created in 20-6-28 上午10:12
 * @description：${description}
 * @modified By：
 * @version: version
 */
@Service
public class ConfigService {

    @Autowired
    private CephConfigDao cephConfigDao;


    public void addOrUpdateCephConfig(CephConfig config) {
        if (config.getId() == null) {
            cephConfigDao.insert(config);
        }else{
            cephConfigDao.updateById(config);
        }
    }

    public List<CephConfig> listCephConfigs() {
        return cephConfigDao.selectList(null);
    }

    public CephConfig getCephConfigDetail(Long id) {
        return cephConfigDao.selectById(id);
    }
}
