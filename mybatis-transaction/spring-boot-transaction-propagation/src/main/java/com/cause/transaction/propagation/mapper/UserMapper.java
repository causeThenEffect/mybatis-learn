package com.cause.transaction.propagation.mapper;

import com.cause.transaction.propagation.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

  /**
   * todo 一个方法就开启一个session，这是mybatis自身实现的，还是spring框架的特性
   * @return
   */
  List<User> getAllUsers();

  List<User> getAllUsersById();

  User getUsersById(int id);

  int getCounts();

  int addUser(User user);

  int updateUser(User user);

}
