package com.st.newtest.Mapper;

import com.st.newtest.Entity.Permissions;
import com.st.newtest.Entity.Role;
import com.st.newtest.Entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    int insertUser(User user);

    int insertRole(Role role);

    int insertPermission(Permissions permission);

    int insertUserAndRoleId(Map<String, Integer> map);

    int insertRoleAndPermissionId(Map<String, Integer> map);

    int deleteUserById(Integer id);

    int deleteRoleById(Integer id);

    int deletePermissionById(Integer id);

    int deleteUserAndRoleIdById(Map<String, Integer> map);

    int deleteRoleAndPermissionIdById(Map<String, Integer> map);

    int updateUserById(User user);

    int updateRoleById(Role role);

    int updatePermissionById(Permissions permission);

    int updateUserAndRoleIdById(Map<String, Integer> map);

    int updateRoleAndPermissionIdById(Map<String, Integer> map);

    List<User> selectAllSingleUser();

    List<Role> selectAllSingleRole();

    List<Permissions> selectAllSinglePermission();

    List<Role> selectAllPermissionIdByRoleId(String rolename);

    User findUserByName(String name);
}