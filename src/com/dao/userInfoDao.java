package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.NamingException;

//import javax.activation.DataSource;
//import javax.naming.InitialContext;

import com.domain.userInfo;

public class userInfoDao {
	public static boolean add(userInfo userInfo) throws SQLException, NamingException {
		// 链接数据库
		Connection conn = JDBCUnit.conn();
		try {
			String name = userInfo.getUsername();
			String pwd = userInfo.getPassword();
			String email = com.domain.userInfo.getEmail();
			String sex = userInfo.getSex();
			PreparedStatement pStmt = conn
					.prepareStatement("insert into userinfo (userName,password,email,sex) value(?,?,?,?)");
			pStmt.setString(1, name);
			pStmt.setString(2, pwd);
			pStmt.setString(3, email);
			pStmt.setString(4, sex);
			pStmt.executeUpdate();
			JDBCUnit.close(null, null, conn);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			JDBCUnit.close(null, null, conn);
			return false;
		}
	}
	/**
	 * 登录时对用户数据进行核对
	 * @param name 表单中的name
	 * @param pwd	表单中的password
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 */
	public static boolean check(String name, String pwd) throws SQLException, NamingException {
		// 建立连接
		Connection conn = JDBCUnit.conn();
		Statement stm = conn.createStatement();
		ResultSet rs = stm.executeQuery("select * from userinfo where userName='" + name + "'");
		while (rs.next()) {
			String formPassword = rs.getString("password");
			if (formPassword.equals(pwd)) {
				JDBCUnit.close(rs, stm, conn);
				return true;
			} else {
				JDBCUnit.close(rs, stm, conn);
				return false;
			}
		}
		JDBCUnit.close(rs, stm, conn);
		return false;
	}

	/**
	 * 检测数据库中是否有相同的姓名，如果有，则提示用户名被占用
	 * 
	 * @param userNameString
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 */
	public static boolean checkDuplicateName(String userNameString) throws SQLException, NamingException {
		Connection conn = JDBCUnit.conn();
		Statement stm = conn.createStatement();
		ResultSet rs = stm.executeQuery("select * from userinfo where userName='" + userNameString + "'");
		int flag = 0;
		while (rs.next()) {
			flag++;
		}
		if (flag == 0)
			return false;
		else
			return true;
	}
}
