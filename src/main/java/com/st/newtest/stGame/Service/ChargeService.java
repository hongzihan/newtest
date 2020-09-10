package com.st.newtest.stGame.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.st.newtest.stGame.Entity.Charge;

import java.util.List;

public interface ChargeService extends IService<Charge> {
    int insertNewChargeData(Charge charge);

    List<String> selectAllZoneNameForChargeTable();
}
