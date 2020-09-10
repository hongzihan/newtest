package com.st.newtest.stGame.Mapper;

import com.st.newtest.stGame.Entity.WebAction;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WebActionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(WebAction record);

    WebAction selectByPrimaryKey(Integer id);

    List<WebAction> selectAll();

    int updateByPrimaryKey(WebAction record);

    List<WebAction> selectByZoneid(String zoneid);
}