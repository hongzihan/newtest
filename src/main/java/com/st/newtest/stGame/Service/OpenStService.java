package com.st.newtest.stGame.Service;

import com.st.newtest.stGame.Entity.Charge;
import com.st.newtest.stGame.Entity.MonsterDie;

import java.util.List;

public interface OpenStService {
    List<MonsterDie> selectByZoneName(String zonename);

    List<MonsterDie> selectAllUniqueZoneName();

    int insertNewMonster(MonsterDie monsterDie);

    Boolean clearTargetReliveTime(MonsterDie monsterDie);
}
