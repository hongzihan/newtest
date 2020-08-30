package com.st.newtest.Service.impl;

import com.st.newtest.Mapper.MonsterDieMapper;
import com.st.newtest.Service.OpenStService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("openStServiceImpl")
public class OpenStServiceImpl implements OpenStService {
    @Autowired(required = false)
    private MonsterDieMapper monsterDieMapper;
}
