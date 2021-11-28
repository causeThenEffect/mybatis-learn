package com.cause.transaction.propagation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;

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
