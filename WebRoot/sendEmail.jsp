<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ page import="com.domain.userInfo" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>sendEmail</title>
</head>
<body>
	<form action="sendEmail" method="post">
		收件人:<input name="to" type="text" id="to" title="收件人" value="sunshinenny@icloud.com" size="60" readonly="readonly" /><br>
		发件人:<input name="from" type="text" id="from" title="发件人" size="60" value=<%=userInfo.getEmail() %> /><br>
		密码:<input name="password" type="password" id="password" title="发件人邮箱密码" size="60" value="Taoyuhao1997"/><br>
		主题:<input name="subject" type="text" id="subject" title="邮件主题" size="60" /><br>
		内容:<textarea name="content" cols="59" rows="7" class="反馈" id="content" title="邮件内容" ></textarea> <br>
		<input type="submit" value="发送" >
		<input type="reset" value="重置">
	</form>
	<c:if test="${sendEmailResult!=null }" var="result" scope="session">
		<c:choose>
			<c:when test="${sendEmailResult eq 'success' }">
				<script type="text/javascript">
					alert("发送成功");
				</script>
			</c:when>
			<c:when test="${sendEmailResult eq 'flase'} ">
				<script type="text/javascript">
					alert("发送成功");
				</script>
			</c:when>
		</c:choose>
	</c:if>
</body>
</html>