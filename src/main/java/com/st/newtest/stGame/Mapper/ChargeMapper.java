package com.st.newtest.stGame.Mapper;

import com.st.newtest.stGame.Entity.Charge;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ChargeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Charge record);

    Charge selectByPrimaryKey(Integer id);

    List<Charge> selectAll();

    int updateByPrimaryKey(Charge record);

    Charge selectByAccountAndZoneName(Map<String,String> map);

    List<Charge> selectAllUniqueZoneName();

    List<Charge> selectAllByZoneName(String zoneName);
}