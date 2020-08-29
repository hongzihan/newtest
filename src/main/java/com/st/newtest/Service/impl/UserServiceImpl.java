package com.st.newtest.Service.impl;

import com.st.newtest.Entity.User;
import com.st.newtest.Mapper.UserMapper;
import com.st.newtest.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionDefinition;

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

//        TransactionStatus txStatus =
//                transactionManager.getTransaction(new DefaultTransactionDefinition());
//        try {
            user.setId(0);

            userMapper.insertUser(user);
            throw new RuntimeException("呵呵呵呵呵呵呃呃呃呃呃呃呃呃");
//        } catch (Exception e) {
//            transactionManager.rollback(txStatus);
//        }
//            transactionManager.commit(txStatus);
//
//
//
//        return true;
    }


}
