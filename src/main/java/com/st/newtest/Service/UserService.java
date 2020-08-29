package com.st.newtest.Service;

import com.st.newtest.Entity.Permissions;
import com.st.newtest.Entity.Role;
import com.st.newtest.Entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    User findUserByName(String name); // 根据名字查找用户

    Boolean insertUser(User user); // 往数据库中插入一个用户
}
