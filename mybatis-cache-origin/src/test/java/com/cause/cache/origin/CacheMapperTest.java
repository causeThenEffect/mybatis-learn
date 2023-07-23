package com.cause.cache.origin;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.hsqldb.lib.StopWatch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.util.List;

/**
 * @author cause
 * @date 2021/11/21
 */
public class CacheMapperTest {

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

  @Test
  void test() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      StopWatch stopWatch = new StopWatch();
      stopWatch.start();
      /**
       * 一个mapper接口对应这一个session，一个connection
       */
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      List<User> allUsers = mapper.getAllUsers();
      System.out.println(stopWatch.elapsedTime());

      /**
       * 只用connection commit 之后，二级缓存才生效
       * 不然会造成脏读
       * 比如：insert之后数据又回滚了
       * 这样缓存里保存的就是脏数据了
       */
      sqlSession.commit();

      /**
       * 此处使用了二级缓存，因为commit之后二级缓存就生效了，否则使用一级缓存
       */
      stopWatch.start();
      Mapper mapper2 = sqlSession.getMapper(Mapper.class);
      List<User> allUsers2 = mapper2.getAllUsers();
      System.out.println(stopWatch.elapsedTime());

      Assertions.assertEquals(allUsers.size(), 5);
      Assertions.assertEquals(allUsers2.size(), 5);
    }

    try(SqlSession sqlSession = sqlSessionFactory.openSession()) {
      StopWatch stopWatch = new StopWatch();
      stopWatch.start();
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      List<User> allUsers = mapper.getAllUsers();
      System.out.println(stopWatch.elapsedTime());
      Assertions.assertEquals(allUsers.size(), 5);
    }
  }


}
