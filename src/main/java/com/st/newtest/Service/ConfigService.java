package com.st.newtest.Service;

import com.st.newtest.Entity.Config;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ken
 * @since 2020-09-18
 */
public interface ConfigService extends IService<Config> {
    List<Map> findAllZoneName();
}
