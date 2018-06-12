package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.NamingException;

import com.domain.GoodsList;

/**
 * 关于购物车「shopCar表」的数据库操作
 * 
 * @author sunshinenny
 *
 */

public class shopCarOptionDao {
	/**
	 * 判断是否存在此商品，如果存在，将其数量加一 or 如果不存在，添加此商品
	 * 
	 * @param strId
	 * @param name
	 * @param price
	 * @param num
	 * @param username
	 * @return
	 * @throws NamingException
	 * @throws SQLException
	 */
	public static boolean add(String strId, String name, float price, int num, String username)
			throws SQLException, NamingException {
		// 新建连接
		Connection conn = JDBCUnit.conn();
		Statement stm = conn.createStatement();
		// sql语句-在shopCar表中查找名字叫做name的商品「name代表商品名称」
		ResultSet getSingle = stm.executeQuery("select * from shopCar where name='" + name + "'");
		// 计算该ResultSet对象的长度，用以判断该商品是否已经存在于数据库中
		// 跳转到rs最后一项，并获取行号
		// 或许还有一个办法，直接返回stm查询到的数量即可
		getSingle.last();
		int length = getSingle.getRow();
		// 重新返回第一项，用以后期遍历「如果需要遍历的话」
		getSingle.beforeFirst();
		try {
			// 如果长度为0，代表数据库不存在此商品
			if (length == 0) {
				// 将商品压入数据库
				PreparedStatement pStmt = conn
						.prepareStatement("insert into shopCar (id,name,price,num,username) value(?,?,?,?,?)");
				pStmt.setString(1, strId);
				pStmt.setString(2, name);
				pStmt.setLong(3, (long) price);
				pStmt.setLong(4, num);
				pStmt.setString(5, username);
				pStmt.executeUpdate();
				JDBCUnit.close(null, null, conn);
				return true;
			} else {
				// 否则将其数量加1
				while (getSingle.next()) {
					// 对购买的用户进行判断，进而修改单个用户的购买内容
					if (getSingle.getString("username").equals(username)) {
						// 遍历ReaultSet对象
						// 获取已知的商品数量
						int singleNum = getSingle.getInt("num") + 1;
						/**
						 * 直接写在循环中是因为相信同样的商品在数据库中只会出现一次
						 * 但也有问题：如果这个数据库存在“编号不相同，但是名字相同的商品时，此处就会出现修改数量错误的bug”
						 */
						Statement wantAddOneSingleStatement = conn.createStatement();
						// sql语句-查找shopCar中商品名「name」等于传递而来的name的商品，并将其数量更新为singleNum「此时的singleNum的数量已经加一了」
						wantAddOneSingleStatement.executeUpdate("UPDATE shopCar SET num=" + singleNum + " where name='"
								+ name + "' and username='" + username + "' ");
						// 关闭数据库
						conn.close();
						return true;
					}

				}
				// 遍历结束后如果没有return回去，说明没有该用户，则针对该用户增加指定商品
				PreparedStatement pStmt = conn
						.prepareStatement("insert into shopCar (id,name,price,num,username) value(?,?,?,?,?)");
				pStmt.setString(1, strId);
				pStmt.setString(2, name);
				pStmt.setLong(3, (long) price);
				pStmt.setLong(4, num);
				pStmt.setString(5, username);
				pStmt.executeUpdate();
				conn.close();
				return true;

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			JDBCUnit.close(null, null, conn);
			return false;
		}
	}

	/**
	 * 清空shopCar表
	 * 
	 * @return
	 */
	public static boolean clear() {
		// 建立连接
		Connection conn;
		try {
			conn = JDBCUnit.conn();
			Statement stm = conn.createStatement();
			// sql语句-清空shopCar表
			stm.executeUpdate("truncate table shopCar");

			// 关闭数据库
			JDBCUnit.close(null, stm, conn);
			return true;
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (NamingException e) {

			e.printStackTrace();
		}
		return false;

	}

	/**
	 * 根据商品名移除数量
	 * 
	 * @param name
	 * @return
	 * @throws NamingException
	 * @throws SQLException
	 */
	public static boolean remove(String name) throws NamingException, SQLException {
		int num;
		Connection conn = JDBCUnit.conn();

		try {
			Statement stm1 = conn.createStatement();
			ResultSet rs = stm1.executeQuery("select * from shopCar where name='" + name + "'");
			while (rs.next()) {
				/**
				 * 遍历数据表 如果对应的商品数量为0——删除该商品 如果商品数量不为0——将其数量减1 如果减少到了0——删除该商品
				 */
				num = rs.getInt("num");// 获取数据库中的num信息，再将其减去1
				Statement stm2 = conn.createStatement();
				if (num != 0) {
					num -= 1;
					stm2.executeUpdate("UPDATE shopCar SET num=" + num + " where name='" + name + "' ");
					if (num == 0)
						stm2.executeUpdate("delete from shopCar where name='" + name + "'");

				} else
					stm2.executeUpdate("delete from shopCar where name='" + name + "'");
			}
			// 关闭数据库
			conn.close();
			return true;
		} catch (SQLException e) {
			// 关闭数据库
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return false;
	}

	/**
	 * 将数据库中的内容存入GoodsList格式的集合中，用以shopCar.jsp进行显示
	 * @param username 
	 * 
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 */
	public static ArrayList<GoodsList> show(String username) throws SQLException, NamingException {
		if(username==null){
			username="noLogin";
		}
		ArrayList<GoodsList> list = new ArrayList<GoodsList>();
		Connection conn = JDBCUnit.conn();
		Statement stm = conn.createStatement();
		ResultSet rs = stm.executeQuery("select * from shopCar where username='"+username+"'");
		while (rs.next()) {
			String gName = rs.getString("name");
			float gPrice = rs.getFloat("price");
			int gNum = rs.getInt("num");
			GoodsList temp = new GoodsList();
			temp.setName(gName);
			temp.setPrice(gPrice);
			temp.setNum(gNum);
			list.add(temp);

		}
		// 关闭数据库
		JDBCUnit.close(rs, stm, conn);
		return list;
	}

	/**
	 * 对比购物车表和库存表 减少库存中的数量
	 * 
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 */
	public static boolean buy() throws SQLException, NamingException {
		System.out.println("function is in dao.buy");
		// 连接数据库
		Connection conn = JDBCUnit.conn();
		// 获取库存信息
		Statement stmAll = conn.createStatement();
		ResultSet rsAll = stmAll.executeQuery("select * from goods");
		// 获取购物车信息
		Statement stmShopCar = conn.createStatement();
		ResultSet rsShopCar = stmShopCar.executeQuery("select * from shopCar");
		System.out.println("before next");
		// 库存信息作为外循环开始遍历
		try {
			while (rsAll.next()) {
				System.out.println("in next");
				String outName = rsAll.getString("name");
				int outNum = rsAll.getInt("num");
				while (rsShopCar.next()) {
					System.out.println("in in next");
					String inName = rsShopCar.getString("name");
					int inNum = rsShopCar.getInt("num");
					if (outName.equals(inName)) {
						int cut = outNum - inNum;
						System.out.println("the same name is" + outName + "\nand the num is" + outNum + "\ninNum is"
								+ inNum + "\nand the cut num is " + cut);
						if (changeKuCun(outName, cut)) {
							continue;
						} else {
							System.out.println("购买操作失败，请重试");
						}
					}
				}
				rsShopCar.beforeFirst();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭数据库
			conn.close();
		}
		return false;

	}

	/**
	 * 执行数据库更新操作
	 * 
	 * @param outName
	 * @param i
	 * @throws SQLException
	 * @throws NamingException
	 */
	private static boolean changeKuCun(String outName, int i) throws SQLException, NamingException {
		System.out.println("function is in dao.changeKuCun");
		Connection conn = JDBCUnit.conn();
		try {
			Statement stm = conn.createStatement();
			stm.executeUpdate("UPDATE goods SET num=" + i + " where name='" + outName + "' ");
			// 调用上方定义的清空购物车方法
			clear();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return false;
	}
}