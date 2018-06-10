package com.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.NamingException;

import com.domain.GoodsList;

public class getGoodsInfoDao {
	/**
	 * 从数据库中获取商品信息，并以GoodsList对象，存入到goodsList集合中返回给servers
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 */
	public ArrayList<GoodsList> goodsInfo() throws SQLException, NamingException {
		//建立数据库连接
		Connection conn=JDBCUnit.conn();
		//新建GoodsList格式的goodsList集合对象
		ArrayList<GoodsList> goodsList=new ArrayList<GoodsList>();
		//打开连接
		Statement stm = conn.createStatement();
		//执行查询语句，并存入ResultSet格式的rs对象内
		//查询语句意思为：从goods表中查询所有
		ResultSet rs = stm.executeQuery("select * from goods");
		//遍历rs对象
		while (rs.next()) {
			//从rs中得到目标数据
			String name=rs.getString("name");
			float price=rs.getFloat("price");
			int num=rs.getInt("num");
			//新建临时GoodsList对象
			GoodsList temp=new GoodsList();
			//设置临时对象的值
			temp.setName(name);
			temp.setPrice(price);
			temp.setNum(num);
			//将临时GoodsList对象存入goodsList集合中
			goodsList.add(temp);
		}
		//关闭数据库
		JDBCUnit.close(rs, stm, conn);
		//返回goodsList集合对象
		return goodsList;
	}
}
