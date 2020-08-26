package com.st.newtest.Mapper;

import com.st.newtest.Entity.TbItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TbItemMapper {
    Integer deleteByPrimaryKey(Integer id);

    Integer insert(TbItem record);

    TbItem selectByPrimaryKey(Integer id);

    List<TbItem> selectAll();

    Integer updateByPrimaryKey(TbItem record);
}