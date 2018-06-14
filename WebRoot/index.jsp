<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商城</title>
<style type="text/css">
@import url("flat/css/flat-ui.min.css");

@import url("flat/css/vendor/bootstrap/css/bootstrap.min.css");

@import url("css/flat-ui.css");

@import url("css/fly.css");
</style>
<script src="js/jquery-3.2.1.min.js"></script>
<script src="js/jquery.fly.min.js"></script>
<script src="js/fly.js"></script>
<script src="js/bootstrap.min.js"></script>
</head>
<body>
	<!-- 将top作为框架导入 -->
	<jsp:include page="top.jsp"></jsp:include>
	<!-- 一般默认都是显示indexShopCar的界面 如果用户点击‘显示购物车’ 就显示shopcar -->
	<!-- 先判断showWhat的值，如果为null 显示indexShopCar 如果为buy 显示indexShopCar 如果为 showcar 显示showcar -->
	<c:if test="${showWhat!=null }" var="result" scope="session">
		<c:choose>
			<c:when test="${showWhat eq 'buy' }">
				<jsp:include page="shopCar/indexShopCar.jsp"></jsp:include></c:when>
			<c:when test="${showWhat eq 'showCar' }">
				<jsp:include page="shopCar/showCar.jsp"></jsp:include></c:when>
			<c:otherwise>
				<jsp:include page="shopCar/indexShopCar.jsp"></jsp:include></c:otherwise>
		</c:choose>
	</c:if>
	<c:if test="${showWhat==null }" var="result" scope="session">
		<jsp:include page="shopCar/indexShopCar.jsp"></jsp:include>
	</c:if>
	<c:if test="${sessionScope.alert!=null }">
		<script type="text/javascript">
			${sessionScope.alert }
		</script>
		<%
			session.removeAttribute("alert");
		%>
	</c:if>
</body>
</html>