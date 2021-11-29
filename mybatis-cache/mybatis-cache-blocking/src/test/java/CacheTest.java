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
  void test() throws InterruptedException {
    for (int i = 0; i < 5; i++) {
      new Thread(() -> {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
          Assertions.assertEquals(5, sqlSession.selectList("getAllUsers").size());
        }
      }).start();
    }

    Thread.sleep(5000);
  }


}
