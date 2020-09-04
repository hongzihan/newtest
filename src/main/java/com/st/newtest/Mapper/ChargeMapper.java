package com.st.newtest.Mapper;

import com.st.newtest.Entity.Charge;
import java.util.List;

public interface ChargeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Charge record);

    Charge selectByPrimaryKey(Integer id);

    List<Charge> selectAll();

    int updateByPrimaryKey(Charge record);
}