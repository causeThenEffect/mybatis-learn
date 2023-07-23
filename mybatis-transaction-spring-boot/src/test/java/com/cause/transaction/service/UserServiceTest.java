package com.cause.transaction.service;

import com.cause.transaction.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

/**
 * UserServiceTransactionStateInvoke Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>11月 24, 2021</pre>
 */
@SpringBootTest
@Slf4j
public class UserServiceTest {

  @Autowired
  UserService userService;

  @Autowired
  SqlSessionFactory sqlSessionFactory;

  @Autowired
  UserMapper userMapper2;

  @BeforeAll
  static void setUp() throws Exception {
  }

  @Test
  public void testGetAllUsers() throws Exception {
    initDataBase();

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    Assertions.assertEquals(5, userService.getAllUsers().size());
    Assertions.assertEquals(1, userService.addUser());
    stopWatch.stop();
    log.info("cost time=====>" + stopWatch.getLastTaskTimeMillis());
  }

  public void initDataBase() throws Exception {
    ScriptRunner runner = new ScriptRunner(sqlSessionFactory.openSession(true).getConnection());
    runner.runScript(Resources.getResourceAsReader("createDB.sql"));
  }

  @Test
  public void addUserTransaction() throws Exception {
    initDataBase();
    userService.addUserTransaction();
  }

  @Test
  public void addUserTransaction2() throws Exception {
    initDataBase();
    userService.addUserTransaction3();
  }

} 
