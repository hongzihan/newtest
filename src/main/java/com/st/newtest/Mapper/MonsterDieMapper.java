package com.st.newtest.Mapper;

import com.st.newtest.Entity.MonsterDie;
import java.util.List;
import java.util.Map;

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