package com.st.newtest.stGame.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.st.newtest.stGame.Entity.DropItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface DropItemMapper extends BaseMapper<DropItem> {
    int deleteByPrimaryKey(Integer id);

    int insert(DropItem record);

    DropItem selectByPrimaryKey(Integer id);

    List<DropItem> selectAll();

    int updateByPrimaryKey(DropItem record);

    List<DropItem> selectByItemKey(String keyname);

    List<DropItem> selectAllByZoneid(String zoneid);

    List<DropItem> selectAllUniqueZoneName();
}