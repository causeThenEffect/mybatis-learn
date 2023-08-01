package com.cause.plugin.config;

import com.cause.plugin.interceptor.ConsumeTimeInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Properties;

/**
 * @author cause
 */
@Configuration
public class MybatisPalAutoConfigure implements InitializingBean {

  @Autowired
  private List<SqlSessionFactory> sqlSessionFactoryList;

  @Value("${limitMilliSecond}")
  private String limitMilliSecond;

  @Override
  public void afterPropertiesSet() {

    ConsumeTimeInterceptor interceptor = new ConsumeTimeInterceptor();
    Properties properties = new Properties();
    properties.setProperty("limitMilliSecond", limitMilliSecond);
    interceptor.setProperties(properties);
    for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
      org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
      if (!containsInterceptor(configuration, interceptor)) {
        configuration.addInterceptor(interceptor);
      }
    }
  }

  /**
   * 是否已经存在相同的拦截器
   *
   * @param configuration
   * @param interceptor
   * @return
   */
  private boolean containsInterceptor(org.apache.ibatis.session.Configuration configuration, Interceptor interceptor) {
    try {
      // getInterceptors since 3.2.2
      return configuration.getInterceptors().contains(interceptor);
    } catch (Exception e) {
      return false;
    }
  }

}
