package com.st.newtest.Controller;

import com.st.newtest.Entity.MonsterDie;
import com.st.newtest.Entity.User;
import com.st.newtest.Util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;


@Controller
public class HelloController {
    private User user = null;
    @RequestMapping("/")
    public ModelAndView welcomePage() {
        return CommonUtil.getPage("index");
    }

    //@RequiresRoles("supermanager")
    @RequiresPermissions("index")
    @RequestMapping("/index")
    public ModelAndView sayHi() {
        return CommonUtil.getPage("index");
    }

    @RequiresPermissions("form-common")
    @RequestMapping("/form-common")
    public ModelAndView newForm1() {
        return CommonUtil.getPage("form-common");
    }

    @RequiresRoles("supermanager")
    @RequestMapping("/form-permission")
    public ModelAndView formPermission() { return CommonUtil.getPage("form-validation");}

    @RequiresPermissions("charts")
    @RequestMapping("/charts")
    public ModelAndView charts() {
        return CommonUtil.getPage("charts");
    }

    @RequiresPermissions("tables")
    @RequestMapping("/tables")
    public ModelAndView tables() {
        return CommonUtil.getPage("tables");
    }

    @RequiresRoles("supermanager")
    @RequestMapping("/monsterDieMsg")
    public ModelAndView monsterDieMsg() {
        ArrayList<MonsterDie> moblist = new ArrayList<>();
        for (int i=0; i<=4; i++) {
            MonsterDie monsterDie = new MonsterDie();
            monsterDie.setDietime("8-31 12:2" + i);
            monsterDie.setMobname("野猪王" + i);
            monsterDie.setKiller("杨大吊" + i);
            monsterDie.setZonename("测试服" + i);
            moblist.add(monsterDie);
        }
        ModelAndView modelAndView = CommonUtil.getPage("monsterDieMsg");
        modelAndView.addObject("moblist", moblist);
        return modelAndView;
    }

    @RequestMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }
}
