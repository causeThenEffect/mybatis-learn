package com.cause.transaction.propagation;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author cause
 * @date 2021/11/25
 */
@SpringBootTest
public class BaseTest {
  @Autowired
  SqlSessionFactory sqlSessionFactory;

  @BeforeEach
  public void initDataBase() {
    TestUtil.initDataBase(sqlSessionFactory);
  }

}
