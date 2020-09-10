package com.st.newtest.stGame.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.st.newtest.stGame.Entity.MonsterDie;

import java.util.List;

public interface MonsterDieService extends IService<MonsterDie> {

    int insertNewMonster(MonsterDie monsterDie);

    Boolean clearTargetReliveTime(MonsterDie monsterDie);
}
