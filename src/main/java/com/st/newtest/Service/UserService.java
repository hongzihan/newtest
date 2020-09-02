package com.st.newtest.Service;

import com.st.newtest.Entity.Permissions;
import com.st.newtest.Entity.Role;
import com.st.newtest.Entity.User;

import java.util.List;

public interface UserService {

    User findUserByName(String name); // 根据名字查找用户

    Boolean insertUser(User user); // 往数据库中插入一个用户

    Boolean deleteUser(String username); // 删除一个用户

    Boolean updateUser(User user); // 修改一个用户的信息

    Boolean insertRole(Role role); // 往数据库中插入一个角色

    Boolean deleteRole(String rolename); // 删除一个角色

    Boolean updateRole(Role role); // 修改一个角色的信息

    Boolean insertPermission(Permissions permissions); // 往数据库中插入一个角色

    Boolean deletePermission(String modelname); // 删除一个角色

    Boolean updatePermission(Permissions permissions); // 修改一个角色的信息

    List<Role> selectAllSingleRole();

    List<Permissions> selectAllSinglePermission();

    Boolean giveRoleToUser(String username, List<String> roles);
}
