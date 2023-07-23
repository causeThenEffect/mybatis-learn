package com.cause.cache.origin;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.hsqldb.lib.StopWatch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author cause
 * @date 2021/11/21
 */
public class CacheDirtyTest {

  private static SqlSessionFactory sqlSessionFactory;

  @BeforeAll
  static void setUp() throws Exception {
    // create a SqlSessionFactory
    try (Reader reader = Resources.getResourceAsReader("com/cause/cache/origin/mybatis-config.xml")) {
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    }

    // populate in-memory database
    BaseDataTest.runScript(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(), "com/cause/cache/origin/CreateDB.sql");
  }

  /**
   *
   * 一级缓存的作用范围是session
   * 这也体现了事务的持久性，一旦事务提交数据将永久生效
   * 但是在提交之前数据都有可能回滚，数据操作不生效
   *
   */
  @Test
  void test() {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      User user = new User();
      user.setId(6);
      user.setName("test6");
      int i = session.insert("addUser", user);

      assertEquals(i, 1);

      /**
       * 本次是从数据库直接读取的，读取之后会存到一级缓存
       * 这里数据的事务隔离级别是读未提交
       * 所以可以读取到六条数据
       */
      List<Object> allUsers = session.selectList("getAllUsers");
      assertEquals(allUsers.size(), 6);

      /**
       * 这次是直接从一级缓存读取的数据，属于脏数据
       * 因为session还没有提交，数据随时有可能回滚
       */
      List<Object> allUsers2 = session.selectList("getAllUsers");
      assertEquals(allUsers2.size(), 6);

      session.rollback();
    }

    /**
     * 数据回滚之后，依然只读到五条，说明刚才读取的是脏数据
     */
    try (SqlSession session = sqlSessionFactory.openSession()) {
     List<Object> allUsers = session.selectList("getAllUsers");
      assertEquals(allUsers.size(), 5);
    }

  }


}
