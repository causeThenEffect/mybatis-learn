package com.cause.transaction.propagation.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author cause
 * @date 2021/11/24
 */
@Service
@Slf4j
public class UserServiceTransactionStateInvoked {

  @Autowired
  UserService userService;

  /**
   * 修饰对象：逻辑事务
   *
   * 相当于当前代码合并到上一个方法中去执行
   *
   * 逻辑事务合并
   */
  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
  public void requestTransactional() {
    userService.addUser(6, "user6");
  }

  /**
   * 修饰对象：逻辑事务
   *
   * 如果调用者有事务就使用，没有就不使用
   */
  @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
  public void supportTransactional() {
    userService.addUser(7, "user7");
    userService.addUser(7, "user7");
  }

  /**
   * 修饰对象：逻辑事务
   *
   * 调用者必须存在事务，否则抛出异常
   */
  @Transactional(rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
  public void mandatoryTransactional() {
    userService.addUser(6, "user6");
  }

  /**
   * 修饰对象：物理事务
   *
   * 此处相当于新起了一个物理事务，所以调用者事务的好坏对于它没有任何影响
   *
   * 物理事务：能够被独立回滚和提交的connection
   * 逻辑事务：虽然被@Transactional修饰，但是调用者和被调用者处于同一个物理事务之中
   */
  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
  public void requiresNewTransactional() {
    userService.addUser(7, "user7");
  }

  /**
   * 修饰对象：逻辑事务
   *
   * 以非事务方式执行此段代码，如果调用者存在事务，就把当前事务挂起
   *
   * 其实这也是在一个物理事务中执行的，只是每次执行之后都被事务提交了，这样每次执行就不会相互影响
   */
  @Transactional(rollbackFor = Exception.class, propagation = Propagation.NOT_SUPPORTED)
  public void notSupportTransactional() {
    try {
      userService.addUser(7, "user7");
      userService.addUser(7, "user7");
    } catch (Exception e) {
      log.info(e.getMessage());
    }
  }

  /**
   * 修饰对象：逻辑事务
   *
   * 调用者不能存在事务
   * Existing transaction found for transaction marked with propagation 'never'
   */
  @Transactional(rollbackFor = Exception.class, propagation = Propagation.NEVER)
  public void neverTransactional() {
    userService.addUser(6, "user6");
  }

  /**
   * 修饰对象：逻辑事务
   */
  @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
  public void nestedTransactional() {

    /**
     * 此处是通过设置savepoint来执行接下来的业务逻辑
     * 如果抛出异常，事务回滚到当前savepoint
     * 但是如果没有捕获异常，继续向外抛，会影响commit操作，从而造成之前的逻辑出错
     */
    try {
      userService.addUser(6, "user6");
    } catch (Exception e) {
      log.info(e.getMessage());
    }
  }

}

