//package com.cause.transaction.service;
//
//import com.cause.transaction.mapper.UserMapper;
//import com.cause.transaction.model.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.TransactionDefinition;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.transaction.support.TransactionTemplate;
//
//import java.util.List;
//
///**
// * @author cause
// * @date 2021/11/24
// */
//@Service
//public class UserServiceTransactionTemplate {
//
//  @Autowired
//  TransactionTemplate transactionTemplate;
//
//  @Autowired
//  UserMapper userMapper;
//
//  public String addUserTransactionTemplate() {
//    // 设置事务传播机制
//    transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//    return transactionTemplate.execute(status -> {
//      // 设置事务回滚点
//      Object savepoint = status.createSavepoint();
//      User user = User.builder().id(6).name("user6").build();
//      userMapper.addUser(user);
//      status.rollbackToSavepoint(savepoint);
//
//      addUser();
//      return "success";
//    });
//  }
//
//  public void addUser() {
//    User user2 = User.builder().id(7).name("user2").build();
//    userMapper.addUser(user2);
//  }
//
//}
