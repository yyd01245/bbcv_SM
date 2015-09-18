var MSI = {};
//218.108.50.250:18080
//var _basePath = "http://localhost:8080/msi/";
/**
 * APP接入授权
 */
MSI.auth_code_req=function(app_name,licence,mobile_id,region_code,region_name){
	var res = null;
	$.ajax({
		type:"POST",url:_basePath+"auth_code_req.do",data:{
			sequence:sequence(),app_name:app_name,licence:licence,mobile_id:mobile_id,region_code:region_code,region_name:region_name
		}
		,async:false,success:function(data,textStatus){
			res = data;
		}
		,error:function(data,textStatus){
			res = null;
		}
	});
	return res;
}

MSI.user_access_req=function(username,passwd,app_name,licence,version){
	var res = null;
	$.ajax({
		type:"POST",url:_basePath+"user_access_req.do",data:{
			sequence:sequence(),appname:app_name,licence:licence,username:username,passwd:passwd,version:version
		}
		,async:false,success:function(data,textStatus){
			res = data;
		}
		,error:function(data,textStatus){
			res = null;
		}
	});
	return res;
}

MSI.user_register_req=function(username,passwd){
	var res = null;
	$.ajax({
		type:"POST",url:_basePath+"user_regist_req.do",data:{
			sequence:sequence(),username:username,passwd:passwd
		}
		,async:false,success:function(data,textStatus){
			res = data;
		}
		,error:function(data,textStatus){
			res = null;
		}
	});
	return res;
}

MSI.user_login_req=function(username,token){
	var res = null;
	$.ajax({
		type:"POST",url:_basePath+"user_login_req.do",data:{
			sequence:sequence(),username:username,token:token
		}
		,async:false,success:function(data,textStatus){
			res = data;
		}
		,error:function(data,textStatus){
			res = null;
		}
	});
	return res;
}

MSI.user_bind_req=function(username,token,vod_page,stream_id){
	var res = null;
	$.ajax({
		type:"POST",url:_basePath+"user_bind_req.do",data:{
			sequence:sequence(),username:username,token:token,vod_page:vod_page,stream_id:stream_id
		}
		,async:false,success:function(data,textStatus){
			res = data;
		}
		,error:function(data,textStatus){
			res = null;
		}
	});
	return res;
}

MSI.user_unbind_req=function(username,token){
	var res = null;
	$.ajax({
		type:"POST",url:_basePath+"user_unbind_req.do",data:{
			sequence:sequence(),username:username,token:token
		}
		,async:false,success:function(data,textStatus){
			res = data;
		}
		,error:function(data,textStatus){
			res = null;
		}
	});
	return res;
}


MSI.user_vodplay_req=function(username,token,url,vodname,posterurl){
	var res = null;
	$.ajax({
		type:"POST",url:_basePath+"user_vodplay_req.do",data:{
			sequence:sequence(),username:username,token:token,url:url,vodname:vodname,posterurl:posterurl
		}
		,async:false,success:function(data,textStatus){
			res = data;
		}
		,error:function(data,textStatus){
			res = null;
		}
	});
	return res;
}


MSI.user_sessionquery_req=function(username,token){
	var res = null;
	$.ajax({
		type:"POST",url:_basePath+"user_sessionquery_req.do",data:{
			sequence:sequence(),username:username,token:token
		}
		,async:false,success:function(data,textStatus){
			res = data;
		}
		,error:function(data,textStatus){
			res = null;
		}
	});
	return res;
}

MSI.user_choosetime_req=function(username,token,stream_id,begintime){
	var res = null;
	$.ajax({
		type:"POST",url:_basePath+"user_choosetime_req.do",data:{
			sequence:sequence(),username:username,token:token,stream_id:stream_id,begintime:begintime
		}
		,async:false,success:function(data,textStatus){
			res = data;
		}
		,error:function(data,textStatus){
			res = null;
		}
	});
	return res;
}

/**
 * STB登录
 */
MSI.stb_login_req=function(auth_code,mobile_id,stb_id,region_code){
	var res = null;
	$.ajax({
		type:"POST",url:_basePath+"stb_login_req.do",data:{
			sequence:sequence(),auth_code:auth_code,mobile_id:mobile_id,stb_id:stb_id,region_code:region_code
		}
		,async:false,success:function(data,textStatus){
			res = data;
		}
		,error:function(data,textStatus){
			res = null;
		}
	});
	return res;
}

/**
 * APP绑定STB
 */
MSI.bind_stb_req=function(auth_code,mobile_id,region_id,stb_ability,bind_type,bind_id){
	var res = null;
	$.ajax({
		type:"POST",url:_basePath+"bind_stb_req.do",data:{
			sequence:sequence(),auth_code:auth_code,mobile_id:mobile_id,region_id:region_id,
			stb_ability:stb_ability,bind_type:bind_type,bind_id:bind_id
		},async:false,success:function(data,textStatus){
			res = data;
		},error:function(data,textStatus){
			res = null;
		}
	});
	return res;
}

/**
 * 解绑STB
 */
MSI.unbind_stb_req=function(auth_code,mobile_id,unbind_type,unbind_id){
	var res = null;
	$.ajax({
		type:"POST",url:_basePath+"unbind_stb_req.do",data:{
			sequence:sequence(),auth_code:auth_code,mobile_id:mobile_id,unbind_type:unbind_type,unbind_id:unbind_id
		},async:false,success:function(data,textStatus){
			res = data;
		},error:function(data,textStatus){
			res = null;
		}
	});
	return res;
}

/**
 * STB绑定查询
 */
MSI.querybind_stb_req=function(auth_code,mobile_id){
	var res = null;
	$.ajax({
		type:"POST",url:_basePath+"querybind_stb_req.do",data:{
			sequence:sequence(),auth_code:auth_code,mobile_id:mobile_id
		}
		,async:false,success:function(data,textStatus){
			res = data;
		}
		,error:function(data,textStatus){
			res = null;
		}
	});
	return res;
}

/**
 * 更改绑定名称
 */
MSI.rename_stb_req=function(auth_code,mobile_id,stb_id,name){
	var res = null;
	$.ajax({
		type:"POST",url:_basePath+"rename_stb_req.do",data:{
			sequence:sequence(),auth_code:auth_code,mobile_id:mobile_id,stb_id:stb_id,name:name
		},async:false,success:function(data,textStatus){
			res = data;
		},error:function(data,textStatus){
			res = null;
		}
	});
	return res;
}

/**
 * 指令下发接口
 */
MSI.command_send_req=function(auth_code,mobile_id,stb_id,region_id,c_type,c_id){
	var res = null;
	$.ajax({
		type:"POST",url:_basePath+"command_send_req.do",data:{
			sequence:sequence(),auth_code:auth_code,mobile_id:mobile_id,region_id:region_id,stb_id:stb_id,c_type:c_type,c_id:c_id
		},async:false,success:function(data,textStatus){
			res = data;
		},error:function(data,textStatus){
			res = null;
		}
	});
	return res;
}

/**
 * 键值下发接口
 */
MSI.key_send_req=function(username,token,key_type,key_value){
	var res = null;
	$.ajax({
		type:"POST",url:_basePath+"key_send_req.do",data:{
			sequence:sequence(),username:username,token:token,key_type:key_type,key_value:key_value
		},async:false,success:function(data,textStatus){
			res = data;
		},error:function(data,textStatus){
			res = null;
		}
	});
	return res;
}

/**
 * STB退出接口
 */
MSI.stb_logout_req=function(auth_code,mobile_id,stb_id){
	var res = null;
	$.ajax({
		type:"POST",url:_basePath+"stb_logout_req.do",data:{
			sequence:sequence(),auth_code:auth_code,mobile_id:mobile_id,stb_id:stb_id
		},async:false,success:function(data,textStatus){
			res = data;
		},error:function(data,textStatus){
			res = null;
		}
	});
	return res;
}

/**
 * 生成二维码
 */
MSI.qr_req=function(stb_id,region_id,terminal_info,ca_id,dtv_id,terminal_model,netword_id,active_url,width,height){
	var res = null;
	$.ajax({
		type:"POST",url:_basePath+"qr_req.do",data:{
			sequence:sequence(),stb_id:stb_id,ca_id:ca_id,region_id:region_id,dtv_id:dtv_id,terminal_model:terminal_model,
			netword_id:netword_id,active_url:active_url,terminal_info:terminal_info,width:width,height:height
		},async:false,success:function(data,textStatus){
			res = data;
		},error:function(data,textStatus){
			res = null;
		}
	});
	return res;
}

/**
 * 二维码短码查询
 */
MSI.qr_query_req=function(auth_code,mobile_id,code){
	var res = null;
	$.ajax({
		type:"POST",url:_basePath+"qr_query_req.do",data:{
			sequence:sequence(),auth_code:auth_code,mobile_id:mobile_id,code:code
		},async:false,success:function(data,textStatus){
			res = data;
		},error:function(data,textStatus){
			res = null;
		}
	});
	return res;
}

/**
 * 获取区域信息
 */
MSI.region_req=function(auth_code,mobile_id,region_code,type){
	var data = {sequence:sequence(),auth_code:auth_code,mobile_id:mobile_id,region_code:region_code,type:type};
	var res = null;
	$.ajax({
		type:"POST",url:_basePath+"region_req.do",data:data,
		async:false,success:function(data,textStatus){
			res = data;
		},error:function(data,textStatus){
			res = null;
		}
	});
	return res;
}

function sequence() {
	var seed = (new Date().getTime()*9301+49297) % 233280;
	return Math.ceil(seed/(233280.0)*100000000)
};