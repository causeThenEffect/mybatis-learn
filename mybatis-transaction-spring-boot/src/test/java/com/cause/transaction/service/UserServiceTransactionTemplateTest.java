//package com.cause.transaction.service;
//
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
///**
// * UserServiceTransactionTemplate Tester.
// *
// * @author <Authors name>
// * @version 1.0
// * @since <pre>11月 25, 2021</pre>
// */
//@SpringBootTest
//public class UserServiceTransactionTemplateTest {
//
//  @Autowired
//  SqlSessionFactory sqlSessionFactory;
//
//  @Autowired
//  UserServiceTransactionTemplate userServiceTransactionTemplate;
//
//  @Test
//  public void testAddUserTransactionTemplate() throws Exception {
//    TestUtil.initDataBase(sqlSessionFactory);
//
//    Assertions.assertEquals("success", userServiceTransactionTemplate.addUserTransactionTemplate());
//  }
//
//
//}
