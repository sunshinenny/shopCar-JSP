<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>注册</title>
<link href="flat/css/vendor/bootstrap/css/bootstrap.min.css"
	rel="stylesheet">
<style type="text/css">
@import url("flat/css/flat-ui.min.css");

body {
	padding-bottom: 200px;
	size: 200px;
}
</style>
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
	function checkDuplicateNameAjax() {

		//初始化XmlHttpRequest对象
		var httpReq = createXmlHttpRequest();
		//处理返回信息的函数
		function processResponse() {
			if (httpReq.readyState == 4) { //4代表请求完成，信息返回
				if (httpReq.status == 200) { //信息成功返回，处理信息
					document.getElementById("duplicateNameError").innerHTML = httpReq.responseText;
				}
			}

		}
		var username = document.getElementById("username").value;
		var url = "checkUserInfoServers?act=cheakDuplicateName&username=" + username;
		//建立对服务器的调用,第三个变量缺省值为true-同步传输，false-异步传输
		httpReq.open("POST", url, false);
		//状态改变的事件触发器,客户端的状态改变会触发readystatechange事件，调用相应的事件处理函数
		//processResponse为上方定义的函数
		httpReq.onreadystatechange = processResponse;
		//发送数据，变量为“指定发送的数据”，缺省为null
		httpReq.send(null);
	}
</script>
</head>
<body>
	<!-- EL标签快捷调用的是request中的变量名，如果request中没有变量，就会报错 -->
	<jsp:include page="top.jsp"></jsp:include>
	<!-- 通过判断zcerror（注册错误）确定显示效果 -->
	<div class="col-md-8">
		<form action="insertUserInfoServers?act=zc" method="post" role="form"
			style="width: 600px;">
			<c:if test="${empty zcerror }" var="result" scope="session">
				<div class="form-group">
					<label for="exampleInputEmail1">Your name</label> <input
						type="text" class="form-control" id="username" name="username"
						placeholder="至少3个字符" onblur="checkDuplicateNameAjax()" required>
					<div id="duplicateNameError"></div>
				</div>
				<div class="form-group">
					<label for="exampleInputPassword1">Password</label> <input
						type="password" class="form-control" id="password" name="password"
						placeholder="至少6位" required>
				</div>
			</c:if>
			<c:if test="${not empty zcerror }" var="result" scope="session">
				<c:choose>
					<c:when test="${zcerror=='nameError' }">
						<div class="form-group has-error">
							<label class="control-label" for="inputError">Your name</label> <input
								type="text" class="form-control" id="username" name="username"
								placeholder="内容填写错误，请重试" onblur="checkDuplicateNameAjax()"
								required>
							<div id="duplicateNameError"></div>
						</div>
						<div class="form-group">
							<label for="exampleInputPassword1">Password</label> <input
								type="password" class="form-control" id="password"
								name="password" placeholder="至少6位" required>
						</div>
					</c:when>
					<c:when test="${zcerror=='pwdError' }">
						<div class="form-group">
							<label for="exampleInputEmail1">Your name</label> <input
								type="text" class="form-control" id="username" name="username"
								placeholder="至少3个字符" onblur="checkDuplicateNameAjax()" required>
							<div id="duplicateNameError"></div>
							<div class="form-group has-error">
								<label class="control-label" for="inputError">password</label> <input
									type="password" class="form-control" id="password"
									name="password" placeholder="内容填写错误，请重试" required>
							</div>
						</div>
					</c:when>
				</c:choose>

			</c:if>
			<div class="form-group">
				<label for="exampleInputEmail1">Your email</label> <input
					type="email" name="email" palceholder="邮箱账号" class="form-control"
					required />
			</div>

			<div class="form-group">
				<label class="radio" for="radio4a"> <input type="radio"
					name="sex" data-toggle="radio" value="Boy" id="radio4a" required=""
					checked="" class="custom-radio"><span class="icons"><span
						class="icon-unchecked"></span><span class="icon-checked"></span></span>
					Boy
				</label> <label class="radio" for="radio4b"> <input type="radio"
					name="sex" data-toggle="radio" value="Girl" id="radio4b"
					required="" class="custom-radio"><span class="icons"><span
						class="icon-unchecked"></span><span class="icon-checked"></span></span>
					Girl
				</label>
			</div>
			<div>
				<td><input type="submit" value="注册"
					class="btn btn-embossed btn-primary" /></td>
				<td><input type="reset" value="取消" style="background: gray;"
					class="btn btn-default btn-primary" /></td>
			</div>
		</form>
	</div>
</body>
</html>