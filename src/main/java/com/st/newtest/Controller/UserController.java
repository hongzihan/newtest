package com.st.newtest.Controller;

import com.st.newtest.Entity.User;
import com.st.newtest.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RequestMapping("/user")
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public String login(String username, String password, HttpSession session) throws IOException {
        System.out.println("username = " + username + " password = " + password + " 请求登录！");
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        User newUser = userService.login(user);
        System.out.println(newUser);
        if (newUser == null) {
            return "{code: 500, msg: '失败'}";
        }
        session.setAttribute("user", newUser);
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("index");
//        return modelAndView;
        return "{code: 200, msg: '成功'}";
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/logout")
    public String logout() {
        return "请求接收成功";
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/regist")
    public String regist() {
        return "请求接收成功";
    }
}
