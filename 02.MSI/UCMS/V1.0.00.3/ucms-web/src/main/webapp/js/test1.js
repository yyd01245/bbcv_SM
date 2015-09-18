//$(function(){
//	test_user_regist_req();
//});
function test_user_regist_req(){
	$("#user_register_req_res").val("");
	var username = $("#username").val();
	var passwd = $("#passwd").val();
	var res = MSI.user_register_req(username,passwd);
	$("#ret_code").val(res.ret_code);
	$("#user_register_req_res").val(JSON.stringify(res));
}

function test_user_access_req(){
	$("#user_access_req_res").val("");
	var app_name = $("#app_name").val();
	var licence = $("#licence").val();
	var version = $("#version").val();
	var username = $("#username").val();
	var passwd = $("#passwd").val();
	var res = MSI.user_access_req(username,passwd,app_name,licence,version);
	$("#token").val(res.token);
	$("#user_access_req_res").val(JSON.stringify(res));
}

function test_user_login_req(){
	$("#user_access_req_res").val("");
	var username = $("#username").val();
	var token = $("#token").val();
	var res = MSI.user_login_req(username,token);
	$("#token").val(res.new_token);
	$("#new_token").val(res.new_token);
	$("#user_login_req_res").val(JSON.stringify(res));
}

function test_user_bind_req(){
	$("#user_bind_req_res").val("");
	var username = $("#username").val();
	var token = $("#token").val();
	var vod_page = $("#vod_page").val();
	var stream_id = $("#stream_id").val();
	var res = MSI.user_bind_req(username,token,vod_page,stream_id);
	$("#token").val(res.new_token);
	$("#new_token").val(res.new_token);
	$("#user_bind_req_res").val(JSON.stringify(res));
}

function test_user_unbind_req(){
	$("#user_unbind_req_res").val("");
	var username = $("#username").val();
	var token = $("#token").val();
	var res = MSI.user_unbind_req(username,token);
	$("#token").val(res.new_token);
	$("#new_token").val(res.new_token);
	$("#user_unbind_req_res").val(JSON.stringify(res));
}

function test_user_vodplay_req(){
	$("#user_vodplay_req_res").val("");
	var username = $("#username").val();
	var token = $("#token").val();
	var url = $("#rtsp_url").val();
	var vodname = $("#vodname").val();
	var posterurl = $("#posterurl").val();
	var res = MSI.user_vodplay_req(username,token,url,vodname,posterurl);
	$("#token").val(res.new_token);
	$("#new_token").val(res.new_token);
	$("#user_vodplay_req_res").val(JSON.stringify(res));
}

function test_user_sessionquery_req(){
	$("#user_sessionquery_req_res").val("");
	var username = $("#username").val();
	var token = $("#token").val();
	var res = MSI.user_sessionquery_req(username,token);
	$("#user_sessionquery_req_res").val(JSON.stringify(res));
}

function test_user_choosetime_req(){
	$("#user_choosetime_req_res").val("");
	var username = $("#username").val();
	var token = $("#token").val();
	var stream_id = $("#stream_id").val();
	var begintime = $("#begintime").val();
	var res = MSI.user_choosetime_req(username,token,stream_id,begintime);
	$("#user_choosetime_req_res").val(JSON.stringify(res));
}

function test_querybind_stb_req(){
	$("#querybind_stb_req_res").val("");
	var auth_code = $("#auth_code").val();
	var mobile_id = $("#mobile_id").val();
	var res = MSI.querybind_stb_req(auth_code,mobile_id);
	$("#querybind_stb_req_res").val(JSON.stringify(res));
}

/**
 * STB登录
 */
function test_stb_login_req(){
	$("#stb_login_req_res").val("");
	var auth_code = $("#auth_code").val();
	var mobile_id = $("#mobile_id").val();
	var region_code = $("#region_code").val();
	var stb_id = $("#login_stb_id").val();
	var res = MSI.stb_login_req(auth_code,mobile_id,stb_id,region_code);
	$("#logout_stb_id").val(stb_id);
	$("#stb_login_req_res").val(JSON.stringify(res));
}

/**
 * APP绑定STB
 */
function test_bind_stb_req(){
	$("#bind_stb_req_res").val("");
	var auth_code = $("#auth_code").val();
	var mobile_id = $("#mobile_id").val();
	var region_code = $("#region_code").val();
	var stb_ability = $("#bind_bind_stb_ability").val();
	var bind_type = $("#bind_bind_type").val();
	var bind_id = $("#bind_bind_id").val();
	var res = MSI.bind_stb_req(auth_code,mobile_id,region_code,stb_ability,bind_type,bind_id);
	$("#bind_stb_req_res").val(JSON.stringify(res));
}



/**
 * AP解绑STB
 */
function test_unbind_stb_req(){
	$("#unbind_stb_req_res").val("");
	var auth_code = $("#auth_code").val();
	var mobile_id = $("#mobile_id").val();
	var unbind_type = $("#unbind_unbind_type").val();
	var unbind_id = $("#unbind_bind_id").val();
	var res = MSI.unbind_stb_req(auth_code,mobile_id,unbind_type,unbind_id);
	$("#unbind_stb_req_res").val(JSON.stringify(res));
}

/**
 * 更改绑定名称
 */
function test_rename_stb_req(){
	$("#rename_stb_req_res").val("");
	var auth_code = $("#auth_code").val();
	var mobile_id = $("#mobile_id").val();
	var stb_id = $("#rename_stb_id").val();
	var name = $("#rename_name").val();
	var res = MSI.rename_stb_req(auth_code,mobile_id,stb_id,name);
	$("#rename_stb_req_res").val(JSON.stringify(res));
}

/**
 * 指令下发接口
 */
function test_command_send_req(){
	$("#command_send_req_res").val("");
	var auth_code = $("#auth_code").val();
	var mobile_id = $("#mobile_id").val();
	var region_code = $("#region_code").val();
	var stb_id = $("#command_stb_id").val();
	var c_type = $("#command_c_type").val();
	var c_id = $("#command_c_id").val();
	var res = MSI.command_send_req(auth_code,mobile_id,stb_id,region_code,c_type,c_id);
	$("#command_send_req_res").val(JSON.stringify(res));
}

function test_command_send_req2(c_type,c_id){
	$("#command_send_req_res").val("");
	var auth_code = $("#auth_code").val();
	var mobile_id = $("#mobile_id").val();
	var region_code = $("#region_code").val();
	var stb_id = $("#command_stb_id").val();
	$("#command_c_type").val(c_type);
	$("#command_c_id").val(c_id);
	var res = MSI.command_send_req(auth_code,mobile_id,stb_id,region_code,c_type,c_id);
	$("#command_send_req_res").val(JSON.stringify(res));
}

/**
 * 键值下发接口
 */
function test_key_send_req(key_value){
	$("#key_send_req_res").val("");
	var username = $("#username").val();
	var token = $("#token").val();
	var key_type = $("#key_key_type").val();
	var res = MSI.key_send_req(username,token,key_type,key_value);
	$("#key_send_req_res").val(JSON.stringify(res));
}

/**
 * STB退出接口
 */
function test_stb_logout_req(){
	$("#stb_logout_req_res").val("");
	var auth_code = $("#auth_code").val();
	var mobile_id = $("#mobile_id").val();
	var stb_id = $("#logout_stb_id").val();
	var res = MSI.stb_logout_req(auth_code,mobile_id,stb_id);
	$("#stb_logout_req_res").val(JSON.stringify(res));
}

/**
 * 生成二维码
 */
function test_qr_req(){
	$("#qr_req_res").val("");
	var stb_id = $("#qr_stb_id").val();
	var region_id = $("#qr_region_id").val();
	var stb_ability="0:0:0:0:0:0:0:0:0:0:0:"+$("#qr_stb_ability").val();
	var res = MSI.qr_req(stb_id,region_id,stb_ability);
	$("#qr_req_res").val(JSON.stringify(res));
}

/**
 * 二维码短码查询
 */
function test_qr_query_req(){
	$("#qr_query_req_res").val("");
	var auth_code = $("#auth_code").val();
	var mobile_id = $("#mobile_id").val();
	var code = $("#qr_query_code").val();
	var res = MSI.qr_query_req(auth_code,mobile_id,code);
	$("#qr_query_req_res").val(JSON.stringify(res));
}

function test_region_req(region_code,type){
	var auth_code = $("#auth_code").val();
	var mobile_id = $("#mobile_id").val();
	var data = MSI.region_req(auth_code,mobile_id,region_code,type);
	var p = null;
	if(type == 0) {
		p = $("#region_province");
	} else if(type == 1) {
		p = $("#region_city");
	} else if(type == 2) {
		p = $("#region_area");
	}
	
	if(p) {
		p.empty();
		if(data) {
			var d = data.message;
			$.each(d,function(i,v){
				p.append("<option value='"+v.region_code+"'>"+v.region_name+"</option>");
			});
			p.trigger("change");
		}
	}
}

function test_channel_common(){
	
}

function test_channel_notice_over(){
	
}

function test_channel_screennotice(){
	
}
