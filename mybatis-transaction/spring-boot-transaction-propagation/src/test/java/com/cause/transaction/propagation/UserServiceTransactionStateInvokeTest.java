package com.cause.transaction.propagation;

import com.cause.transaction.propagation.service.UserService;
import com.cause.transaction.propagation.service.UserServiceTransactionStateInvoke;
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
public class UserServiceTransactionStateInvokeTest extends BaseTest {

  @Autowired
  UserServiceTransactionStateInvoke userServiceTransactionStateInvoke;

  @Autowired
  UserService userService;

  @Test
  void testRequestTransactional() {
    try {
      userServiceTransactionStateInvoke.requestTransactional();
    } catch (Exception e) {
      log.info(e.getMessage());
    }

    Assertions.assertEquals(5, userService.getAllUsers().size());
  }

  @Test
  void testSupportTransactional() {
    try {
      userServiceTransactionStateInvoke.supportTransactional();
    } catch (Exception e) {
      log.info(e.getMessage());
    }

    Assertions.assertEquals(7, userService.getAllUsers().size());
  }

  @Test
  void testMandatoryTransactional() {
    try {
      userServiceTransactionStateInvoke.mandatoryTransactional();
    } catch (Exception e) {
      log.info(e.getMessage());
    }

    Assertions.assertEquals(6, userService.getAllUsers().size());
  }

  @Test
  void testRequiresNewTransactional() {
    try {
      userServiceTransactionStateInvoke.requiresNewTransactional();
    } catch (Exception e) {
      log.info(e.getMessage());
    }

    Assertions.assertEquals("user7", userService.getAllUsers().get(5).getName());
  }

  @Test
  void testNotSupportTransactional() {
    try {
      userServiceTransactionStateInvoke.notSupportTransactional();
    } catch (Exception e) {
      log.info(e.getMessage());
    }

    Assertions.assertEquals(7, userService.getAllUsers().size());
  }

  @Test
  void testNeverTransactional() {
    try {
      userServiceTransactionStateInvoke.neverTransactional();
    } catch (Exception e) {
      log.info(e.getMessage());
    }

    Assertions.assertEquals(6, userService.getAllUsers().size());
  }

  @Test
  void testNestedTransactional() {
    try {
      userServiceTransactionStateInvoke.nestedTransactional();
    } catch (Exception e) {
      log.info(e.getMessage());
    }

    Assertions.assertEquals(7, userService.getAllUsers().size());
  }

} 
