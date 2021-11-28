package com.cause.transaction.propagation;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.IOException;

/**
 * @author cause
 * @date 2021/11/25
 */
public class TestUtil {

  /**
   * 新建表结构和数据，排除测试干扰因素
   *
   * @param sqlSessionFactory
   * @throws Exception
   */
  public static void initDataBase(SqlSessionFactory sqlSessionFactory) {
    ScriptRunner runner = new ScriptRunner(sqlSessionFactory.openSession(true).getConnection());
    try {
      runner.runScript(Resources.getResourceAsReader("createDB.sql"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
