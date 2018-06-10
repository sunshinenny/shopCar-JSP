package com.web;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class sendEmail
 */
@WebServlet("/sendEmail")
public class sendEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public sendEmail() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			request.setCharacterEncoding("gb2312");
			String from = request.getParameter("from");
			String to = request.getParameter("to");
			String subject = request.getParameter("subject");
			String content = request.getParameter("content");
			String password = request.getParameter("password");
			// 生成SMTP主机名称
			int n = from.indexOf('@');
			int m = from.length();
			String mailserver = "smtp." + from.substring(n + 1, m);
			// 建立邮件对话
			Properties pro = new Properties();
			pro.put("mail.smtp.host", mailserver);
			pro.put("mail.smtp.auth", "true");
			Session sess = Session.getInstance(pro);
			sess.setDebug(true);
			// 新建消息对象
			MimeMessage message = new MimeMessage((Session) sess);
			// 设置发件人
			InternetAddress from_mail = new InternetAddress(from);
			message.setFrom(from_mail);
			// 设置收件人
			InternetAddress to_mail = new InternetAddress(to);
			message.setRecipient(Message.RecipientType.TO, to_mail);
			// 设置邮件内容
			message.setSubject(subject);
			message.setText(content);
			message.setSentDate(new Date());
			// 发送邮件
			message.saveChanges();
			Transport transport = sess.getTransport("smtp");
			transport.connect(mailserver, from, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			request.setAttribute("sendEmailResult", "success");
			// 将发送成功的消息转发到新页面
			request.getRequestDispatcher("index.jsp").forward(request, response);
			response.sendRedirect("sendEmail.jsp");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("发送失败" + e.getMessage());
			e.printStackTrace();
			request.setAttribute("sendEmailResult", "flase");
			request.getRequestDispatcher("index.jsp").forward(request, response);
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
