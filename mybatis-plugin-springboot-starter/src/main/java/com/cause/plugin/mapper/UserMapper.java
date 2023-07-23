package com.cause.plugin.mapper;

import com.cause.plugin.bean.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    List<User> getAllUsers();

}
