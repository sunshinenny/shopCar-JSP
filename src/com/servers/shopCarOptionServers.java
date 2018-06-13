package com.servers;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import com.dao.shopCarOptionDao;
import com.domain.GoodsList;

public class shopCarOptionServers {

	/**
	 * 将商品信息存入购物车表中
	 * 
	 * @param strId
	 * @param name
	 * @param price
	 * @param num
	 * @param username
	 * @throws NamingException
	 * @throws SQLException
	 */
	public static boolean add(String strId, String name, float price, int num, String username)
			throws SQLException, NamingException {

		return shopCarOptionDao.add(strId, name, price, num, username);

	}

	/**
	 * 清空购物车表
	 * @param username 
	 * 
	 * @return
	 */
	public static boolean clear(String username) {
		return shopCarOptionDao.clear(username);
	}

	/**
	 * 根据商品名移除数量
	 * 
	 * @param name
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 */
	public static boolean remove(String name) throws NamingException, SQLException {
		return shopCarOptionDao.remove(name);
	}

	/**
	 * 
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 */
	public static ArrayList<GoodsList> showCar(String username) throws SQLException, NamingException {
		return shopCarOptionDao.show(username);
	}

	/**
	 * 对比购物车表和库存表 减少库存中的数量
	 * @param username 
	 * 
	 * @return
	 * @throws NamingException
	 * @throws SQLException
	 */

	public static boolean buy(String username) throws SQLException, NamingException {
		// TODO Auto-generated method stub
		return shopCarOptionDao.buy(username);
	}

	/**
	 * 将购物车中所有者为noLogin的商品修改成登陆后的用户 需要解决的问题，如果该用户已存在对应商品，需要提取购物车中的商品数量与其相加
	 * 
	 * @param formUsername
	 * @return 
	 * @throws NamingException 
	 * @throws SQLException 
	 */
	public static boolean changeUser(String formUsername) throws SQLException, NamingException {
		return shopCarOptionDao.changeUser(formUsername);

	}
}
