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
public class CacheTest {

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
   * 看来注释真的会影响代码简洁性
   */
  @Test
  void test() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      StopWatch stopWatch = new StopWatch();
      stopWatch.start();
      List<Object> allUsers = sqlSession.selectList("getAllUsers");
      System.out.println(stopWatch.elapsedTime());

      /**
       * 此处使用了一级缓存
       * 没用到二级缓存，因为二级缓存需要session关闭的时候才存储
       *
       * 通过提出问题，然后去寻找答案
       * 有些问题是一闪而过的，捕捉这些疑问，然后解答，逐步找到问题的答案
       * 框架的来龙去脉越来越清晰
       *
       */
      sqlSession.commit();

      stopWatch.start();
      List<Object> allUsers2 = sqlSession.selectList("getAllUsers");
      System.out.println(stopWatch.elapsedTime());

      Assertions.assertEquals(allUsers.size(), 5);
      Assertions.assertEquals(allUsers2.size(), 5);
    }

    /**
     * 此处使用的是二级缓存
     * 上一次session关闭之后，数据存储到了二级缓存，所以就可以取到了
     *
     * 一个session对应了一个connection
     *
     * 调用openSession()方法的时候可以从连接池取出一个connection
     *
     *  todo 和spring结合的时候就是就只是创建了一个sessionFactory吗？
     */
    try(SqlSession sqlSession = sqlSessionFactory.openSession()) {
      StopWatch stopWatch = new StopWatch();
      stopWatch.start();
      List<Object> allUsers = sqlSession.selectList("getAllUsers");
      System.out.println(stopWatch.elapsedTime());
      Assertions.assertEquals(allUsers.size(), 5);
    }
  }


}
