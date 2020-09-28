package com.st.newtest.stGame.Service.impl;

import com.st.newtest.stGame.Entity.StRole;
import com.st.newtest.stGame.Mapper.StRoleMapper;
import com.st.newtest.stGame.Service.StRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ken
 * @since 2020-09-23
 */
@Service
public class StRoleServiceImpl extends ServiceImpl<StRoleMapper, StRole> implements StRoleService {
    @Autowired
    private StRoleMapper stRoleMapper;
    @Override
    public int deleteByZoneName(String zoneName) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("zone_name", zoneName);
        return stRoleMapper.deleteByMap(map);
    }
}
