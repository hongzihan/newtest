package com.st.newtest.stGame.Service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.st.newtest.Util.CommonUtil;
import com.st.newtest.stGame.Entity.MonsterDie;
import com.st.newtest.stGame.Mapper.MonsterDieMapper;
import com.st.newtest.stGame.Service.MonsterDieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

@Service
public class MonsterDieServiceImpl extends ServiceImpl<MonsterDieMapper, MonsterDie> implements MonsterDieService {
    @Autowired(required = false)
    private MonsterDieMapper monsterDieMapper;

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertNewMonster(MonsterDie monsterDie) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("zonename", monsterDie.getZonename());
        hashMap.put("mobname", monsterDie.getMobname());
        hashMap.put("mapname", monsterDie.getMapname());
        List<MonsterDie> monsterDies = monsterDieMapper.selectByMap(hashMap);
        if (monsterDies != null && monsterDies.size() > 0) {
            MonsterDie mobDie = monsterDies.get(0);
            // 计算预估刷新间隔
            // 1.计算两次死亡的时间差 timeTemp
            long timeIntervel = 0;
            try {
                timeIntervel = CommonUtil.calTimeInterval(monsterDie.getDietime(), mobDie.getDietime());
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
            // 2.若数据库内该条数据已经存在刷新间隔，则比较两个刷新间隔，取小的一方
            if (timeIntervel >= 60 * 20) { // 若boss触发鞭尸，则不加入预知时间计算
                if (mobDie.getRelivetime() != null) {
                    if (timeIntervel > mobDie.getRelivetime()) {
                        timeIntervel = mobDie.getRelivetime();
                    }
                }
                mobDie.setRelivetime((int) timeIntervel);
            }
            mobDie.setKiller(monsterDie.getKiller());
            mobDie.setDietime(monsterDie.getDietime());
            return monsterDieMapper.updateById(mobDie);
        } else {
            return monsterDieMapper.insert(monsterDie);
        }
    }

    @Override
    public Boolean clearTargetReliveTime(MonsterDie monsterDie) {
        HashMap<String, Object> hMap;
        hMap = new HashMap<>();
        hMap.put("zonename", monsterDie.getZonename());
        hMap.put("mobname", monsterDie.getMobname());
        hMap.put("mapname", monsterDie.getMapname());
        List<MonsterDie> monsterDies = monsterDieMapper.selectByMap(hMap);
        if (monsterDies != null && monsterDies.size() > 0) {
            MonsterDie mobDie = monsterDies.get(0);
            mobDie.setRelivetime(null);
            monsterDieMapper.updateById(mobDie);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public int deleteByZoneName(String zoneName) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("zonename", zoneName);
        return monsterDieMapper.deleteByMap(map);
    }
}
