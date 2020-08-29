package com.st.newtest.Controller;

import com.st.newtest.Entity.Permissions;
import com.st.newtest.Entity.User;
import com.st.newtest.Mapper.UserMapper;
import com.st.newtest.Service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.regex.Pattern;

@RequestMapping("/user")
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

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

    @RequiresRoles("supermanager")
    @ResponseBody
    //@Transactional(rollbackFor = Exception.class)
    @RequestMapping(method = RequestMethod.POST, value = "/userAction")
    public String userAction(User user, Integer type) {

        // 所有操作都需要检测username
        if (!Pattern.matches("^[^0-9][\\w_]{3,9}$", user.getUsername())) {
            return "failed 用户名非法";
        }
        if (type.equals(1) && !user.getUsername().equals("")) { // 增加一个用户
            if (!Pattern.matches("^[\\w_]{6,20}$", user.getPassword())) {
                return "failed 密码非法";
            }
            if (!Pattern.matches("^[\u4E00-\u9FA5A-Za-z0-9_]{2,10}$", user.getNickname())) {
                return "failed 昵称非法";
            }
            if (!Pattern.matches("^[0-9]*[1-9][0-9]*$", user.getCorepwd().toString())) {
                return "failed 核心密码非法";
            }

                userService.insertUser(user);
        } else if (type.equals(2)) { // 删除一个用户
            // 根据username查找到该用户
            // 通过查找到的用户获取id并通过id删除
        } else if (type.equals(3)) { // 修改一个用户
            // 根据username查找用户
            // 比对不同值并修改
        }
        return "success";
    }
}
