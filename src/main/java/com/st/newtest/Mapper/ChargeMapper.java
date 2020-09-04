package com.st.newtest.Mapper;

import com.st.newtest.Entity.Charge;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChargeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Charge record);

    Charge selectByPrimaryKey(Integer id);

    List<Charge> selectAll();

    int updateByPrimaryKey(Charge record);

    Charge selectByKeyName(String username);
}