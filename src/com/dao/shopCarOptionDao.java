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
		ResultSet getSingle = stm
				.executeQuery("select * from shopCar where name='" + name + "' and username='" + username + "'");
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
				conn.close();
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
						// 由于一个商品只会出现一次，在成功修改完一次之后即可return true
						return true;
					}

				}
				// 下面的代码理论上可以删除，但目前应该不影响结果
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
			conn.close();
			return false;
		}
	}

	/**
	 * 清空shopCar表
	 * 
	 * @param username
	 * 
	 * @return
	 */
	public static boolean clear(String username) {
		// 建立连接
		Connection conn;
		try {
			conn = JDBCUnit.conn();
			Statement stm = conn.createStatement();
			// 删除当前用户购物车内但所有商品信息
			stm.executeUpdate("DELETE FROM shopCar WHERE username = '" + username + "'");
			// 关闭数据库
			conn.close();
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
	 * 将数据库中的内容存入GoodsList格式的集合中，用以shopCar.jsp进行显示 仅显示当前用户但购物车
	 * 以后可以考虑增加administer模式，可以直接查看所有人但购物车
	 * 
	 * @param username
	 * 
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 */
	public static ArrayList<GoodsList> show(String username) throws SQLException, NamingException {
		if (username == null) {
			username = "noLogin";
		}
		ArrayList<GoodsList> list = new ArrayList<GoodsList>();
		Connection conn = JDBCUnit.conn();
		Statement stm = conn.createStatement();
		ResultSet rs = stm.executeQuery("select * from shopCar where username='" + username + "'");
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
		conn.close();
		return list;
	}

	/**
	 * 对比购物车表和库存表 减少库存中的数量
	 * 
	 * @param username
	 * 
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 */
	public static boolean buy(String username) throws SQLException, NamingException {
		// 连接数据库
		Connection conn = JDBCUnit.conn();
		// 获取库存信息
		Statement stmAll = conn.createStatement();
		ResultSet rsAll = stmAll.executeQuery("select * from goods");
		// 获取购物车信息
		Statement stmShopCar = conn.createStatement();
		ResultSet rsShopCar = stmShopCar.executeQuery("select * from shopCar where username='" + username + "'");
		// 库存信息作为外循环开始遍历
		try {
			while (rsAll.next()) {
				String outName = rsAll.getString("name");
				int outNum = rsAll.getInt("num");
				while (rsShopCar.next()) {
					String inName = rsShopCar.getString("name");
					int inNum = rsShopCar.getInt("num");
					if (outName.equals(inName)) {
						int cut = outNum - inNum;
						if (changeKuCun(outName, cut, username)) {
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
	 * @param username
	 * @throws SQLException
	 * @throws NamingException
	 */
	private static boolean changeKuCun(String outName, int i, String username) throws SQLException, NamingException {
		Connection conn = JDBCUnit.conn();
		try {
			Statement stm = conn.createStatement();
			stm.executeUpdate("UPDATE goods SET num=" + i + " where name='" + outName + "' ");
			// 调用上方定义的清空购物车方法
			clear(username);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return false;
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
		Connection conn = JDBCUnit.conn();
		try {
			// 获取未登陆时的项目，即username=nologin的商品
			Statement originUser = conn.createStatement();
			// 获取登陆后的项目，即username=loginname的商品
			Statement loginUser = conn.createStatement();
			// 获取username=nologin的集合
			ResultSet originResultSet = originUser.executeQuery("select * from shopCar where username='noLogin'");
			// 获取shopcar表中username=loginname的集合
			// 可能bug出现在这里，应该不仅仅已有的修改数量，没有的增加到指定用户购物车中
			ResultSet loginResultSet = loginUser
					.executeQuery("select * from shopCar where username='" + formUsername + "'");
			try {
				// 开始遍历未登陆用户商品集合
				while (originResultSet.next()) {
					// 获取未登陆用户的商品名&数量，用于与登陆后用户的购物车进行计算
					String originName = originResultSet.getString("name");
					int originNum = originResultSet.getInt("num");
					// 开始遍历登陆用户商品集合
					while (loginResultSet.next()) {
						// 获取登陆用户的商品名&数量
						String loginName = loginResultSet.getString("name");
						int loginNum = loginResultSet.getInt("num");
						// 如果未登陆用户商品名和登陆用户已有的商品名相同，则执行登陆用户商品数量相加操作
						if (originName.equals(loginName)) {
							int add = originNum + loginNum;
							if (changeUserName(originName, add, formUsername, "add")) {
								continue;
								// 如果修改成功，直接跳出内部遍历，防止使用add方法时将商品数量加一了
							} else {
								System.out.println("购买操作失败，请重试");
							}
						}
						// 如果登陆用户没有该商品，添加到其购物车中
						else {
							if (changeUserName(originName, originNum, formUsername, "change")) {
								continue;
							} else {
								System.out.println("购买操作失败，请重试");
							}
						}
					}
					loginResultSet.beforeFirst();
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// 关闭数据库
				conn.close();
			}
			// UPDATE goods SET num=" + i + " where name='" + outName + "' "
			// 调用上方定义的清空购物车方法
			clear(formUsername);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return false;
	}

	/**
	 * 改变username为nologin的商品，改为登陆的用户名
	 * 
	 * @param originName
	 * @param add
	 * @param formUsername
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 */
	private static boolean changeUserName(String originName, int num, String formUsername, String act)
			throws SQLException, NamingException {
		// 建立连接
		Connection conn = JDBCUnit.conn();
		try {
			Statement stm = conn.createStatement();
			// 如果操作为add，则修改登录账号的数量
			if (act.equals("add")) {
				stm.executeUpdate("UPDATE shopCar SET num=" + num + " where name='" + originName + "' and username='"
						+ formUsername + "' ");
				stm.executeUpdate("DELETE FROM shopCar WHERE username = 'noLogin' and name ='" + originName + "'");
			} else if (act.equals("change")) {
				// 如果操作为change，则修改nologin用户为登陆后账号
				stm.executeUpdate("UPDATE shopCar SET username='" + formUsername + "' where name='" + originName
						+ "' and username='noLogin' ");
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return false;
	}
}