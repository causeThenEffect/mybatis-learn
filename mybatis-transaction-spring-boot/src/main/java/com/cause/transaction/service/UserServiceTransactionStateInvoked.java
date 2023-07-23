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
public class UserServiceTransactionStateInvoked {

  /**
   * 一个mapper方法调用结束之后关闭当前session
   * 但是再调用mapper方法，创建新的session，connection依然不变
   * 可以理解为一个mapper绑定了一个connection
   */
  @Autowired
  UserMapper userMapper;

  /**
   * 修饰对象：逻辑事务
   *
   * 相当于当前代码合并到上一个方法中去执行
   *
   * 事务合并
   */
  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
  public void requestTransactional() {
    User user = User.builder().id(6).name("user6").build();
    userMapper.addUser(user);
  }

  /**
   * 修饰对象：逻辑事务
   *
   * 如果调用者有事务就使用，没有就不使用
   */
  @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
  public void supportTransactional() {
    User user = User.builder().id(7).name("user7").build();
    userMapper.addUser(user);

    User user2 = User.builder().id(7).name("user7").build();
    userMapper.addUser(user2);
  }

  /**
   * 修饰对象：逻辑事务
   *
   * 调用者必须存在事务，否则抛出异常
   * todo 但是奇怪的是方法插入数据成功了
   */
  @Transactional(rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
  public void mandatoryTransactional() {
    User user = User.builder().id(6).name("user6").build();
    userMapper.addUser(user);
  }

  /**
   * 修饰对象：物理事务
   *
   * 此处相当于新起了一个物理事务，所以调用者事务的好坏对于它没有任何影响
   *
   * 我好像知道什么是物理事务，什么是逻辑事务了
   * 物理事务：能够被独立回滚和提交的connection
   * 逻辑事务：虽然被@Transactional修饰，但是调用者和被调用者处于同一个物理事务之中
   */
  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
  public void requiresNewTransactional() {
    User user = User.builder().id(7).name("user7").build();
    userMapper.addUser(user);
  }

  /**
   * 修饰对象：逻辑事务
   *
   * 如果调用者存在事务，当前方法，就以普通方法执行
   *
   * 这样此段代码就不受事务控制，后来回滚对它也没有影响
   */
  @Transactional(rollbackFor = Exception.class, propagation = Propagation.NOT_SUPPORTED)
  public void notSupportTransactional() {
    User user = User.builder().id(6).name("user6").build();
    userMapper.addUser(user);
  }

  /**
   * 修饰对象：逻辑事务
   *
   * 调用者不能存在事务
   */
  @Transactional(rollbackFor = Exception.class, propagation = Propagation.NEVER)
  public void neverTransactional() {
    User user = User.builder().id(6).name("user6").build();
    userMapper.addUser(user);
  }

  /**
   * 修饰对象：逻辑事务
   */
  @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
  public void nestedTransactional() {
    User user = User.builder().id(6).name("user6").build();
    userMapper.addUser(user);
  }

}

