package com.st.newtest.Service;

import com.st.newtest.Entity.User;

public interface UserService {

    User findUserByName(String name); // 根据名字查找用户

    Boolean insertUser(User user); // 往数据库中插入一个用户

    Boolean deleteUser(String username); // 删除一个用户

    Boolean updateUser(User user); // 修改一个用户的信息
}
