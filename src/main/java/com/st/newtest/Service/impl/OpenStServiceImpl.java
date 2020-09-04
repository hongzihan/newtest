package com.st.newtest.Service.impl;

import com.st.newtest.Entity.Charge;
import com.st.newtest.Entity.MonsterDie;
import com.st.newtest.Mapper.ChargeMapper;
import com.st.newtest.Mapper.MonsterDieMapper;
import com.st.newtest.Service.OpenStService;
import com.st.newtest.Util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service("openStServiceImpl")
public class OpenStServiceImpl implements OpenStService {
    @Autowired(required = false)
    private MonsterDieMapper monsterDieMapper;

    @Autowired(required = false)
    private ChargeMapper chargeMapper;

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<MonsterDie> selectByZoneName(String zonename) {
        if (zonename != null && zonename != "") {
            return monsterDieMapper.selectByZoneName(zonename);
        }
        return null;
    }

    @Override
    public List<MonsterDie> selectAllUniqueZoneName() {
        return monsterDieMapper.selectAllUniqueZoneName();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertNewMonster(MonsterDie monsterDie) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("zonename", monsterDie.getZonename());
        hashMap.put("mobname", monsterDie.getMobname());
        hashMap.put("mapname", monsterDie.getMapname());
        List<MonsterDie> monsterDies = monsterDieMapper.selectByZoneAndMobAndMapName(hashMap);
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
            return monsterDieMapper.updateByPrimaryKey(mobDie);
        } else {
            return monsterDieMapper.insert(monsterDie);
        }
    }

    @Override
    public Boolean clearTargetReliveTime(MonsterDie monsterDie) {
        HashMap<String, String> hMap;
        hMap = new HashMap<>();
        hMap.put("zonename", monsterDie.getZonename());
        hMap.put("mobname", monsterDie.getMobname());
        hMap.put("mapname", monsterDie.getMapname());
        List<MonsterDie> monsterDies = monsterDieMapper.selectByZoneAndMobAndMapName(hMap);
        if (monsterDies != null && monsterDies.size() > 0) {
            MonsterDie mobDie = monsterDies.get(0);
            mobDie.setRelivetime(null);
            monsterDieMapper.updateByPrimaryKey(mobDie);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public int insertNewChargeData(Charge charge) {
        String dateTime = df.format(new Date().getTime());
        charge.setDateTime(dateTime);
        charge.setChargeCount(1);
        Charge chargeNew = chargeMapper.selectByKeyName(charge.getUsername());
        if (chargeNew != null) {
            chargeNew.setDateTime(dateTime);
            chargeNew.setChargeCount(chargeNew.getChargeCount() + charge.getChargeCount());
            chargeNew.setChargeNum(chargeNew.getChargeNum() + charge.getChargeNum());

        } else {
            chargeMapper.insert(charge);
        }
        return 0;
    }
}
