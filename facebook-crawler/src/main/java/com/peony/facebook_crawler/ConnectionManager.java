package com.peony.facebook_crawler;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 管理SQL连接
 * 
 * @author BAO
 *
 */
public class ConnectionManager {

	private static final String SUBJECT_CONFIG = "/DB.properties";


	private static final String DRIVER_PROP = "driver";

	private static final String URL_PROP = "url";

	private static final String USERNAME_PROP = "username";

	private static final String PASSWORD_PROP = "password";

	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);

	private static ConnectionManager instance = null;

	private ComboPooledDataSource dataSource = new ComboPooledDataSource();


	public static ConnectionManager getInstance() {
		if (instance == null) {
			instance = new ConnectionManager();
		}
		return instance;
	}

	private Properties loadConfig(String confFile) throws IOException {
		InputStream in = this.getClass().getResourceAsStream(confFile);
		try {
			Properties props = new Properties();
			props.load(in);
			return props;
		} catch (Exception e) {
			LOGGER.error(e.getMessage() + "加载配置文件失败！", e);
		} finally {
			in.close();
		}
		return null;
	}

	private void testConnection(ComboPooledDataSource dataSource) throws SQLException {
		Connection conn = dataSource.getConnection();
		conn.close();
	}

	public boolean tryInit(ComboPooledDataSource dataSource, String conf) {
		try {
			Properties prop = loadConfig(conf);
			LOGGER.info("初始化数据库配置: " + conf);
			dataSource.setDriverClass(prop.getProperty(DRIVER_PROP));
			dataSource.setJdbcUrl(prop.getProperty(URL_PROP));
			dataSource.setUser(prop.getProperty(USERNAME_PROP));
			dataSource.setPassword(prop.getProperty(PASSWORD_PROP));
			dataSource.setMaxPoolSize(25);
			dataSource.setInitialPoolSize(10);
			testConnection(dataSource);
			LOGGER.info("初始化数据库成功！");
			return true;
		} catch (IOException e) {
			LOGGER.error(e.getMessage() + "加载配置文件失败！", e);
		} catch (SQLException e) {
			LOGGER.error(e.getMessage() + "测试连接失败！", e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage() + "初始化数据库失败！", e);
		}
		LOGGER.error("初始化数据库连接失败！");
		return false;
	}

	public boolean init() {
		if (!tryInit(dataSource, SUBJECT_CONFIG)) {
			LOGGER.error("初始化数据库连接失败");
			return false;
		} else {
			LOGGER.info("初始化数据库连接成功");
		}
		return true;
	}

	public Connection getDBConnection() throws SQLException {
		return dataSource.getConnection();
	}

}
