package com.st.newtest.Mapper;

import com.st.newtest.Entity.MonsterDie;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MonsterDieMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MonsterDie record);

    MonsterDie selectByPrimaryKey(Integer id);

    List<MonsterDie> selectAll();

    int updateByPrimaryKey(MonsterDie record);
}