package com.servers;

import java.io.IOException;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.userInfoDao;
import com.domain.userInfo;

@WebServlet(urlPatterns = { "/insertUserInfoServers" }, name = "insertUserInfoServers")
public class insertUserInfoServers extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public insertUserInfoServers() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 定义编码格式
		request.setCharacterEncoding("utf-8");
		// 获取表单项
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String sex = request.getParameter("sex");
		String email = request.getParameter("email");
		System.out.println(username + "\n" + password + "\n" + sex + "\n");
		request.setAttribute("test", username);
		// 简单的对数据判断长度「用户名最少三位，密码最少六位」
		// 如果符合要求，将所有个人信息压入user中
		if (username.length() >= 3 && password.length() >= 6) {
			// 新建userInfo对象，临时存储，再将userInfo对象存入数据库中
			userInfo temp = new userInfo();
			temp.setUsername(username);
			temp.setPassword(password);
			temp.setSex(sex);
			temp.setEmail(email);
			// 将对象存入到数据库中
			try {
				// 使用insertUserInfo的构造函数快速链接数据库，并且添加相应信息
				if (userInfoDao.add(temp)) {
					// 设置错误标识符，用于优化用户体验「如果注册时出错，就提示信息错误等」
					// 注册成功，重置错误标识符，以便下一次注册和登陆
					request.getSession().setAttribute("zcerror", null);
					request.getSession().setAttribute("dlerror", null);
				} else {
					// 否则返回“注册错误”，zcerror=nameError
					request.getSession().setAttribute("zcerror", "nameError");
					request.getSession().setAttribute("dlerror", null);
				}
				// 重定向到login页面
				response.sendRedirect("login.jsp");
			} catch (SQLException | NamingException e) {
				System.out.println("添加用户时，数据库连接出错，请重试。");
				e.printStackTrace();
			}
		}
		// 如果信息不符合要求，则跳回注册页面,并作提示
		else if (username.length() < 3) {
			// 返回error信息，在注册页通过判断进一步显示信息
			request.getSession().setAttribute("zcerror", "nameError");
			response.sendRedirect("zc.jsp");
		} else if (password.length() < 6) {
			request.getSession().setAttribute("zcerror", "pwdError");
			response.sendRedirect("zc.jsp");
		}
	}
	/**
	 * 在表单中，使用的是post传递
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
