package com.st.newtest.Mapper;

import com.st.newtest.Entity.DropItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DropItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DropItem record);

    DropItem selectByPrimaryKey(Integer id);

    List<DropItem> selectAll();

    int updateByPrimaryKey(DropItem record);

    List<DropItem> selectByItemKey(String keyname);

    List<DropItem> selectAllByZoneid(String zoneid);

    List<DropItem> selectAllUniqueZoneName();
}