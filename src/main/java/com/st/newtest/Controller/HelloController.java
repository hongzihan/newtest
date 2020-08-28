package com.st.newtest.Controller;

import com.st.newtest.Entity.User;
import com.st.newtest.Util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

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

    @RequestMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }
}
