package com.cause.transaction.propagation.service;

import com.cause.transaction.propagation.mapper.UserMapper;
import com.cause.transaction.propagation.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cause
 * @date 2021/11/24
 */
@Service
public class UserService {

  /**
   * 一个mapper方法调用结束之后关闭当前session
   * 但是再调用mapper方法，创建新的session，connection依然不变
   * 可以理解为一个mapper绑定了一个connection
   */
  @Autowired
  UserMapper userMapper;

  public List<User> getAllUsers() {
    return userMapper.getAllUsers();
  }

  public void addUser(int id, String name) {
    User user = User.builder().id(id).name(name).build();
    userMapper.addUser(user);
  }

}
