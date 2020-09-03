package com.st.newtest.Controller;

import com.st.newtest.Service.OpenStService;
import com.st.newtest.Util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChartController {

    @Autowired
    private OpenStService openStService;

    @RequiresPermissions("charts")
    @RequestMapping("/charts")
    public ModelAndView charts() {
        ModelAndView mav = CommonUtil.getPage("charts");
        return mav;
    }
}
