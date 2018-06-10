package com.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * 使用数据库连接池技术 进行数据库连接
 * 
 * @author sunshinenny
 *
 */

public class JDBCUnit {
	/**
	 * static的conn方法，在该类外调用，可以直接连接数据库
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 */
	public static Connection conn() throws SQLException, NamingException {
		//从META-INF中定义的context.xml中获取shopCarDatabase对象，并新建连接池
		Context c = new InitialContext();
		DataSource ds = (DataSource) c.lookup("java:comp/env/shopCarDatabase");
		//使用连接池的默认连接方式，新建connection对象
		Connection conn = ds.getConnection();
		//返回connection对象，以便其他类连接数据库
		return conn;
	}

	/**
	 * 关闭数据库连接
	 * @param rs
	 * @param stm
	 * @param conn
	 */
	public static void close(java.sql.ResultSet rs, java.sql.Statement stm, Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				rs = null;
			}
		}
		if (stm != null) {
			try {
				stm.close();// 可能存在错误
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				stm = null;
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				conn = null;
			}
		}
	}
}
