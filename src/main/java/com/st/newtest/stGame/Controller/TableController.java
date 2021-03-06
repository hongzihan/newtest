package com.st.newtest.stGame.Controller;

import com.st.newtest.stGame.Entity.Charge;
import com.st.newtest.stGame.Service.ChargeService;
import com.st.newtest.Util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequestMapping("/table")
@Controller
public class TableController {

    @Autowired
    private ChargeService chargeService;


    @RequestMapping("/chargeTable")
    @RequiresRoles("administrator")
    public ModelAndView chargeTable(Charge charge) {
        ModelAndView mav = CommonUtil.getPage("chargeTable");
        // 查所有的区名
        List<String> stringList = chargeService.selectAllZoneNameForChargeTable();
        if (stringList != null) {
            mav.addObject("zoneNameList", stringList);
        }
        if (charge.getZoneName() != null) {
            mav.addObject("curZoneName", charge.getZoneName());
            List<Charge> charges = chargeService.lambdaQuery().eq(Charge::getZoneName, charge.getZoneName()).list();
            if (charges != null) {
                mav.addObject("chargeList", charges);
            }
        }
        return mav;
    }
}
