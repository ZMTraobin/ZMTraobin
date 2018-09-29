package com.cmig.future.csportal.storm.db;

import org.apache.commons.dbutils.QueryRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 15:23 2017/9/5.
 * @Modified by zhangtao on 15:23 2017/9/5.
 */
public class MyDbUtils {
	private static String className = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://10.17.5.90:3306/hap_dev?useUnicode=true&characterEncoding=utf-8";
	private static String user = "hap_dev";
	private static String password = "hap_dev";
	private static QueryRunner queryRunner = new QueryRunner();

	public static final String INSERT_LOG = "insert into log_info(time,num) values(?,?)";

	public static final String CSP_API_INSERT_LOG = "insert into csp_api_log_storm(APP_USER_ID,DEVICE_TYPE,DEVICE_MODEL,EQUIPMENT_MODEL,REQUEST_URI,CREATE_TIME) values(?,?,?,?,?,?)";

	static{
		try {
			Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws Exception {
		String topdomain = "taobao.com";
		String usetime = "100";
		String currentTime="1444218216106";
		MyDbUtils.update(MyDbUtils.INSERT_LOG, topdomain,usetime,currentTime);
	}

	/**
	 *
	 * @param sql
	 * @param params
	 * @throws SQLException
	 */
	public static void update(String sql,Object... params) throws SQLException {
		Connection connection = getConnection();
		//更新数据
		queryRunner.update(connection,sql, params);
		connection.close();
	}

	/**
	 * @throws SQLException
	 *
	 */
	public static Connection getConnection() throws SQLException {
		//获取mysql连接
		return DriverManager.getConnection(url, user, password);
	}
}
