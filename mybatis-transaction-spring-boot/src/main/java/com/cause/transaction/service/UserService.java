package com.cause.transaction.service;

import com.cause.transaction.mapper.UserMapper;
import com.cause.transaction.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * @author cause
 * @date 2021/11/24
 */
@Service
public class UserService {

  /**
   * 一个mapper方法调用结束之后关闭当前session
   * 但是再调用mapper方法，创建新的session，connection依然不变
   * 可以理解为一个mapper绑定了一个connection
   */
  @Autowired
  UserMapper userMapper;

  /**
   * 事实证明在spring容器实例化了一个mapper对象，所以两个mapper引用了同一个对象
   */
  @Autowired
  UserMapper userMapper2;

  /**
   * 通过事务的方式可以使用到一级缓存
   *
   * 如果开启事务
   * SpringManagedTransaction.getConnection()
   * 通过调试可以看到这个方法每次获取到的connection都是同一个
   * 如果被关闭事务，该方法每次获取的connection都是不同的
   *
   * @return
   */
//  @Transactional
  public List<User> getAllUsers() {
    return userMapper.getAllUsers();
  }

  public int addUser() {
    User user = User.builder().id(66).name("user66").build();
    return userMapper2.addUser(user);
  }

  @Transactional(rollbackFor = Exception.class)
  public void addUserTransaction() {
    User user = User.builder().id(6).name("user6").build();
    userMapper2.addUser(user);

    User user2 = User.builder().id(2).name("user2").build();
    userMapper2.addUser(user2);
  }

  /**
   * rollbackFor: 导致事务回滚的异常
   */
//  @Transactional(rollbackFor = Exception.class)
  public void addUserTransaction1() {
    User user = User.builder().id(6).name("user6").build();
    userMapper2.addUser(user);
  }

  /**
   * 事务的传播方式指的是：当前事务对于被包裹的事务有什么影响
   */
  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
  public void addUserTransaction3() {
    User user = User.builder().id(6).name("user6").build();
    userMapper2.addUser(user);

    addUserTransaction1();
  }

}
