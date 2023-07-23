package com.cause.transaction.service;

import com.cause.transaction.mapper.UserMapper;
import com.cause.transaction.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author cause
 * @date 2021/11/24
 */
@Service
public class UserServiceTransactionStateInvoke {

  /**
   * 一个mapper方法调用结束之后关闭当前session
   * 但是再调用mapper方法，创建新的session，connection依然不变
   * 可以理解为一个mapper绑定了一个connection
   */
  @Autowired
  UserMapper userMapper;

  @Autowired
  UserServiceTransactionStateInvoked userServiceTransactionStateInvoked;

  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
  public void requestTransactional() {
    User user = User.builder().id(6).name("user6").build();
    userMapper.addUser(user);

    /**
     * 虽然try但是还是回滚了之前的提交
     * @see nestedTransactional()
     */
    try {
      userServiceTransactionStateInvoked.requestTransactional();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

//  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
  public void supportTransactional() {
    User user = User.builder().id(6).name("user6").build();
    userMapper.addUser(user);

    userServiceTransactionStateInvoked.supportTransactional();
  }

//  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
  public void mandatoryTransactional() {
    addUser2();
    /**
     * 调用者的方法必须存在事务，否则报一下错误信息：
     * No existing transaction found for transaction marked with propagation 'mandatory'
     */
    userServiceTransactionStateInvoked.mandatoryTransactional();
  }

  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
  public void requiresNewTransactional() {
    User user = User.builder().id(6).name("user6").build();
    userMapper.addUser(user);

    userServiceTransactionStateInvoked.requiresNewTransactional();

    User user2 = User.builder().id(6).name("user6").build();
    userMapper.addUser(user2);
  }

  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
  public void notSupportTransactional() {
    userServiceTransactionStateInvoked.notSupportTransactional();

    User user = User.builder().id(6).name("user6").build();
    userMapper.addUser(user);
  }

  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
  public void neverTransactional() {
    User user = User.builder().id(6).name("user6").build();
    userMapper.addUser(user);

    userServiceTransactionStateInvoked.neverTransactional();
  }

  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
  public void nestedTransactional() {
    User user = User.builder().id(6).name("user6").build();
    userMapper.addUser(user);

    /**
     * try之后没有回滚了之前的提交
     * 因为此处设置了savepoint
     * @see nestedTransactional()
     */
    try {
      userServiceTransactionStateInvoked.nestedTransactional();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * rollbackFor: 导致事务回滚的异常
   */
  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
  public void addUser1() {
    User user = User.builder().id(6).name("user6").build();
    userMapper.addUser(user);
  }

  public void addUser2() {
    User user = User.builder().id(6).name("user6").build();
    userMapper.addUser(user);
  }

}

