package com.cause.transaction.propagation;

import com.cause.transaction.propagation.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author cause
 * @date 2021/11/28
 */
@Slf4j
class UserServiceTest extends BaseTest {

  @Autowired
  UserService userService;

  @Test
  void addUserTest() {
    try {
      userService.addUser();
    } catch (Exception e) {
      log.info(e.getMessage());
    }

    Assertions.assertEquals(7, userService.getAllUsers().size());
  }

  @Test
  void addUser3Test() {
    try {
      userService.addUser3();
    } catch (Exception e) {
      log.info(e.getMessage());
    }
    Assertions.assertEquals(6, userService.getAllUsers().size());
  }

}
