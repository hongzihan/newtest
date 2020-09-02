package com.st.newtest.Service.impl;

import com.st.newtest.Entity.Permissions;
import com.st.newtest.Entity.Role;
import com.st.newtest.Entity.User;
import com.st.newtest.Mapper.UserMapper;
import com.st.newtest.Service.UserService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public Boolean deleteRole(String rolename) {
        // 根据rolename查找到该角色
        if (rolename != null) {
            Role role = userMapper.selectSingleRole(rolename);
            // 通过查找到的角色获取id并通过id删除
            if (role != null) {
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean giveRoleToUser(String username, List<String> roles) {
        User user = userMapper.selectSingleUser(username);
        if (user == null) {
            return false;
        }
        HashMap<String, Integer> map = null;
        for (String role : roles) {
            Role roleNew = userMapper.selectSingleRole(role);
            if (roleNew != null) {
                map = new HashMap<>();
                map.put("uid", user.getId());
                map.put("rid", roleNew.getId());
                userMapper.insertUserAndRoleId(map);
            }
        }
        return true;
    }


}
