package com.st.newtest.Controller;

import com.st.newtest.Entity.User;
import com.st.newtest.Service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@RequestMapping("/user")
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public String login(User user) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(
                user.getUsername(),
                user.getPassword()
        );
        try{
            subject.login(usernamePasswordToken);
        } catch (AuthenticationException e) {
            // e.printStackTrace();
            return "账号密码错误";
        } catch (AuthorizationException e) {
            // e.printStackTrace();
            return "没有权限";
        }
        return "login success";
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

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/testuser")
    public String testUser(String username) {
        User user = userService.findUserByName(username);
        System.out.println(user);
        return "11";
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/testlogin")
    public String testLogin(User user) {
        return "";
    }
}
