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
public class SerializableTest {

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
   *
   * mysql通过锁机制现实串行化
   *
   * 一个session拥有一个executor
   * 一个executor拥有一个transaction
   * 一个transaction拥有一个connection
   * 拥有了connection就拥有一个物理事务
   *
   * @throws InterruptedException
   */
  @Test
  void test() throws InterruptedException {

    try (SqlSession session = sqlSessionFactory.openSession(TransactionIsolationLevel.SERIALIZABLE)) {
//    try (SqlSession session = sqlSessionFactory.openSession(TransactionIsolationLevel.REPEATABLE_READ)) {
      // 开启事务
      List<User> list = session.selectList("getAllUsers");
      assertEquals(5, list.size());

      // 开启子线程
      startThread();

      // 等待子线程插入数据
      Thread.sleep(1000);

      // 再次读取依然是刚才的数据，说明数据可以重复读取
      List<User> list2 = session.selectList("getAllUsers");
      assertEquals(5, list2.size());

      /**
       *
       * sqlSessionFactory.openSession(TransactionIsolationLevel.REPEATABLE_READ)
       * 出现幻读问题
       * 子线程插入数据成功
       * 主线程事务回滚
       *
       * sqlSessionFactory.openSession(TransactionIsolationLevel.SERIALIZABLE)
       * 解决幻读问题
       * 主线程插入数据成功
       * 子线程事务回滚
       *
       */
      insertUser(session, "user66666");

      User user = session.selectOne("getUsersById", 6);
      assertEquals("user66666", user.getName());
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
