package com.cause.page;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cause.page.bean.User;
import com.cause.page.mapper.UserMapper;
import org.junit.Test;

import javax.annotation.Resource;

public class UserMapperTest extends BaseTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void test() {
        Page<User> page = new Page<>(1,2);
        IPage<User> allUsers = userMapper.getAllUsers(page);
        System.out.println(JSON.toJSONString(allUsers));
    }

}
