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
public class ReadCommittedTest {

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
   * 事务的隔离级别是相对于当前线程来说的，看你想要保证当前线程隔离级别有多高
   * @throws InterruptedException
   */
  @Test
  void test() throws InterruptedException {

    startThread();

    // 等待子线程执行，开启子线程就相当于开启了一个事务
    Thread.sleep(10000);

    try (SqlSession session = sqlSessionFactory.openSession(TransactionIsolationLevel.READ_COMMITTED)) {
//    try (SqlSession session = sqlSessionFactory.openSession(TransactionIsolationLevel.READ_UNCOMMITTED)) {
      // 开启事务
      List<User> list = session.selectList("getAllUsers");
      assertEquals(5, list.size());

      // 等待子线程提交数据
      Thread.sleep(10000);

      // 再次读取依然是刚才的数据，说明数据可以重复读取
      List<User> list2 = session.selectList("getAllUsers");
      assertEquals(6, list2.size());
    }

  }

  public Thread startThread() {
    Thread thread1 = new Thread(() -> {
      try (SqlSession session2 = sqlSessionFactory.openSession()) {
        insertUser(session2, "user6", 10000);
      }
    });
    thread1.start();
    return thread1;
  }

  public void insertUser(SqlSession session, String name, long sleepTime) {
    User user2 = new User();
    user2.setId(6);
    user2.setName(name);
    int j = session.insert("addUser", user2);
    assertEquals(1, j, "数据插入失败");
    // session如果没有主动commit，close的时候刚才的操作将会被回滚
    if (sleepTime > 0 ) {
      try {
        Thread.sleep(sleepTime);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    session.commit();
  }

}
