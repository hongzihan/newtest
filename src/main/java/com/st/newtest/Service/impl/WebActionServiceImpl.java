package com.st.newtest.Service.impl;

import com.st.newtest.Entity.WebAction;
import com.st.newtest.Mapper.WebActionMapper;
import com.st.newtest.Service.WebActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("webActionServiceImpl")
public class WebActionServiceImpl implements WebActionService {

    @Autowired(required = false)
    private WebActionMapper webActionMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return webActionMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(WebAction record) {
        return webActionMapper.insert(record);
    }

    @Override
    public WebAction selectByPrimaryKey(Integer id) {
        return webActionMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<WebAction> selectAll() {
        return webActionMapper.selectAll();
    }

    @Override
    public int updateByPrimaryKey(WebAction record) {
        return webActionMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<WebAction> selectByZoneid(String zoneid) {
        return webActionMapper.selectByZoneid(zoneid);
    }

    @Override
    public List<WebAction> selectAndDelAllByZoneid(String zoneid) {
        List<WebAction> waList = selectByZoneid(zoneid);
        for (WebAction webAction : waList) {
            deleteByPrimaryKey(webAction.getId());
        }
        return waList;
    }
}
