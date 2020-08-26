package com.st.newtest.Mapper;

import com.st.newtest.Entity.CountItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CountItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CountItem record);

    CountItem selectByPrimaryKey(Integer id);

    List<CountItem> selectAll();

    int updateByPrimaryKey(CountItem record);

    CountItem selectByItemKey(String keyname);
}