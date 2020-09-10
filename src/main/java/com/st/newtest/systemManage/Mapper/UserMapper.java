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

    int insertUserAndRoleId(Map<String, Object> map);

    int insertRoleAndPermissionId(Map<String, Object> map);

    int deleteUserById(Long id);

    int deleteRoleById(Long id);

    int deletePermissionById(Long id);

    int deleteUserAndRoleIdById(Map<String, Object> map);

    int deleteRoleAndPermissionIdById(Map<String, Object> map);

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

    List<UserRole> selectAllRoleIdForUser(Long uid);

    List<RolePermission> selectAllPermissionIdForRole(Long rid);

    User findUserByName(String name);

    User findUserByNameWithoutPermission(String name);

    List<Role> selectRolesByModelName(String modelname);

    List<User> selectUsersByRoleName(String rolename);
}