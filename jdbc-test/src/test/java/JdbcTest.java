import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author cause
 * @date 2021/11/24
 */
@Slf4j
public class JdbcTest {

    @BeforeAll
    public static void createTable() throws ClassNotFoundException, SQLException {
        Class.forName("org.hsqldb.jdbcDriver");
        try (Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:cache", "sa", "");
             Statement statement = conn.createStatement()) {
            statement.executeUpdate("create table users (id int,name varchar(20));");
            statement.executeUpdate("insert into users values(1, 'User1')");
        }
    }

    @Test
    public void list() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users where id = ?";

        Class.forName("org.hsqldb.jdbcDriver");
        try (Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:cache", "sa", "");
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, 1);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                log.info(id + "=======>" + name);
            }
        }
    }

    @Test
    void insertTest() {
        Connection conn = null;
        Statement statement = null;
        try {
            // 加载注册驱动
            Class.forName("org.hsqldb.jdbcDriver");
            // 获取数据库连接
            conn = DriverManager.getConnection("jdbc:hsqldb:mem:cache", "sa", "");
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
