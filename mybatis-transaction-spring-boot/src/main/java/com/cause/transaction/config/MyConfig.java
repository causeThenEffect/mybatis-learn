package com.cause.transaction.config;

import org.apache.ibatis.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;

/**
 * @author cause
 * @date 2021/11/26
 */
@Configuration
public class MyConfig {

  @Autowired
  DataSource dataSource;

  @Bean
  public TransactionManager transactionManager() {
    DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
    dataSourceTransactionManager.setNestedTransactionAllowed(true);
    return dataSourceTransactionManager;
  }

}
