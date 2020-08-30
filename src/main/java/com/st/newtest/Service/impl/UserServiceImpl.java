package com.st.newtest.Service.impl;

import com.st.newtest.Entity.User;
import com.st.newtest.Mapper.UserMapper;
import com.st.newtest.Service.UserService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service("userService")
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {
    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Override
    public User findUserByName(String name) {
        return userMapper.findUserByName(name);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean insertUser(User user) {
        if (user.getUsername() != null) {
            User newUser = userMapper.selectSingleUser(user.getUsername());
            if (newUser == null) {
                user.setPassword(new Md5Hash(user.getPassword(), user.getUsername(), 3).toString());
                userMapper.insertUser(user);
            } else { // 已经存在该用户，用户名不能相同
                return false;
            }
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean deleteUser(String username) {
        // 根据username查找到该用户
        if (username != null) {
            User user = userMapper.selectSingleUser(username);
            // 通过查找到的用户获取id并通过id删除
            if (user != null) {
                userMapper.deleteUserById(user.getId());
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public Boolean updateUser(User user) {
        // 根据username查找用户
        if (user.getUsername() != null) {
            User userT = userMapper.selectSingleUser(user.getUsername());
            user.setPassword(new Md5Hash(user.getPassword(), user.getUsername(), 3).toString());
            // password
            if (user.getPassword() != null && Pattern.matches("^[\\w_]{6,20}$", user.getPassword())) {
                userT.setPassword(user.getPassword());
            }
            // nickname
            if (user.getNickname() != null && Pattern.matches("^[\u4E00-\u9FA5A-Za-z0-9_]{2,10}$", user.getNickname())) {
                userT.setNickname(user.getNickname());
            }
            // corepwd
            if (user.getCorepwd() != null && Pattern.matches("^[0-9]*[1-9][0-9]*$", user.getCorepwd().toString())) {
                userT.setCorepwd(user.getCorepwd());
            }
            if (userMapper.updateUser(userT) <= 0) {
                return false; // 无行数影响，代表无任何改变
            }

        }
        // 比对不同值并修改
        return true;
    }


}
