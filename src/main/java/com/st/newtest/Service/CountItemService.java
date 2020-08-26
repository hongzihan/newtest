package com.st.newtest.Service;

import com.st.newtest.Entity.CountItem;

import java.util.List;

public interface CountItemService {
    int deleteByPrimaryKey(Integer id);

    int insert(CountItem record);

    CountItem selectByPrimaryKey(Integer id);

    List<CountItem> selectAll();

    int updateByPrimaryKey(CountItem record);

    CountItem selectByItemKey(String keyname);

    Boolean insertNewData(CountItem countItem);
}
