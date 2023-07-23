import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class MaperBindingTest {

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
  void autoCommitTest() {
    /**
     * 记住一次session只打开一个connection
     *
     * 如果是自动提交其实，其实相当于在mysql服务器开启了一次事务
     * 事务交给mysql自己处理，mysql客户端只关心执行结果就可以了
     */
    try(SqlSession session = sqlSessionFactory.openSession(false)) {
      Mapper mapper = session.getMapper(Mapper.class);
      assertEquals(5, mapper.getAllUsers().size());
      assertEquals(2, mapper.getUsersById(2).getId());

      User user = new User();
      user.setId(6);
      user.setName("user6");
      assertEquals(1, mapper.addUser(user));

      session.commit();
    }
  }

}
