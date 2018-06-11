<%@page import="com.domain.userInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登陆</title>
</head>

<body>
	<jsp:include page="top.jsp"></jsp:include>
	<div class="col-md-8">
		<form action="checkUserInfoServers?act=zc" method="post" role="form"
			style="width: 600px;">
			<c:if test="${dlerror==null }" var="error1" scope="session">
				<div class="form-group">
					<label for="exampleInputEmail1">Your name</label> <input
						type="text" class="form-control" id="username" name="username"
						placeholder="账号"> <label for="exampleInputPassword1">Password</label>
					<input type="password" class="form-control" id="password"
						name="password" placeholder="密码">
				</div>
			</c:if>
			<c:if test="${dlerror!=null }" var="error1" scope="session">
				<div class="form-group has-error">
					<label class="control-label" for="inputError">Your name</label> <input
						type="text" class="form-control" id="username" name="username"
						placeholder="内容填写错误，请重试">
				</div>

				<div class="form-group">
					<label for="exampleInputPassword1">Password</label> <input
						type="password" class="form-control" id="password" name="password"
						placeholder="密码">
				</div>
			</c:if>
			<div>
				<td><input type="submit" value="登陆"
					class="btn btn-embossed btn-primary" /></td>
				<td><input type="reset" value="取消" style="background: gray;"
					class="btn btn-default btn-primary" /></td>
			</div>
		</form>
	</div>
</body>
</html>