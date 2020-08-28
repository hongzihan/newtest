package com.st.newtest.Service.impl;

import com.st.newtest.Entity.Permissions;
import com.st.newtest.Entity.Role;
import com.st.newtest.Entity.User;
import com.st.newtest.Mapper.UserMapper;
import com.st.newtest.Service.UserService;
import org.junit.Test;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired(required = false)
    private UserMapper userMapper;

    @Override
    public int insertUser(User user) {
        return 0;
    }

    @Override
    public int insertRole(Role role) {
        return 0;
    }

    @Override
    public int insertPermission(Permissions permission) {
        return userMapper.insertPermission(permission);
    }

    @Override
    public int insertUserAndRoleId(Map<String, Integer> map) {
        return 0;
    }

    @Override
    public int insertRoleAndPermissionId(Map<String, Integer> map) {
        return 0;
    }

    @Override
    public int deleteUserById(Integer id) {
        return 0;
    }

    @Override
    public int deleteRoleById(Integer id) {
        return 0;
    }

    @Override
    public int deletePermissionById(Integer id) {
        return 0;
    }

    @Override
    public int deleteUserAndRoleIdById(Map<String, Integer> map) {
        return 0;
    }

    @Override
    public int deleteRoleAndPermissionIdById(Map<String, Integer> map) {
        return 0;
    }

    @Override
    public int updateUserById(User user) {
        return 0;
    }

    @Override
    public int updateRoleById(Role role) {
        return 0;
    }

    @Override
    public int updatePermissionById(Permissions permission) {
        return 0;
    }

    @Override
    public int updateUserAndRoleIdById(Map<String, Integer> map) {
        return 0;
    }

    @Override
    public int updateRoleAndPermissionIdById(Map<String, Integer> map) {
        return 0;
    }

    @Override
    public List<User> selectAllSingleUser() {
        return null;
    }

    @Override
    public List<Role> selectAllSingleRole() {
        return null;
    }

    @Override
    public List<Permissions> selectAllSinglePermission() {
        List<Permissions> pers = userMapper.selectAllSinglePermission();
        for (Permissions permissions : pers) {
            System.out.println(permissions.getModelname());
        }

        return null;
    }

    @Override
    public List<Role> selectAllPermissionIdByRoleId(String rolename) {
        System.out.println("hell" + rolename);
        List<Role> roleList = userMapper.selectAllPermissionIdByRoleId(rolename);
        for (Role role : roleList) {
            System.out.println(role.getRolename());
        }
        return null;
    }

    @Override
    public User findUserByName(String name) {
        return userMapper.findUserByName(name);
    }
}
