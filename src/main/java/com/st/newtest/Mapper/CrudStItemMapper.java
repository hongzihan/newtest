package com.st.newtest.Mapper;

import com.st.newtest.Entity.CrudStItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CrudStItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CrudStItem record);

    CrudStItem selectByPrimaryKey(Integer id);

    List<CrudStItem> selectAll();

    int updateByPrimaryKey(CrudStItem record);

    List<CrudStItem> selectByZoneid(String zoneid);
}