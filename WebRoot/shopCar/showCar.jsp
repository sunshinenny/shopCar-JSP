<%@page import="com.servers.shopCarOptionServers"%>
<%@page import="com.domain.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.DecimalFormat"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%
	ArrayList<GoodsList> buyList =shopCarOptionServers.showCar((String)session.getAttribute("loginUserName"));
%>

<html>
<head lang="en">
<meta charset="UTF-8">
<body>
	<%!float total = 0;%>
	<%
		DecimalFormat df = new DecimalFormat("0.0");
		if (buyList == null || buyList.size() == 0) {
			total = 0;
	%>
	<div align="center">
		<h2>没有商品可显示!</h2>
	</div>
	<%
		} else {
			total = 0;
			for (int i = 0; i < buyList.size(); i++) {
				GoodsList single = (GoodsList) buyList.get(i);
				total = single.getPrice() * single.getNum() + total;
				String imgUrl = single.getName() + ".jpg";
	%>
	<div class="col-sm-6 col-md-4" style="width: 20%;">
		<div class="thumbnail">
			<img src="img/<%=imgUrl%>" width="180" height="180">
			<div class="caption">

				<h4>
					<span style="color: red;">￥<%=single.getPrice()%><br></span> <span><%=single.getNum()%>个
					</span>
				</h4>

				<p><%=single.getName()%></p>
				<a href="doCar?action=remove&id=-1&name=<%=single.getName()%>"
					class="button orange addcar">从购物车移除</a>
			</div>
		</div>
	</div>
	<%
		}
		}
	%>
	<div class="m-sidebar">
		<div class="cart">
			<i id="end"></i> <span><a href="doCar?action=shopBox&id=-1">商城库存</a></span>
			<i id="end"></i> <span>总价<br><%=df.format(total)%></span> <i id="end"></i>
			<br>
			<c:if test="${loginUserName!=null}" var="cheackLoginResult" scope="session">
					<span><a href="doCar?action=docar&id=-1"><span style="color:red;">购买</span></a></span>
			</c:if>
			<c:if test="${loginUserName==null or loginUserName=='noLogin'}" var="cheackLoginResult" scope="session">
					<span><a href="login.jsp?act=loginThanBuy"><span style="color:red;">登陆后购买</span></a></span>
					
			</c:if>
			<br>
			<span><a href="doCar?action=clear&id=-1"><span style="color: #FFFFFF;">清除购物车</span></a></span>
		</div>
	</div>
	<div id="msg">移除成功！</div>
</body>
</html>