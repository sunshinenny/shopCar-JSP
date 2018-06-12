package com.servers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.userInfoDao;;

@WebServlet("/checkUserInfoServers")
public class checkUserInfoServers extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public checkUserInfoServers() {
	}

	/**
	 * Ajax异步调用数据库，进行用户名查重操作 注册时调用post请求 使用ajax时使用get请求
	 * 最先设计servlet时出现失误，没有在一种请求中使用if区分开，以后注意！
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 设置编码
		request.setCharacterEncoding("utf-8");
		// 获取act，用以判断下一步操作
		String act = (String) request.getParameter("act");
		if (act.equals("zc")) {
			// 如果act为zc，则连接数据库进行相关操作
			// 获取表单项
			String formUsername = request.getParameter("username");
			String formPassword = request.getParameter("password");
			// 链接数据库，进行信息匹对
			try {
				if (userInfoDao.check(formUsername, formPassword)) {
					// 如果返回值为true，即验证通过
					// 设置显示内容-登陆成功
					// 将登陆成功的用户名压入到session中，变量名为LoginUserName
					request.getSession().setAttribute("loginUserName", formUsername);
					request.getSession().setAttribute("dlerror", null);
					// 重定向
					response.sendRedirect("index.jsp");
				} else {
					// 登陆错误
					request.getSession().setAttribute("dlerror", "nameError");
					// 重定向
					response.sendRedirect("login.jsp");
				}
			} catch (SQLException | NamingException e) {

				System.out.println("数据库连接失败，登陆失败");
				e.printStackTrace();
			}
		} else if (act.equals("cheakDuplicateName")) { // 通过url传递，从zc.jsp中获取username
			// 如果act为cheakDuplicateName，则进行数据库查询操作，查询有没有相同的username
			request.setCharacterEncoding("utf-8");
			String userNameString = (String) request.getParameter("username");
			// 定义输出对象，用于对zj.jsp输出提示
			PrintWriter outPrintWriter = response.getWriter();
			try {
				/**
				 * 调用cheakDuplicateName方法，对用户名进行判断「返回值为false
				 * 说明不存在该用户，为true说明存在该用户」
				 */
				if (userInfoDao.checkDuplicateName(userNameString)) {
					response.setCharacterEncoding("UFT-8");
					outPrintWriter.print("<span style='color:red;'>Already has the user,please try again!</span>");
				} else {
					outPrintWriter.print("<span style='color:green;'>Database not hava the user.</span>");
				}
			} catch (SQLException | NamingException e) {
				e.printStackTrace();
			}
		}

	}
}
