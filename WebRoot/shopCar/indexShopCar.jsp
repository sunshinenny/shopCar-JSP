<%@page import="com.dao.*"%>
<%@page import="com.domain.GoodsList"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	ArrayList<GoodsList> goodSingle = new getGoodsInfoDao().goodsInfo();
%>
<%
	session.setAttribute("goodSingle", goodSingle);
%>

<html>
<head lang="en">
<meta charset="UTF-8">
<title>飞入购物车</title>
<script type="text/javascript">
	function createXmlHttpRequest() {
		var httpReq;
		try { //Firefox, Opera 8.0+, Safari…
			httpReq = new XMLHttpRequest();
		} catch (e) {
			try { //Internet Explorer
				httpReq = new ActiveXObject("Msxml2.XMLHTTP");
			} catch (e) {
				try {
					httpReq = new ActiveXObject("Microsoft.XMLHTTP");
				} catch (e) {}
			}
		}
		return httpReq;
	}
	function addGoodsToShopCar(i, username) {

		//初始化XmlHttpRequest对象
		var httpReq = createXmlHttpRequest();
		//处理返回信息的函数
		function processResponse() {
			if (httpReq.readyState == 4) { //4代表请求完成，信息返回
				if (httpReq.status == 200) { //信息成功返回，处理信息
					// 从java类中获取返回字符串
					var msg = httpReq.responseText;
					//对返回对字符串进行判断
					if (msg == "alert") {
						/* window.alert("购买数超出库存数，添加失败"); */
						document.getElementById("msg").innerHTML = "已超出库存数，添加失败";
					} else {
						document.getElementById("msg").innerHTML = "已成功加入购物车！";
					}
				}
			}

		}
		var id = i;
		console.log("the goodsId is " + id);
		// 此处URL多传递了一个参数，可能导致别的调用docar的地方出现问题，警告⚠
		console.log(username);
		var url = "doCar?action=buy&id=" + id + "&username=" + username;
		//建立对服务器的调用,第三个变量缺省值为true-同步传输，false-异步传输
		httpReq.open("POST", url, true);
		//状态改变的事件触发器,客户端的状态改变会触发readystatechange事件，调用相应的事件处理函数
		//processResponse为上方定义的函数
		httpReq.onreadystatechange = processResponse;
		//发送数据，变量为“指定发送的数据”，缺省为null
		httpReq.send(null);
	}
</script>
</head>
<body>
	<%
		if (goodSingle == null || goodSingle.size() == 0) {
	%>
	<div class="container">
		<h2>没有商品可显示!</h2>
	</div>
	<%
		} else {
			for (int i = 0; i < goodSingle.size(); i++) {
				GoodsList single = (GoodsList) goodSingle.get(i);
				String imgUrl = single.getName() + ".jpg";
	%>
	<div class="col-sm-6 col-md-4" style="width: 20%;" align="center">
		<div class="thumbnail col-sm-12">
			<img src="img/<%=imgUrl%>" width="180" height="180">
			<h4>
				<span>￥<%=single.getPrice()%></span> <span><font
					style="font-size: 16px;">&nbsp;&nbsp;&nbsp;库存:<%=single.getNum()%></font></span>
			</h4>

			<p><%=single.getName()%></p>
			<c:if test="${loginUserName!=null }" var="userNameFlag"
				scope="session">
				<a class="button orange addcar"
					onclick="addGoodsToShopCar(<%=i%>,'<%=session.getAttribute("loginUserName") %>')">加入购物车</a>
			</c:if>
			<c:if test="${loginUserName==null }" var="userNameFlag"
				scope="session">
				<a class="button orange addcar"
					onclick="addGoodsToShopCar(<%=i%>,'noLogin')">加入购物车</a>
			</c:if>
		</div>
	</div>
	<%
		}
		}
	%>
	</div>
	<div style="display: none; z-index: 2;" class="email" align="center">
		<jsp:include page="/sendEmail.jsp"></jsp:include>
	</div>
	<div class="m-sidebar">
		<div class="cart">
			<i id="end"></i> <span><a href="doCar?action=showCar&id=-1">购物车</a></span>
			<i id="end"></i> <span class="emailToMe"><a>联系我们</a></span> <i
				id="end"></i>
			<script type="text/javascript">
				$(".emailToMe").click(function() {
					$(".email").toggle(2000);
				})
			</script>
		</div>
		<div id="msg">已成功加入购物车！</div>
		<!-- //内嵌一个发送邮件的页面 -->
</body>
</html>