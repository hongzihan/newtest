package com.st.newtest.Service;

import com.st.newtest.Entity.WebAction;

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
