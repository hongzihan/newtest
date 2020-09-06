package com.st.newtest.stGame.Mapper;

import com.st.newtest.stGame.Entity.MonsterDie;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MonsterDieMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MonsterDie record);

    MonsterDie selectByPrimaryKey(Integer id);

    List<MonsterDie> selectAll();

    int updateByPrimaryKey(MonsterDie record);

    List<MonsterDie> selectByZoneName(String zonename);

    List<MonsterDie> selectByZoneAndMobAndMapName(Map<String, String> map);

    List<MonsterDie> selectAllUniqueZoneName();
}