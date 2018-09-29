var token = null;
var host_url = null;
var community_id = null;

//先获取当前环境
function getUrl() {
	var prop_ = prompt("ljhext://get_request_host/getHost_url");
	getHost_url(prop_);
}
function getHost_url(redata) {
	var data = eval('(' + redata + ')');
	host_url = data.host_url;
	localStorage.csportalurl = host_url;
	localStorage.http_service1 = host_url+"/";
}

//先获取用户token
function getToken() {
//	window.location.href = 'ljh://user_token/getUserToken';
	var prop_ = prompt("ljh://user_token/getUserToken");
	getUserToken(prop_);
}
function getUserToken(redata) {
	var data = eval('(' + redata + ')');
	token = data.user_token;
	localStorage.token = data.user_token;
}
//获取小区id
function getSelectArea() {
	var prop_ = prompt("ljhext://current_community_id/getSelectAreaCb");
	getSelectAreaCb(prop_);
}

function getSelectAreaCb(redata) {
	var data = eval('(' + redata + ')');
	community_id = data.community_id; //调用native接口，必须在app应用环境下调用才能有数据
}

//var http_service = "http://csportal.cm-dev.cn" ; //服务器地址
//var http_service = "http://10.16.69.86:8080/core/" ; //服务器地址





//var image_service = ""; //图片服务地址
var image_service = "";
//var image_service="http://image.cmlejia.com/show/";

function GetRequest() {
	var url = location.search; //获取url中"?"符及其后边的字串
	var theRequest = new Object();
	if(url.indexOf("?") != -1) {
		var str = url.substr(1);
		strs = str.split("&");
		for(var i = 0; i < strs.length; i++) {
			theRequest[strs[i].split("=")[0]] = (strs[i].split("=")[1]);
		}
	}
	return theRequest;
}

function GetUrl() {
	var url = location.search; //获取url中"?"符后的字串
	return url;
}

function getHeaders() {
	var token = localStorage.token;
	return {
		//动态获取token值
		"authorization": token
	}
}
//有状态值返回对应得状态
function incidentStatus(incident_status) {
	incident_status = incident_status.toUpperCase();
	var pairStatus = null;
	if(incident_status == "NEW") {
		pairStatus = "新建"
	} else if(incident_status == "NEW_ASSIGN") {
		pairStatus = "已派工"
	} else if(incident_status == "WAIT_REPAIR") {
		pairStatus = "待维修"
	} else if(incident_status == "PENDING") {
		pairStatus = "处理中"
	} else if(incident_status == "DONE") {
		pairStatus = "维修完成"
	} else if(incident_status == "DONE_EX") {
		pairStatus = "完成"
	} else if(incident_status == "WAIT_ASSIGN") {
		pairStatus = "待分派"
	} else if(incident_status == "CANCEL") {
		pairStatus = "已取消"
	}
	return pairStatus;
}



//请求数据后状态403时提示操作超
function closePpage(code) {

	//window.location = "ljh://finish_page";
	if(code == "403") {
		//alert("token："+localStorage.token);
		if(mui != null) {
			mui.toast("操作超时！")
		}
		setTimeout(function() {
			window.location = "ljh://finish_page";
		}, 3000);
	}
}

//调用原生的提示信息
function totast(msg) {
	var params = {
		"message": msg
	}
	var sds = urlencode(JSON.stringify(params));
	location.href = "ljh://show_toast?" + sds
}

function urlencode(str) {
	//str = (str + '').toString();   
	//return encodeURIComponent(str).replace(/!/g, '%21').replace(/'/g, '%27').replace(/\(/g, '%28').  
	//replace(/\)/g, '%29').replace(/\*/g, '%2A').replace(/%20/g, '+');  
	
	return encodeURI(str)
}


