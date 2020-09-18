package com.st.newtest.Service.impl;

import com.st.newtest.Entity.Config;
import com.st.newtest.Mapper.ConfigMapper;
import com.st.newtest.Service.ConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
