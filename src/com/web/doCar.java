package com.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.domain.GoodsList;
import com.servers.shopCarOptionServers;

/**
 * 此servlet是关于购物车的增删查改，类似于servers的存在
 */
@WebServlet("/doCar")
public class doCar extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public doCar() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 从url中获取操作内容和商品id
		String action = request.getParameter("action");
		String strId = request.getParameter("id");
		// 将id从string转为int
		int id = Integer.parseInt(strId);
		// 获取session中保存的goodSingle对象，将其转为arraylist类型
		ArrayList<GoodsList> goodsList = (ArrayList) request.getSession().getAttribute("goodSingle");
		// 从url中获取操作选项
		if (action.equals("buy")) {
			// 在商店页面点击buy 停留在商店页面
			// 通过id判断购买的商品
			// 利用索引号获取goodsList中的数据
			GoodsList goodSingle = (GoodsList) goodsList.get(id);
			try {
				// 判断购物车中商品数量，如果购物车中数量大于库存数，不允许对其进行添加
				// new一个临时GoodsList型的buyList集合，获取购物车数据库中的信息
				ArrayList<GoodsList> buyList = shopCarOptionServers.showCar();
				// 定义两个变量，获取name和num
				String getBuyListNameString = null;
				int getBuyListNumInt = 0;
				// 用name进行定位，定位成功后，提取出该商品的num
				for (int i = 0; i < buyList.size(); i++) {
					// 利用索引进行集合的遍历
					GoodsList forBuyListNameGoodsList = (GoodsList) buyList.get(i);
					// 获取name
					getBuyListNameString = forBuyListNameGoodsList.getName();
					// 如果name相同，获取num，并直接退出循环
					// BUG：如果购物车中存在多个name，将会导致判断出错
					// Ways：将name的索引改成ID的索引
					if (getBuyListNameString.equals(goodSingle.getName())) {
						getBuyListNumInt = forBuyListNameGoodsList.getNum();
						break;
					}
				}
				// 进行判断，如果购物车内的数量少于库存，则进行添加操作
				if (goodSingle.getNum() > getBuyListNumInt) {
					// System.out.println("添加成功\n" + "库存数为" +
					// goodSingle.getNum() + "\n购物车中的数量为：" + getBuyListNumInt);

					// 调用shopcarOptionServers中add方法，将商品的对应数据存入到shopCar表中
					// 商品数量暂时写死为1，一次数量加1
					if (shopCarOptionServers.add(strId, goodSingle.getName(), goodSingle.getPrice(), 1)) {
						// 返回显示内容，用以shopCarIndex显示
						request.getSession().setAttribute("showWhat", "buy");

					} else {
						System.out.println("添加商品信息到数据库时出现错误「数据库操作已执行」，请重试");
					}
				} else {
					// 输出到js中
					PrintWriter outPrintWriter = response.getWriter();
					outPrintWriter.print("alert");
					// 如果为alert，则提示购买错误
				}
			} catch (SQLException | NamingException e) {
				System.out.println("添加商品信息到数据库时出现错误「数据库操作没有执行」，请重试");
				e.printStackTrace();
			}

		}
		if (action.equals("clear")) {
			// 在购物车界面点击clear 清空购物车并跳转到商店页面
			if (shopCarOptionServers.clear()) {
				// 返回显示内容
				request.getSession().setAttribute("showWhat", "buy");
				// 重定向
				response.sendRedirect("index.jsp");
			} else {
				System.out.println("清空购物车操作出错「数据库操作已执行」，请重试");
				response.sendRedirect("index.jsp");
			}
		}
		if (action.equals("remove")) {
			// 在购物车界面点击remove 停留在购物车界面
			String name = request.getParameter("name");
			try {
				if (shopCarOptionServers.remove(name)) {
					// MyTools.removeItem(name);
					request.getSession().setAttribute("showWhat", "showCar");
					// response.setHeader("refresh", "3;url=/index.jsp");
					response.sendRedirect("index.jsp");
				} else {
					System.out.println("减少商品数量操作出现错误「数据库操作已执行」，请重试");
				}
			} catch (NamingException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if (action.equals("showCar")) {
			// 在商店页面点击显示购物车 跳转到购物车界面
			// 返回显示内容
			request.getSession().setAttribute("showWhat", "showCar");
			// 重定向
			response.sendRedirect("index.jsp");
		}
		if (action.equals("shopBox")) {
			// 在购物车界面点击显示商店页面 跳转到商店页面
			// 返回显示内容
			request.getSession().setAttribute("showWhat", "buy");
			// 重定向
			response.sendRedirect("index.jsp");
		}
		if (action.equals("docar")) {
			try {
				if (shopCarOptionServers.buy()) {
					request.getSession().setAttribute("showWhat", "shopCar");
					// 重定向
					request.getSession().setAttribute("alert", "alert('购买成功');");
					response.sendRedirect("index.jsp");
				} else {
					request.getSession().setAttribute("alert", "alert('操作失败，请重试');");
				}
			} catch (SQLException | NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
