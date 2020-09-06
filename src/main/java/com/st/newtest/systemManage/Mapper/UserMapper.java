package com.st.newtest.systemManage.Mapper;

import com.st.newtest.systemManage.Entity.*;
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

    int updateUser(User user);

    int updateRole(Role role);

    int updatePermission(Permissions permission);

    int updateUserAndRoleId(Map<String, Integer> map);

    int updateRoleAndPermissionId(Map<String, Integer> map);

    List<User> selectAllSingleUser();

    List<Role> selectAllSingleRole();

    List<Permissions> selectAllSinglePermission();

    Role findRoleByRolename(String rolename);

    User selectSingleUser(String username);

    Role selectSingleRole(String rolename);

    Permissions selectSinglePermission(String modelname);

    List<UserRole> selectAllRoleIdForUser(Integer uid);

    List<RolePermission> selectAllPermissionIdForRole(Integer rid);

    User findUserByName(String name);

    User findUserByNameWithoutPermission(String name);

    List<Role> selectRolesByModelName(String modelname);

    List<User> selectUsersByRoleName(String rolename);
}