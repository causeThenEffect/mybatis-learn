package com.cause.transaction;

import com.cause.transaction.service.TestUtil;
import lombok.extern.slf4j.Slf4j;
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
