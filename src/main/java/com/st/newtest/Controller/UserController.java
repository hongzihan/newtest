package com.st.newtest.Controller;

import com.st.newtest.Entity.User;
import com.st.newtest.Service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@RequestMapping("/user")
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public String login(User user) {
        try{
            /**
             * 密码加密 md5
             *   Shiro提供的MD5加密方法 Md5Hash()
             *   1. 加密的内容
             *   2. 加密的盐值 混淆字符串
             *   3.
             */
            String password = new Md5Hash(user.getPassword(), user.getUsername(), 3).toString();
            // 构造登录令牌
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(
                    user.getUsername(),
                    password
            );
            // 获取subject
            Subject subject = SecurityUtils.getSubject();
            subject.login(usernamePasswordToken);
        } catch (AuthenticationException e) {
            // e.printStackTrace();
            return "账号密码错误";
        } catch (AuthorizationException e) {
            // e.printStackTrace();
            return "没有权限";
        } catch (Exception e) {
            return "账号密码错误";
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
