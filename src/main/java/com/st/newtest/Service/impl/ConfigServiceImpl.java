package com.st.newtest.Service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.st.newtest.Entity.Config;
import com.st.newtest.Mapper.ConfigMapper;
import com.st.newtest.Service.ConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ken
 * @since 2020-09-18
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {

    @Autowired
    private ConfigMapper configMapper;

    @Override
    public List<String> findAllZoneName() {
        List<String> list = null;
        List<Config> configList = new LambdaQueryChainWrapper<Config>(configMapper).eq(Config::getConfigName, "zoneNameConfig").list();
        if (!(configList == null || configList.size() <= 0)) {
            Config config = configList.get(0);
            list = (List<String>) JSONArray.parse(config.getConfigValue());
        }
        return list;
    }
}
