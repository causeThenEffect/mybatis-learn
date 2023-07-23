import com.sun.xml.internal.bind.v2.util.DataSourceSource;
import org.apache.ibatis.datasource.DataSourceFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author cause
 * @date 2021/11/29
 */
@Configuration
public class Config {

  @Bean
  public DataSource dataSource(){
    return null;
  }

  @Bean
  public SqlSessionFactory sqlSessionFactory() throws Exception {
    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
    factoryBean.setDataSource(dataSource());
    return factoryBean.getObject();
  }

}
