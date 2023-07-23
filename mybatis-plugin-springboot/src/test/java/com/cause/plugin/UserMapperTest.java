package com.cause.plugin;

import com.cause.plugin.bean.User;
import com.cause.plugin.mapper.UserMapper;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

public class UserMapperTest extends BaseTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void test() {
        List<User> allUsers = userMapper.getAllUsers();
        System.out.println(allUsers.toString());
    }

}
