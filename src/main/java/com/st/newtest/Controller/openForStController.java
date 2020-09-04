package com.st.newtest.Controller;

import com.st.newtest.Entity.Charge;
import com.st.newtest.Entity.MonsterDie;
import com.st.newtest.Service.OpenStService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/openSt")
@RestController
public class openForStController {

    @Autowired
    private OpenStService openStService;

    @RequestMapping("/insertNewMonster")
    public String insertNewMonster(MonsterDie monsterDie) {
        openStService.insertNewMonster(monsterDie);
        return "success";
    }

    @RequestMapping("/insertNewChargeData")
    public String insertNewChargeData(Charge charge) {
        openStService.insertNewChargeData(charge);
        return "success";
    }
}
