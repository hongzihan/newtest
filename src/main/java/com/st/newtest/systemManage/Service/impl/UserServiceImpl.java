package com.st.newtest.systemManage.Service.impl;

import com.st.newtest.systemManage.Mapper.UserMapper;
import com.st.newtest.systemManage.Service.UserService;
import com.st.newtest.systemManage.Entity.*;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
                // 先找关联角色 并删除
                User userByName = userMapper.findUserByNameWithoutPermission(username);
                if (userByName != null) {
                    List<Role> roleList = userByName.getRoleList();
                    HashMap<String, Integer> map = null;
                    for (Role role : roleList) {
                        map = new HashMap<>();
                        map.put("uid", user.getId());
                        map.put("rid", role.getId());
                        userMapper.deleteUserAndRoleIdById(map);
                    }
                }
                // 删除用户
                userMapper.deleteUserById(user.getId());
            } else {
                return false;
            }
        }
        return true;
    }

    @Transactional
    @Override
    public Boolean updateUser(User user) {
        // 根据username查找用户
        if (user.getUsername() != null) {
            User userT = userMapper.selectSingleUser(user.getUsername());
            // password
            if (user.getPassword() != null && Pattern.matches("^[\\w_]{6,20}$", user.getPassword())) {
                user.setPassword(new Md5Hash(user.getPassword(), user.getUsername(), 3).toString());
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
        return true;
    }

    @Transactional
    @Override
    public Boolean insertRole(Role role) {
        if (role.getRolename() != null) {
            Role newRole = userMapper.selectSingleRole(role.getRolename());
            if (newRole == null) {
                userMapper.insertRole(role);
            } else { // 已经存在该角色，角色名不能相同
                return false;
            }
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean deleteRole(String rolename) {
        // 根据rolename查找到该角色
        if (rolename != null) {
            Role role = userMapper.selectSingleRole(rolename);
            if (role != null) {
                // 因为关联表的问题，所以在删除角色的同时，需要删除角色所对应的所有权限和用户
                HashMap<String, Integer> map = null;
                // 第一步先查询该角色是否还有关联用户
                List<User> users = userMapper.selectUsersByRoleName(rolename);
                if (users != null) {
                    for (User user : users) {
                        map = new HashMap<>();
                        map.put("uid", user.getId());
                        map.put("rid", role.getId());
                        userMapper.deleteUserAndRoleIdById(map);
                    }
                }
                // 第二步查询该角色是否有关联权限
                Role roleWithP = userMapper.findRoleByRolename(rolename);
                if (roleWithP != null) { // 有关联权限才能查到该角色
                    List<Permissions> permissionsList = roleWithP.getPermissionsList();
                    for (Permissions permissions : permissionsList) {
                        map = new HashMap<>();
                        map.put("rid", role.getId());
                        map.put("pid", permissions.getId());
                        userMapper.deleteRoleAndPermissionIdById(map);
                    }
                }
                // 删除用户
                userMapper.deleteRoleById(role.getId());
            } else {
                return false;
            }
        }
        return true;
    }

    @Transactional
    @Override
    public Boolean updateRole(Role role) {
        // 根据username查找角色
        if (role.getRolename() != null) {
            Role roleT = userMapper.selectSingleRole(role.getRolename());
            // roledesc
            if (role.getRoledesc() != null && Pattern.matches("^[\u4E00-\u9FA5A-Za-z0-9_]{1,30}$", role.getRoledesc())) {
                roleT.setRoledesc(role.getRoledesc());
            }
            if (userMapper.updateRole(roleT) <= 0) {
                return false; // 无行数影响，代表无任何改变
            }
        }
        return true;
    }

    @Transactional
    @Override
    public Boolean insertPermission(Permissions permissions) {
        if (permissions.getModelname() != null) {
            Permissions newPermission = userMapper.selectSinglePermission(permissions.getModelname());
            if (newPermission == null) {
                userMapper.insertPermission(permissions);
            } else { // 已经存在该权限，权限名不能相同
                return false;
            }
        }
        return true;
    }

    @Transactional
    @Override
    public Boolean deletePermission(String modelname) {
        // 根据modelname查找到该权限
        if (modelname != null) {
            Permissions permission = userMapper.selectSinglePermission(modelname);
            // 通过查找到的权限获取id并通过id删除
            if (permission != null) {
                // 先找关联角色 并删除关联
                List<Role> roleList = userMapper.selectRolesByModelName(modelname);
                if (roleList != null) {
                    HashMap<String, Integer> map = null;
                    for (Role role : roleList) {
                        map = new HashMap<>();
                        map.put("rid", role.getId());
                        map.put("pid", permission.getId());
                        userMapper.deleteRoleAndPermissionIdById(map);
                    }
                }
                // 删除权限
                userMapper.deletePermissionById(permission.getId());
            } else {
                return false;
            }
        }
        return true;
    }

    @Transactional
    @Override
    public Boolean updatePermission(Permissions permissions) {
        // 根据modelname查找权限
        if (permissions.getPermission() != null) {
            Permissions permissionT = userMapper.selectSinglePermission(permissions.getModelname());
            // roledesc
            if (permissions.getPermission() != null && Pattern.matches("^[\u4E00-\u9FA5A-Za-z0-9_]{1,30}$", permissions.getPermission())) {
                permissionT.setPermission(permissions.getPermission());
            }
            if (userMapper.updatePermission(permissionT) <= 0) {
                return false; // 无行数影响，代表无任何改变
            }
        }
        return true;
    }

    @Override
    public List<Role> selectAllSingleRole() {
        return userMapper.selectAllSingleRole();
    }

    @Override
    public List<Permissions> selectAllSinglePermission() {
        return userMapper.selectAllSinglePermission();
    }

    /**
     * 根据给定的用户名和角色组，为用户分配角色
     * @param username 用户名
     * @param roles 角色组
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean giveRoleToUser(String username, List<String> roles) {
        User user = userMapper.selectSingleUser(username);
        if (user == null) {
            return false;
        }
        for (String role : roles) {
            Role roleNew = userMapper.selectSingleRole(role);
            if (roleNew != null) {
                List<UserRole> userRoles = userMapper.selectAllRoleIdForUser(user.getId());
                Boolean isRepeat = false;
                if (userRoles != null) {// 角色当前已经拥有至少一个权限
                    for (UserRole ur : userRoles) {
                        if (ur.getRid().equals(roleNew.getId())) {
                            isRepeat = !isRepeat; // 该权限与已有权限重复
                            break;
                        }
                    }
                }
                if (!isRepeat) { // 若权限无重复
                    HashMap<String, Integer> map = null;
                    map = new HashMap<>();
                    map.put("uid", user.getId());
                    map.put("rid", roleNew.getId());
                    userMapper.insertUserAndRoleId(map);
                }
            }
        }
        return true;
    }

    @Override
    public Boolean deleteRoleToUser(String username, List<String> roles) {
        User user = userMapper.selectSingleUser(username);
        if (user == null) {
            return false;
        }
        for (String role : roles) {
            Role roleNew = userMapper.selectSingleRole(role);
            if (roleNew != null) {
                List<UserRole> userRoles = userMapper.selectAllRoleIdForUser(user.getId());
                if (userRoles != null) {
                    for (UserRole userRole : userRoles) {
                        if (userRole.getRid().equals(roleNew.getId())) { // 该角色确实存在
                            HashMap<String, Integer> map = new HashMap<>();
                            map.put("uid", user.getId());
                            map.put("rid", roleNew.getId());
                            userMapper.deleteUserAndRoleIdById(map);
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * 根据给定的角色和权限组，为角色分配权限
     * @param rolename 角色名
     * @param permissions 权限组
     * @return
     */
    @Transactional
    @Override
    public Boolean givePermissionToRole(String rolename, List<String> permissions) {
        Role role = userMapper.selectSingleRole(rolename);
        if (role == null) {
            return false;
        }
        for (String permission : permissions) {
            Permissions permissionNew = userMapper.selectSinglePermission(permission);
            if (permissionNew != null) {
                List<RolePermission> rolePermissions = userMapper.selectAllPermissionIdForRole(role.getId());
                Boolean isRepeat = false;
                if (rolePermissions != null) {// 角色当前已经拥有至少一个权限
                    for (RolePermission rp : rolePermissions) {
                        if (rp.getPid().equals(permissionNew.getId())) {
                            isRepeat = !isRepeat; // 该权限与已有权限重复
                            break;
                        }
                    }
                }
                if (!isRepeat) { // 若权限无重复
                    HashMap<String, Integer> map = null;
                    map = new HashMap<>();
                    map.put("rid", role.getId());
                    map.put("pid", permissionNew.getId());
                    userMapper.insertRoleAndPermissionId(map);
                }
            }
        }
        return true;
    }

    @Override
    public Boolean deletePermissionToRole(String rolename, List<String> permissions) {
        Role role = userMapper.selectSingleRole(rolename);
        if (role == null) {
            return false;
        }
        for (String permission : permissions) {
            Permissions permissionNew = userMapper.selectSinglePermission(permission);
            if (permissionNew != null) {
                List<RolePermission> rolePermissions = userMapper.selectAllPermissionIdForRole(role.getId());
                if (rolePermissions != null) {
                    for (RolePermission rp : rolePermissions) {
                        if (rp.getPid().equals(permissionNew.getId())) { // 该权限确实存在
                            HashMap<String, Integer> map = new HashMap<>();
                            map.put("rid", role.getId());
                            map.put("pid", permissionNew.getId());
                            userMapper.deleteRoleAndPermissionIdById(map);
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public List<String> findAllRoleForUser(String username) {
        User user = userMapper.findUserByNameWithoutPermission(username);
        if (user == null || user.getRoleList() == null) {
            return null;
        }
        List<Role> roleList = user.getRoleList();
        List<String> roles = null;
        for (Role role : roleList) {
            if (roles != null) {
                roles.add(role.getRolename());
            } else {
                roles = new ArrayList<>();
                roles.add(role.getRolename());
            }
        }
        return roles;
    }

    @Override
    public List<String> findAllPermissionsForRole(String rolename) {
        Role role = userMapper.findRoleByRolename(rolename);
        if (role == null || role.getPermissionsList() == null) {
            return null;
        }
        List<Permissions> permissionsList = role.getPermissionsList();
        List<String> permissions = null;
        for (Permissions permission : permissionsList) {
            if (permissions != null) {
                permissions.add(permission.getPermission());
            } else {
                permissions = new ArrayList<>();
                permissions.add(permission.getPermission());
            }
        }
        return permissions;
    }


}
