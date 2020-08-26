package com.st.newtest.Controller;

import com.st.newtest.Entity.User;
import com.st.newtest.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        User newUser = userService.login(user);
        if (newUser == null) {
            return "{code: 500, msg: '失败'}";
        }
        session.setAttribute("user", newUser);
        return "{code: 200, msg: '成功'}";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 销毁session
        return "login";
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/regist")
    public String regist() {
        return "请求接收成功";
    }
}
