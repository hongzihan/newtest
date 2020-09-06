package com.st.newtest.stGame.Service;

import com.st.newtest.stGame.Entity.WebAction;

import java.util.List;

public interface WebActionService {
    int deleteByPrimaryKey(Integer id);

    int insert(WebAction record);

    WebAction selectByPrimaryKey(Integer id);

    List<WebAction> selectAll();

    int updateByPrimaryKey(WebAction record);

    List<WebAction> selectByZoneid(String zoneid);

    List<WebAction> selectAndDelAllByZoneid(String zoneid);
}
