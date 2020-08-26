package com.st.newtest.Service;

import com.st.newtest.Entity.CrudStItem;

import java.util.List;

public interface CrudStItemService {
    int deleteByPrimaryKey(Integer id);

    int insert(CrudStItem record);

    CrudStItem selectByPrimaryKey(Integer id);

    List<CrudStItem> selectAll();

    int updateByPrimaryKey(CrudStItem record);

    List<CrudStItem> selectAndDelAllByZoneid(String zoneid);

    List<CrudStItem> selectByZoneid(String zoneid);
}
