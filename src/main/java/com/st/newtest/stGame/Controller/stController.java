package com.st.newtest.stGame.Controller;

import com.st.newtest.stGame.Entity.MonsterDie;
import com.st.newtest.stGame.Service.DropItemService;
import com.st.newtest.stGame.Service.OpenStService;
import com.st.newtest.Util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RequestMapping("/st")
@RestController
public class stController {

    @Autowired
    private OpenStService openStService;

    @Autowired
    private DropItemService dropItemService;

    @RequestMapping(method = RequestMethod.GET, value = "/getHello/{name}")
    public ModelMap getHello(@PathVariable("name") String name) {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("code", "200");
        modelMap.addAttribute("message", "hello" + name);
        return modelMap;
    }

    @RequiresPermissions("searchMonsterMsg")
    @RequestMapping("/searchMonsterMsg")
    public ModelAndView searchMonsterMsg(String zonename) throws ParseException {
        List<MonsterDie> monsterDies = openStService.selectByZoneName(zonename);
        List<MonsterDie> auz = openStService.selectAllUniqueZoneName();
        ModelAndView mav = CommonUtil.getPage("monsterDieMsg");
        if (monsterDies != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dNow = new Date( ); // 当前时间
            for (MonsterDie monsterDie : monsterDies) {
                if (monsterDie.getRelivetime() != null) {
                    Date x = df.parse(monsterDie.getDietime());
                    long l = x.getTime() + monsterDie.getRelivetime() * 1000; // 未来出生时间
                    monsterDie.setFutureBornTime(df.format(l));
                    if (dNow.getTime() - l >= 0) {
                        monsterDie.setMobStatus("存活");
                    } else {
                        monsterDie.setMobStatus("死亡");
                    }
                } else {
                    monsterDie.setMobStatus("未知");
                }
            }
            mav.addObject("mobList", monsterDies);
        }
        mav.addObject("zonename", zonename);
        mav.addObject("zoneList", auz);
        return mav;
    }

    @RequiresPermissions("clearRelive")
    @ResponseBody
    @RequestMapping("/clearRelive")
    public String clearRelive(MonsterDie monsterDie) {
        if (!openStService.clearTargetReliveTime(monsterDie)) {
            return "failed";
        }
        return "success";
    }

    @RequiresPermissions("form-common")
    @RequestMapping("/form-common")
    public ModelAndView newForm1() {
        ModelAndView page = CommonUtil.getPage("form-common");
        List<String> strings = dropItemService.selectAllUniqueZoneName();
        if (strings != null) {
            page.addObject("zoneNameList", strings);
        }
        return page;
    }
}
