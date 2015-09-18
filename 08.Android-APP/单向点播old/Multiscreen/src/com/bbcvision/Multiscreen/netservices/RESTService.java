package com.bbcvision.Multiscreen.netservices;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.bbcvision.Multiscreen.bean.ChooseTimeInfo;
import com.bbcvision.Multiscreen.bean.KeySendInfo;
import com.bbcvision.Multiscreen.bean.SessionQueryInfo;
import com.bbcvision.Multiscreen.bean.UnBindInfo;
import com.bbcvision.Multiscreen.bean.UserBingInfo;
import com.bbcvision.Multiscreen.bean.UserLoginInfo;
import com.bbcvision.Multiscreen.bean.VodPlayInfo;
import com.bbcvision.Multiscreen.configs.NetConfig;
import com.bbcvision.Multiscreen.manager.BaseApplication;
import com.bbcvision.Multiscreen.tools.CommonTool;
import com.bbcvision.Multiscreen.tools.PackageUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;


public class RESTService {
	public int code;
	public Map<String, String> results;

	public void regUesr(String regnumber, String regpsw,
			final CallbackregUesr callbackregUesr) {

		RequestParams params = new RequestParams();
		params.addBodyParameter("username", regnumber);
		params.addBodyParameter("passwd", regpsw);

		String path = "http://" + NetConfig.NC_HOST + ":" + NetConfig.NC_POST
				+ NetConfig.NC_REG_URL;

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, path, params,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
						// UIUtils.showToastSafe("reging");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (isUploading) {
						} else {
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {

						int ret_code = JSONService.regJSON(responseInfo.result);
						// code = ret_code;
						callbackregUesr.callbackRegUesr(ret_code);
						// Log.i("responseInfo", responseInfo.result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.i("errormsg", msg);
						callbackregUesr.callbackRegUesr(-1);
					}
				});
	}

	public void accessUesr(String number, String psw,
			final CallbackaccessUesr callbackaccessUesr) {
		String cmd = "user_access_req";
		String appname = "KYSX";
		String licence = "KYSX1234";
		String version = PackageUtils.getVersionCode() + "";

		RequestParams params = new RequestParams();
		params.addBodyParameter("username", number);
		params.addBodyParameter("passwd", psw);
		params.addBodyParameter("cmd", cmd);
		params.addBodyParameter("appname", appname);
		params.addBodyParameter("licence", licence);
		params.addBodyParameter("version", version);

		String path = "http://" + NetConfig.NC_HOST + ":" + NetConfig.NC_POST
				+ NetConfig.NC_ACCESS_URL;

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, path, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						// UIUtils.showToastSafe("reging");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (isUploading) {
						} else {
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {

						results = JSONService.accessJSON(responseInfo.result);
						callbackaccessUesr.callbackAccessUesr(results);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.i("errormsg", msg);
						results = new HashMap<String, String>();
						results.put("return_code", "-1");
						callbackaccessUesr.callbackAccessUesr(results);
					}
				});
	}

	public void loginUesr(Map<String, String> results2, final String number,
			final CallbackloginUesr callbackloginUesr) {
		final String service_url = results2.get("service_url");
		String token = results2.get("token");
		String sequence = CommonTool.getMobileId(BaseApplication
				.getApplication());

		RequestParams params = new RequestParams();
		params.addBodyParameter("username", number);
		params.addBodyParameter("token", token);
		params.addBodyParameter("sequence", sequence);

		// String path = "http://" + NetConfig.NC_HOST +
		// ":"+NetConfig.NC_POST+NetConfig.NC_LOGIN_URL;
		String path = "http://" + service_url + NetConfig.NC_LOGIN_URL;

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, path, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						// UIUtils.showToastSafe("reging");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (isUploading) {
						} else {
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {

						UserLoginInfo userLoginInfo = JSONService
								.loginSON(responseInfo.result);
						
						userLoginInfo.setService_url(service_url);
						userLoginInfo.setUsername(number);
						
						Log.i("userLoginInfo", userLoginInfo.getService_url()+"---"+userLoginInfo.getUrl()+"---"+userLoginInfo.getNew_token());
						callbackloginUesr.callbackLoginUesr(userLoginInfo);

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.i("loginErrormsg", msg);
						UserLoginInfo userLoginInfo = new UserLoginInfo();
						userLoginInfo.setReturn_code(-1 + "");
						callbackloginUesr.callbackLoginUesr(userLoginInfo);
					}
				});
	}

	public void bindUesr(UserLoginInfo userLoginInfo, String stream_id,String url,
			final CallbackbindUesr callbackbindUesr) {
		
		String vod_page = "http://vod.html";
		
		RequestParams params = new RequestParams();
		params.addBodyParameter("username", userLoginInfo.getUsername());
		params.addBodyParameter("token", userLoginInfo.getNew_token());
		params.addBodyParameter("sequence", userLoginInfo.getSequence());
		params.addBodyParameter("stream_id", stream_id);
		params.addBodyParameter("url", url);
		params.addBodyParameter("vod_page", userLoginInfo.getUrl());
		
		
		
		

		//String path = "http://" + NetConfig.NC_HOST +
		// ":"+NetConfig.NC_POST+NetConfig.NC_BIND_URL;
		String path = "http://" + userLoginInfo.getService_url() + NetConfig.NC_BIND_URL;

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, path, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						// UIUtils.showToastSafe("reging");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (isUploading) {
						} else {
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						UserBingInfo userBingInfo  = JSONService
								.bindJSON(responseInfo.result);
						Log.i("BindInfo", responseInfo.result);
						callbackbindUesr.callBackbindUesr(userBingInfo);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.i("loginErrormsg", msg);
						UserBingInfo userBingInfo = new UserBingInfo();
						userBingInfo.setReturn_code(-1 + "");
						callbackbindUesr.callBackbindUesr(userBingInfo);
					}
				});
	}
	
	
	public void vodPlay(UserLoginInfo userLoginInfo, String url,String vodname,String posterurl,
			final CallbackvodPlay callbackvodPlay) {
		
		RequestParams params = new RequestParams();
		params.addBodyParameter("username", userLoginInfo.getUsername());
		params.addBodyParameter("token", userLoginInfo.getNew_token());
		params.addBodyParameter("sequence", userLoginInfo.getSequence());
		params.addBodyParameter("url", url);
		params.addBodyParameter("vodname", vodname);
		params.addBodyParameter("posterurl", posterurl);
		
		String path = "http://" + userLoginInfo.getService_url() + NetConfig.NC_VODPLAY_URL;

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, path, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						// UIUtils.showToastSafe("reging");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (isUploading) {
						} else {
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {

						Log.i("vodPlayInfo", responseInfo.result);
						
						VodPlayInfo vodPlayInfo  = JSONService
								.vodPlayJSON(responseInfo.result);
						Log.i("BindInfo", responseInfo.result);
						callbackvodPlay.callbackVodPlay(vodPlayInfo);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.i("vodPlayErrormsg", msg);
						VodPlayInfo vodPlayInfo = new VodPlayInfo();
						vodPlayInfo.setReturn_code(-1 + "");
						callbackvodPlay.callbackVodPlay(vodPlayInfo);
					}
				});
	}
	
	public void unBind(UserLoginInfo userLoginInfo,
			final CallbackunBind callbackunBind) {
		
		RequestParams params = new RequestParams();
		params.addBodyParameter("username", userLoginInfo.getUsername());
		params.addBodyParameter("token", userLoginInfo.getNew_token());
		params.addBodyParameter("sequence", userLoginInfo.getSequence());
		
		String path = "http://" + userLoginInfo.getService_url() + NetConfig.NC_UNBIND_URL;

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, path, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						// UIUtils.showToastSafe("reging");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (isUploading) {
						} else {
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {

						Log.i("unBindInfo", responseInfo.result);
						
						UnBindInfo unBindInfo = JSONService
								.unBindJSON(responseInfo.result);
						callbackunBind.callbackUnBind(unBindInfo);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.i("vodPlayErrormsg", msg);
						UnBindInfo unBindInfo = new UnBindInfo();
						unBindInfo.setReturn_code(-1 + "");
						callbackunBind.callbackUnBind(unBindInfo);
					}
				});
	}
	
	public void keySend(UserLoginInfo userLoginInfo,String key_type,String key_value,
			final CallbackkeySend callbackkeySend) {
		
		RequestParams params = new RequestParams();
		params.addBodyParameter("username", userLoginInfo.getUsername());
		params.addBodyParameter("token", userLoginInfo.getNew_token());
		params.addBodyParameter("sequence", userLoginInfo.getSequence());
		params.addBodyParameter("key_type", key_type);
		params.addBodyParameter("key_value", key_value);
		
		String path = "http://" + userLoginInfo.getService_url() + NetConfig.NC_KEYSEND_URL;

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, path, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						// UIUtils.showToastSafe("reging");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (isUploading) {
						} else {
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {

						Log.i("keySendInfo", responseInfo.result);
						
						KeySendInfo keySendInfo = JSONService
								.keySendInfoJSON(responseInfo.result);
						callbackkeySend.callbackKeySend(keySendInfo);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.i("vodPlayErrormsg", msg);
						KeySendInfo keySendInfo = new KeySendInfo();
						keySendInfo.setReturn_code(-1 + "");
						callbackkeySend.callbackKeySend(keySendInfo);
					}
				});
	}
	
	public void chooseTime(UserLoginInfo userLoginInfo,String begintime,
			final CallbackchooseTime callbackchooseTime) {
		
		RequestParams params = new RequestParams();
		params.addBodyParameter("username", userLoginInfo.getUsername());
		params.addBodyParameter("token", userLoginInfo.getNew_token());
		params.addBodyParameter("sequence", userLoginInfo.getSequence());
		params.addBodyParameter("begintime", begintime);
		params.addBodyParameter("stream_id", userLoginInfo.getStream_id());
		
		String path = "http://" + userLoginInfo.getService_url() + NetConfig.NC_CHOOSETIME_URL;

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, path, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						// UIUtils.showToastSafe("reging");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (isUploading) {
						} else {
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {

						Log.i("chooseTime", responseInfo.result);
						
						ChooseTimeInfo chooseTimeInfo = JSONService
								.chooseTimeInfoJSON(responseInfo.result);
						callbackchooseTime.callbackChooseTime(chooseTimeInfo);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.i("vodPlayErrormsg", msg);
						ChooseTimeInfo chooseTimeInfo = new ChooseTimeInfo();
						chooseTimeInfo.setReturn_code(-1 + "");
						callbackchooseTime.callbackChooseTime(chooseTimeInfo);
					}
				});
	}
	
	public void sessionQuery(UserLoginInfo userLoginInfo,
			final CallbacksessionQuery callbacksessionQuery) {
		
		RequestParams params = new RequestParams();
		params.addBodyParameter("username", userLoginInfo.getUsername());
		params.addBodyParameter("token", userLoginInfo.getNew_token());
		params.addBodyParameter("sequence", userLoginInfo.getSequence());
		
		String path = "http://" + userLoginInfo.getService_url() + NetConfig.SESSIONQUERY_URL;

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, path, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						// UIUtils.showToastSafe("reging");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (isUploading) {
						} else {
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {

						Log.i("chooseTime", responseInfo.result);
						
						SessionQueryInfo sessionQueryInfo = JSONService
								.eessionQueryInfoJSON(responseInfo.result);
						callbacksessionQuery.callbackSessionQuery(sessionQueryInfo);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.i("vodPlayErrormsg", msg);
						SessionQueryInfo sessionQueryInfo = new SessionQueryInfo();
						sessionQueryInfo.setReturn_code(-1 + "");
						callbacksessionQuery.callbackSessionQuery(sessionQueryInfo);
					}
				});
	}
}
