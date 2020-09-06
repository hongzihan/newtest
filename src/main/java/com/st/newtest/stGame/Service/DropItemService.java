package com.st.newtest.stGame.Service;

import com.st.newtest.stGame.Entity.DropItem;

import java.util.List;

public interface DropItemService {
    int deleteByPrimaryKey(Integer id);

    int insert(DropItem record);

    DropItem selectByPrimaryKey(Integer id);

    List<DropItem> selectAll();

    int updateByPrimaryKey(DropItem record);

    List<DropItem> selectByItemKey(String keyname);

    Boolean insertNewData(DropItem dropItem);

    List<DropItem> selectAllByZoneid(String zoneid);

    List<String> selectAllUniqueZoneName();
}
