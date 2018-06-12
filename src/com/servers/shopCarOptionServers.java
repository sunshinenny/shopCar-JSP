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
	public static boolean add(String strId, String name, float price, int num, String username) throws SQLException, NamingException {

		return shopCarOptionDao.add(strId, name, price, num,username);

	}

	/**
	 * 清空购物车表
	 * 
	 * @return
	 */
	public static boolean clear() {
		return shopCarOptionDao.clear();
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
	 * 
	 * @return
	 * @throws NamingException 
	 * @throws SQLException 
	 */

	public static boolean buy() throws SQLException, NamingException {
		System.out.println("function is in servers");
		// TODO Auto-generated method stub
		return shopCarOptionDao.buy();
	}
}
