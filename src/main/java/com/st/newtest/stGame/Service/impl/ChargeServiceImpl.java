package com.st.newtest.stGame.Service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.st.newtest.stGame.Entity.Charge;
import com.st.newtest.stGame.Mapper.ChargeMapper;
import com.st.newtest.stGame.Service.ChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class ChargeServiceImpl extends ServiceImpl<ChargeMapper, Charge> implements ChargeService {
    @Autowired
    private ChargeMapper chargeMapper;

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public int insertNewChargeData(Charge charge) {
        String dateTime = df.format(new Date().getTime());
        charge.setDateTime(dateTime);
        charge.setChargeCount(1);
        HashMap<String, Object> map = new HashMap<>();
        map.put("accountName", charge.getAccountName());
        map.put("zoneName", charge.getZoneName());
        List<Charge> chargeList = chargeMapper.selectByMap(map);
        Charge chargeNew = null;
        if (chargeList != null) {
            chargeNew = chargeList.get(0);
        }
        if (chargeNew != null) {
            chargeNew.setDateTime(dateTime);
            chargeNew.setChargeCount(chargeNew.getChargeCount() + charge.getChargeCount());
            chargeNew.setChargeNum(chargeNew.getChargeNum() + charge.getChargeNum());
            chargeNew.setUsername(charge.getUsername());
            chargeMapper.updateById(chargeNew);
        } else {
            chargeMapper.insert(charge);
        }
        return 0;
    }

    @Override
    public List<String> selectAllZoneNameForChargeTable() {
        List<Charge> chargeList = new LambdaQueryChainWrapper<Charge>(chargeMapper).groupBy(Charge::getZoneName).select(Charge::getZoneName).list();
        List<String> list = null;
        if (chargeList != null) {
            list = new ArrayList<>();
            for (Charge charge : chargeList) {
                list.add(charge.getZoneName());
            }
        }
        return list;
    }
}
