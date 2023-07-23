import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author cause
 * @date 2021/11/24
 */
public class JdbcTest {

  @Test
  void list() {
    String sql = "SELECT * FROM users where id > ?";
    Connection conn = null;
//    Statement st = null;
    ResultSet rs = null;
    PreparedStatement ps = null;
    try {
      // 加载注册驱动
      Class.forName("com.mysql.cj.jdbc.Driver");
      // 获取数据库连接
      conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/page", "root", "root");
      // 创建语句对象
      ps = conn.prepareStatement(sql);
      ps.setInt(1, 2);
      // 执行SQL语句
      rs = ps.executeQuery();
      while (rs.next()) {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        System.out.println(id + "=======>" + name);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (ps != null) {
          ps.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      } finally {
        try {
          if (conn != null) {
            conn.close();
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  @Test
  void insertTest() {
    Connection conn = null;
    Statement statement = null;
    try {
      // 加载注册驱动
      Class.forName("com.mysql.cj.jdbc.Driver");
      // 获取数据库连接
      conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/page", "root", "root");
      /**
       *  关闭事务自动提交
       *  在commit之前的的所有操作具有原子性
       *
       *  如果没有显示的关闭事务自动提交，一条sql默认是一个事务
       */
      conn.setAutoCommit(false);
      // 创建语句对象
      statement = conn.createStatement();
      // 执行insert SQL语句
      int i = statement.executeUpdate("insert into users(id,name) values (44,88)");
      int j = statement.executeUpdate("insert into users(id,name) values (45,88)");
      assertEquals(1, i);
      assertEquals(1, j);
      conn.rollback();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (statement != null) {
          statement.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      } finally {
        try {
          if (conn != null) {
            if (!conn.getAutoCommit()) {
              // 如果没有手动提交事务，事务里的操作会被回滚
              conn.commit();
            }
            conn.close();
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

}
