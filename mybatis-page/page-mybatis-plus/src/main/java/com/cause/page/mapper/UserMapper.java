package com.cause.page.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cause.page.bean.User;
import org.apache.ibatis.annotations.Mapper;

 @Mapper
public interface UserMapper {

    IPage<User> getAllUsers(IPage<User> page);

}
