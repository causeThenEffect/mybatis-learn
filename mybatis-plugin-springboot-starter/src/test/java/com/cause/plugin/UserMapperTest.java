package com.cause.plugin;

import com.cause.plugin.bean.User;
import com.cause.plugin.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {

    @Resource
    private UserMapper userMapper;

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void createTable() throws IOException, SQLException {
        DataSource dataSource = sqlSessionFactory.getConfiguration().getEnvironment().getDataSource();
        try (Connection connection = dataSource.getConnection();
             Reader reader = Resources.getResourceAsReader("CreateDB.sql")) {
            ScriptRunner runner = new ScriptRunner(connection);
            runner.setAutoCommit(true);
            runner.setStopOnError(false);
            runner.setLogWriter(null);
            runner.setErrorLogWriter(null);
            runner.runScript(reader);
        }
    }

    @Test
    public void test() {
        List<User> allUsers = userMapper.getAllUsers();
        System.out.println(allUsers.toString());
    }

}
