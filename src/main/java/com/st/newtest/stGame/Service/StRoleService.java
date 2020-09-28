package com.st.newtest.stGame.Service;

import com.st.newtest.stGame.Entity.StRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ken
 * @since 2020-09-23
 */
public interface StRoleService extends IService<StRole> {
    int deleteByZoneName(String zoneName);
}
