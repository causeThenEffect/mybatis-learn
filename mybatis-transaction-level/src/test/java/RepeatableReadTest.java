import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author cause
 * @date 2021/11/21
 */
@Slf4j
public class RepeatableReadTest {

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
   * 事务的隔离级别是相对于当前事务来说的
   * 看当前事务可以接受什么样的影响程度
   * 然后就设置什么样的事务隔离级别
   *
   * mysql底层通过视图的方式实现了可以重复读取
   *
   * @throws InterruptedException
   */
  @Test
  void test() throws InterruptedException {

//    try (SqlSession session = sqlSessionFactory.openSession(TransactionIsolationLevel.READ_COMMITTED)) {
    try (SqlSession session = sqlSessionFactory.openSession(TransactionIsolationLevel.REPEATABLE_READ)) {
      // 开启事务
      List<User> list = session.selectList("getAllUsers");
      assertEquals(5, list.size());

      // 等待子线程插入数据
      startThread();

      // 等待子线程插入数据
      Thread.sleep(1000);

      // 再次读取依然是刚才的数据，说明数据可以重复读取
      List<User> list2 = session.selectList("getAllUsers");
      assertEquals(5, list2.size());


      /**
       *
       * 可重复读的问题解决了，但是又出现了幻读问题
       *
       * sqlSessionFactory.openSession(TransactionIsolationLevel.REPEATABLE_READ)
       * 出现幻读问题
       * 子线程插入数据成功
       * 主线程事务回滚
       */
      try {
        insertUser(session, "user66666");
      } catch (Exception e) {
        e.printStackTrace();
      }

    }

    try (SqlSession session = sqlSessionFactory.openSession(TransactionIsolationLevel.REPEATABLE_READ)) {
      User user = session.selectOne("getUsersById", 6);
      assertEquals("user6", user.getName());
    }

  }

  public Thread startThread() {
    Thread thread1 = new Thread(() -> {
      try (SqlSession session2 = sqlSessionFactory.openSession()) {
        insertUser(session2, "user6");
      }
    });
    thread1.start();
    return thread1;
  }

  public void insertUser(SqlSession session, String name) {
    User user2 = new User();
    user2.setId(6);
    user2.setName(name);
    int j = session.insert("addUser", user2);
    assertEquals(1, j, "数据插入失败");
    // session如果没有主动commit，close的时候刚才的操作将会被回滚
    session.commit();
  }

}
