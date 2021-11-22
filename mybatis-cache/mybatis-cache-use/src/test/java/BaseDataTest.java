/*
 *    Copyright 2009-2021 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;

public class BaseDataTest {

  /**
   * 根据datasource获取connection，在获取sql执行器
   *
   * @param ds
   * @param resource
   * @throws IOException
   * @throws SQLException
   */
  public static void runScript(DataSource ds, String resource) throws IOException, SQLException {
    try (Connection connection = ds.getConnection()) {
      ScriptRunner runner = new ScriptRunner(connection);
      runner.setAutoCommit(true);
      runner.setStopOnError(false);
      runner.setLogWriter(null);
      runner.setErrorLogWriter(null);
      runScript(runner, resource);
    }
  }

  /**
   *
   * 使用sql执行器，执行相应的sql
   *
   * @param runner
   * @param resource
   * @throws IOException
   * @throws SQLException
   */
  public static void runScript(ScriptRunner runner, String resource) throws IOException {
    try (Reader reader = Resources.getResourceAsReader(resource)) {
      runner.runScript(reader);
    }
  }

}
