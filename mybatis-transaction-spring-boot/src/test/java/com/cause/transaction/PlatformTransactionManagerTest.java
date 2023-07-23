package com.cause.transaction;

import com.cause.transaction.mapper.UserMapper;
import com.cause.transaction.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @author cause
 * @date 2021/11/26
 */
@Slf4j
public class PlatformTransactionManagerTest extends BaseTest {

  @Autowired
  PlatformTransactionManager platformTransactionManager;

  @Autowired
  UserMapper userMapper;

  @Test
  void test(){
    initDataBase();

    TransactionDefinition transactionDefinition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_SUPPORTS);
    transactionDefinition.getIsolationLevel();
    TransactionStatus transactionStatus = platformTransactionManager.getTransaction(transactionDefinition);
    try {
      addUser();
//      addUser();
      platformTransactionManager.commit(transactionStatus);
    } catch (Exception e) {
      log.info(e.getMessage());
      platformTransactionManager.rollback(transactionStatus);
    }
  }

  public void addUser() {
    User user = User.builder().id(6).name("user6").build();
    userMapper.addUser(user);
  }

}
