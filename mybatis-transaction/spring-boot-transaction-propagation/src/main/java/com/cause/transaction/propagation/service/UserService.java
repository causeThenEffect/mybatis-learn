package com.cause.transaction.propagation.service;

import com.cause.transaction.propagation.mapper.UserMapper;
import com.cause.transaction.propagation.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * @author cause
 * @date 2021/11/24
 */
@Service
@Slf4j
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

  /**
   * 测试spring事务失效的场景
   *
   *
     Spring在扫描Bean的时候会自动为标注了@Transactional注解的类生成一个代理类（proxy）,
     当有注解的方法被调用的时候，实际上是代理类调用的，代理类在调用之前会开启事务，
     执行事务的操作，但是同类中的方法互相调用，相当于this.B()，此时的B方法并非是代理类调用，
     而是直接通过原有的Bean直接调用，所以注解会失效
   */
  public void addUser() {
    addUser(6, "user6");
    addUser2();
  }

  @Transactional(rollbackFor = Exception.class)
  public void addUser2() {
    addUser(7, "user7");
    addUser(7, "user7");
  }

  /**
   * 事务抛出的异常被捕获了，所以事务失效了
   */
  @Transactional(rollbackFor = Exception.class)
  public void addUser3() {
    addUser(6, "user6");
    try {
      addUser(6, "user6");
    } catch (Exception e) {
      log.info(e.getMessage());
    }
  }

}
