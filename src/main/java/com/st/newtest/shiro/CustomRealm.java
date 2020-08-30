package com.st.newtest.shiro;

import com.st.newtest.Entity.Permissions;
import com.st.newtest.Entity.Role;
import com.st.newtest.Entity.User;
import com.st.newtest.Service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;


public class CustomRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("==============授权================");
        //获取登录用户
        User user = (User) principalCollection.getPrimaryPrincipal();
        //添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        for (Role role : user.getRoleList()) {
            //System.out.println("rolename = " + role.getRolename());
            //添加角色
            simpleAuthorizationInfo.addRole(role.getRolename());
            //添加权限
            for (Permissions permissions : role.getPermissionsList()) {
                //System.out.println("permissions = " + permissions.getModelname());
                simpleAuthorizationInfo.addStringPermission(permissions.getModelname());
            }
        }
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("==============认证================");
        //加这一步的目的是在Post请求的时候会先进认证，然后在到请求
        if (authenticationToken.getPrincipal() == null) {
            return null;
        }
        //获取用户信息
        String name = authenticationToken.getPrincipal().toString();
        String password = new String((char[])authenticationToken.getCredentials());
        System.out.println("password = " + password);
        User user = userService.findUserByName(name);
        if (user == null && user.getPassword().equals(password)) {
            //这里返回后会报出对应异常
            System.out.println("密码错误");
            return null;
        } else {
            //这里验证authenticationToken和simpleAuthenticationInfo的信息
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(), getName());
            return simpleAuthenticationInfo;
        }
    }
}
