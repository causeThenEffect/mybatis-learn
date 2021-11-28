package com.cause.transaction.propagation.service;

import com.cause.transaction.propagation.model.User;
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

  @Autowired
  UserService userService;

  @Autowired
  UserServiceTransactionStateInvoked userServiceTransactionStateInvoked;

  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
  public void requestTransactional() {
    userService.addUser(6, "user6");

    userServiceTransactionStateInvoked.requestTransactional();
  }

  //  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
  public void supportTransactional() {
    userService.addUser(6, "user6");

    userServiceTransactionStateInvoked.supportTransactional();
  }

  //  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
  public void mandatoryTransactional() {
    userService.addUser(6, "user6");
    /**
     * 调用者的方法必须存在事务，否则报一下错误信息：
     * No existing transaction found for transaction marked with propagation 'mandatory'
     */
    userServiceTransactionStateInvoked.mandatoryTransactional();
  }

  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
  public void requiresNewTransactional() {
    userService.addUser(6, "user6");

    userServiceTransactionStateInvoked.requiresNewTransactional();

    userService.addUser(6, "user6");
  }

  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
  public void notSupportTransactional() {
    userServiceTransactionStateInvoked.notSupportTransactional();

    userService.addUser(6, "user6");
  }

  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
  public void neverTransactional() {
    userService.addUser(6, "user6");

    userServiceTransactionStateInvoked.neverTransactional();
  }

  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
  public void nestedTransactional() {
    userService.addUser(6, "user6");

    userServiceTransactionStateInvoked.nestedTransactional();

    userService.addUser(7, "user7");
  }

}

