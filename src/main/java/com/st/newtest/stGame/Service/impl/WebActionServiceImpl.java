package com.st.newtest.stGame.Service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.st.newtest.stGame.Entity.WebAction;
import com.st.newtest.stGame.Mapper.WebActionMapper;
import com.st.newtest.stGame.Service.WebActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("webActionServiceImpl")
public class WebActionServiceImpl extends ServiceImpl<WebActionMapper, WebAction> implements WebActionService {

    @Autowired(required = false)
    private WebActionMapper webActionMapper;

    @Override
    public List<WebAction> selectAndDelAllByZoneid(String zoneid) {
        List<WebAction> waList = new LambdaQueryChainWrapper<WebAction>(webActionMapper).eq(WebAction::getZoneid, zoneid).list();
        for (WebAction webAction : waList) {
            webActionMapper.deleteById(webAction.getId());
        }
        return waList;
    }
}
