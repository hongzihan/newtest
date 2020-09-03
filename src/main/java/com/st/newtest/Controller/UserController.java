package com.st.newtest.Controller;

import com.st.newtest.Entity.Permissions;
import com.st.newtest.Entity.Role;
import com.st.newtest.Entity.User;
import com.st.newtest.Service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.regex.Pattern;

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

    @RequiresRoles("supermanager")
    @ResponseBody
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
            if (!userService.insertUser(user)) {
                return "failed 用户名已经存在";
            }
        } else if (type.equals(2)) { // 删除一个用户
           if (!userService.deleteUser(user.getUsername())) {
               return "failed 不存在该用户";
           }
        } else if (type.equals(3)) { // 修改一个用户
            if (!userService.updateUser(user)) {
                return "failed 更新失败（需更改内容相同或更新失败）";
            }
        }
        return "success";
    }

    @RequiresRoles("supermanager")
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/roleAction")
    public String roleAction(Role role, Integer type) {
        // 所有操作都需要检测username
        if (!Pattern.matches("^[\u4E00-\u9FA5A-Za-z0-9_]{1,30}$", role.getRolename())) {
            return "failed 角色名非法";
        }
        if (type.equals(1) && !role.getRolename().equals("")) { // 增加一个角色
            if (!Pattern.matches("^[\u4E00-\u9FA5A-Za-z0-9_]{1,30}$", role.getRoledesc())) {
                return "failed 角色描述非法";
            }
            if (!userService.insertRole(role)) {
                return "failed 角色名已经存在";
            }

        } else if (type.equals(2)) { // 删除一个角色
            if (!userService.deleteRole(role.getRolename())) {
                return "failed 不存在该角色";
            }
        } else if (type.equals(3)) { // 修改一个角色
            if (!Pattern.matches("^[\u4E00-\u9FA5A-Za-z0-9_]{1,30}$", role.getRoledesc())) {
                return "failed 角色描述非法";
            }
            if (!userService.updateRole(role)) {
                return "failed 更新失败（需更改内容相同或更新失败）";
            }
        }
        return "success";
    }

    @RequiresRoles("supermanager")
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/permissionAction")
    public String permissionAction(Permissions permission, Integer type) {
        // 所有操作都需要检测username
        if (!Pattern.matches("^[\u4E00-\u9FA5A-Za-z0-9_]{1,30}$", permission.getModelname())) {
            return "failed 权限接口非法";
        }
        if (type.equals(1) && !permission.getModelname().equals("")) { // 增加一个角色
            if (!Pattern.matches("^[\u4E00-\u9FA5A-Za-z0-9_]{1,30}$", permission.getPermission())) {
                return "failed 权限名非法";
            }
            if (!userService.insertPermission(permission)) {
                return "failed 权限名已经存在";
            }

        } else if (type.equals(2)) { // 删除一个角色
            if (!userService.deletePermission(permission.getModelname())) {
                return "failed 不存在该权限";
            }
        } else if (type.equals(3)) { // 修改一个角色
            if (!Pattern.matches("^[\u4E00-\u9FA5A-Za-z0-9_]{1,30}$", permission.getPermission())) {
                return "failed 权限名非法";
            }
            if (!userService.updatePermission(permission)) {
                return "failed 更新失败（需更改内容相同或更新失败）";
            }
        }
        return "success";
    }

    /**
     * 为用户分配角色接口
     * @param user
     * @param preRole
     * @return
     */
    @ResponseBody
    @RequiresRoles("supermanager")
    @RequestMapping("/userRoleAction")
    public String userRoleAction(User user, @RequestParam("preRole[]") List<String> preRole, Integer type) {
        if (type == 1) {
            if (!userService.giveRoleToUser(user.getUsername(), preRole)) {
                return "failed 找不到该用户！";
            }
        } else if (type == 2) {
            if (!userService.deleteRoleToUser(user.getUsername(), preRole)) {
                return "failed 找不到该用户！";
            }
        } else {
            return "failed 请至少选择一个操作类型";
        }

        return "success";
    }

    /**
     * 为角色分配权限接口
     * @param role
     * @param prePermission
     * @return
     */
    @ResponseBody
    @RequiresRoles("supermanager")
    @RequestMapping("/rolePermissionAction")
    public String rolePermissionAction(Role role, @RequestParam("prePermission[]") List<String> prePermission) {
        if (!userService.givePermissionToRole(role.getRolename(), prePermission)) {
            return "failed 找不到该角色！";
        }
        return "success";
    }
}
