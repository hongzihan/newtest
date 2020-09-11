package com.st.newtest.stGame.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.st.newtest.stGame.Entity.WebAction;

import java.util.List;

public interface WebActionService extends IService<WebAction> {
    List<WebAction> selectAndDelAllByZoneid(String zoneid);
}
