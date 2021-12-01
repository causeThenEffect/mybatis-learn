import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
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
    try (Reader reader = Resources.getResourceAsReader("./mybatis-config.xml")) {
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    }

    // populate in-memory database
    BaseDataTest.runScript(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(), "./CreateDB.sql");
  }

  /**
   * 一级缓存的作用范围是session
   * 这也体现了事务的持久性，一旦事务提交数据将永久生效
   * 但是在提交之前数据都有可能回滚，数据操作不生效
   *
   */
  @Test
  void test() {
    /**
     * 一级缓存的作用范围session
     */
    try (SqlSession session = sqlSessionFactory.openSession()) {
      User user = new User();
      user.setId(6);
      user.setName("test6");
      int i = session.insert("addUser", user);

      assertEquals(1, i);

      /**
       * 本次是从数据库直接读取的，读取之后会存到一级缓存
       *
       * 没有存入二级缓存，二级缓存需要session.commit()之后才会才会真正保存
       * 这也是为什么此处二级缓存没有脏数据的原因
       *
       * 这里数据库的事务隔离级别是读未提交，所以可以读取到六条数据
       */
      List<Object> allUsers = session.selectList("getAllUsers");
      assertEquals(6, allUsers.size());

//      session.commit();

      /**
       * 这次是直接从一级缓存读取的数据，属于脏数据
       */
      List<Object> allUsers2 = session.selectList("getAllUsers");
      assertEquals(6, allUsers2.size());

      session.rollback();

      // session回滚之后，一级缓存就被清除了，这次获取数据是直接从数据库读取
      List<Object> allUsers3 = session.selectList("getAllUsers");
      assertEquals(5, allUsers3.size());
    }

    /**
     * 本次读取的是二级缓存
     *
     * 二级缓存没有读到脏数据，二级缓存只有在分布式环境中才会读到脏数据
     *
     * 二级缓存的作用范围：应用程序启动后的一个namespace
     *
     * 数据回滚之后，依然只读到五条，说明刚才读取的是脏数据
     */
    try (SqlSession session = sqlSessionFactory.openSession()) {
     List<Object> allUsers = session.selectList("getAllUsers");
      assertEquals(5, allUsers.size());
    }

  }


}
