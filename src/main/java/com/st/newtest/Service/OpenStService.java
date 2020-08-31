package com.st.newtest.Service;

import com.st.newtest.Entity.MonsterDie;

import java.util.List;

public interface OpenStService {
    List<MonsterDie> selectByZoneName(String zonename);

    List<MonsterDie> selectAllUniqueZoneName();

    int insertNewMonster(MonsterDie monsterDie);
}
