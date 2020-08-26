package com.st.newtest.Service;

import com.st.newtest.Entity.TbItem;

import java.util.List;

public interface TbItemService {
    Integer deleteByPrimaryKey(Integer id);

    Integer insert(TbItem record);

    TbItem selectByPrimaryKey(Integer id);

    List<TbItem> selectAll();

    Integer updateByPrimaryKey(TbItem record);
}
