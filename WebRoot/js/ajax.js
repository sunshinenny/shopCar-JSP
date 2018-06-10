/**
 * 
 */

/**
 * 
 */

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

//初始化XmlHttpRequest对象
var httpReq = createXmlHttpRequest();
//处理返回信息的函数
function processResponse() {
	if (httpReq.readyState == 4) { //4代表请求完成，信息返回
		if (httpReq.status == 200) { //信息成功返回，处理信息
		}
	}

}

var url = "${path}/ShopServlet";
//建立对服务器的调用
httpReq.open("get", url, true);
//状态改变的事件触发器,客户端的状态改变会触发readystatechange事件，调用相应的事件处理函数
httpReq.onreadystatechange = processResponse;
//发送数据
httpReq.send(null);