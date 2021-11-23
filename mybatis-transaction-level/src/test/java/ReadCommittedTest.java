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

  @Test
  void test() throws InterruptedException {
    try (SqlSession session = sqlSessionFactory.openSession(TransactionIsolationLevel.READ_COMMITTED)) {
//    try (SqlSession session = sqlSessionFactory.openSession(TransactionIsolationLevel.READ_UNCOMMITTED)) {
      // 开启事务
      List<User> list = session.selectList("getAllUsers");
      assertEquals(5, list.size());

      // 开启子线程就相当于开启了一个事务
      Thread thread1 = startThread();

      // 等待子线程修改数据，但是并没有提交
      Thread.sleep(1000);

      // 看本次是否读到脏数据
      List<User> list2 = session.selectList("getAllUsers");
      assertEquals(5, list2.size());

      // 等待子线程执行结束
      thread1.join();

      // 暴露了不能重复读取问题
      List<User> list3 = session.selectList("getAllUsers");
      assertEquals(6, list3.size());
    }

  }

  public Thread startThread() {
    Thread thread1 = new Thread(() -> {
      try (SqlSession session2 = sqlSessionFactory.openSession()) {
        // 两秒之后再提交数据
        insertUser(session2, "user6", 2000);
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
