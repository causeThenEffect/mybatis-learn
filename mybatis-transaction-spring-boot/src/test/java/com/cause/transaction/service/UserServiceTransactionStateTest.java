package com.cause.transaction.service;

import com.cause.transaction.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * UserServiceTransactionStateInvoke Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>11月 25, 2021</pre>
 */
@Slf4j
public class UserServiceTransactionStateTest extends BaseTest {

  @Autowired
  UserServiceTransactionStateInvoke userServiceTransactionStateInvoke;

  @Autowired
  UserService userService;

  @Test
  void testRequestTransactional() {
    try {
      userServiceTransactionStateInvoke.requestTransactional();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    Assertions.assertEquals(5, userService.getAllUsers().size());
  }

  @Test
  void testSupportTransactional() {
    try {
      userServiceTransactionStateInvoke.supportTransactional();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    Assertions.assertEquals(7, userService.getAllUsers().size());
  }

  @Test
  void testMandatoryTransactional() {
    try {
      userServiceTransactionStateInvoke.mandatoryTransactional();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    Assertions.assertEquals(6, userService.getAllUsers().size());
  }

  @Test
  void testRequiresNewTransactional() {
    try {
      userServiceTransactionStateInvoke.requiresNewTransactional();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    Assertions.assertEquals("user7", userService.getAllUsers().get(5).getName());
  }

  @Test
  void testNotSupportTransactional() {
    try {
      userServiceTransactionStateInvoke.notSupportTransactional();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    Assertions.assertEquals(6, userService.getAllUsers().size());
  }

  /**
   * 调用者不应该以事务方式运行，否则报以下错误：
   * Existing transaction found for transaction marked with propagation 'never'
   */
  @Test
  void testNeverTransactional() {
    try {
      userServiceTransactionStateInvoke.neverTransactional();
    } catch (Exception e) {
      log.info(e.getMessage());
    }

    Assertions.assertEquals(5, userService.getAllUsers().size());
  }

  @Test
  void testNestedTransactional() {
    try {
      userServiceTransactionStateInvoke.nestedTransactional();
    } catch (Exception e) {
      log.info(e.getMessage());
    }

    Assertions.assertEquals(6, userService.getAllUsers().size());
  }

} 
