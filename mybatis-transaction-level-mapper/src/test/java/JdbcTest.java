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
    String sql = "SELECT * FROM users";
    Connection conn = null;
//    Statement st = null;
    ResultSet rs = null;
    PreparedStatement ps = null;
    try {
      // 1. 加载注册驱动
      Class.forName("com.mysql.cj.jdbc.Driver");
      // 2. 获取数据库连接
      conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/page", "root", "root");
      // 3. 创建语句对象
      ps = conn.prepareStatement(sql);
      // 4. 执行SQL语句
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
  void add() {
    String sql = "insert into users(id,name) values (?,?)";
    Connection conn = null;
//    Statement st = null;
    ResultSet rs = null;
    PreparedStatement ps = null;
    try {
      // 加载注册驱动
      Class.forName("com.mysql.cj.jdbc.Driver");
      // 获取数据库连接
      conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/page", "root", "root");
      conn.setAutoCommit(false);
      // 创建语句对象
      ps = conn.prepareStatement(sql);
      ps.setInt(1, 7);
      ps.setString(2, "user6");
      // 执行SQL语句
      int i = ps.executeUpdate();
      assertEquals(1, i);
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
            if (!conn.getAutoCommit()) {
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
