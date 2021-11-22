import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CacheTest {

  private static SqlSessionFactory sqlSessionFactory;

  @BeforeAll
  static void setUp() throws Exception {
    // create a SqlSessionFactory
    try (Reader reader = Resources.getResourceAsReader("./mybatis-config.xml")) {
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    }

    // populate in-memory database
    BaseDataTest.runScript(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(), "./CreateDB.sql");
  }

  @Test
  void test() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      StopWatch stopWatch = new StopWatch();
      stopWatch.start();
      List<Object> allUsers = sqlSession.selectList("getAllUsers");
      log.info("cost time: {}",stopWatch.elapsedTime());
      Assertions.assertEquals(allUsers.size(), 5);

//      sqlSession.commit();

      /**
       * 使用一级缓存
       *
       * 因为session没有提交，所以二级缓存不能使用
       * 避免二级缓存产生脏读数据
       *
       */
      stopWatch.start();
      List<Object> allUsers2 = sqlSession.selectList("getAllUsers");
      log.info("cost time: {}", stopWatch.elapsedTime());
      Assertions.assertEquals(allUsers2.size(), 5);
    }

    /**
     * 使用二级缓存
     */
    try(SqlSession sqlSession = sqlSessionFactory.openSession()) {
      StopWatch stopWatch = new StopWatch();
      stopWatch.start();
      List<Object> allUsers = sqlSession.selectList("getAllUsers");
      log.info("cost time: {}", stopWatch.elapsedTime());
      Assertions.assertEquals(allUsers.size(), 5);
    }
  }


}
