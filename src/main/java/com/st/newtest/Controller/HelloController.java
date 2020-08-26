package com.st.newtest.Controller;

import com.st.newtest.Entity.User;
import com.st.newtest.Util.CommonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class HelloController {
    private User user = null;
    @RequestMapping("/")
    public ModelAndView welcomePage(HttpSession session) {
        return CommonUtil.getPage("index", session);
    }

    @RequestMapping("/index")
    public ModelAndView sayHi(HttpSession session) {
        return CommonUtil.getPage("index", session);
    }

    @RequestMapping("/form-common")
    public ModelAndView newForm1(HttpSession session) {
        return CommonUtil.getPage("form-common", session);
    }

    @RequestMapping("/charts")
    public ModelAndView charts(HttpSession session) {
        return CommonUtil.getPage("charts", session);
    }

    @RequestMapping("/tables")
    public ModelAndView tables(HttpSession session) {
        return CommonUtil.getPage("tables", session);
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }
}
