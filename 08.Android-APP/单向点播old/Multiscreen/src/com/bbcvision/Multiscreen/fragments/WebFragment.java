package com.bbcvision.Multiscreen.fragments;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bbcvision.Multiscreen.R;
import com.bbcvision.Multiscreen.bean.UserLoginInfo;
import com.bbcvision.Multiscreen.bean.VodPlayInfo;
import com.bbcvision.Multiscreen.jsInterface.GetRstp;
import com.bbcvision.Multiscreen.jsInterface.JavaScriptInterface;
import com.bbcvision.Multiscreen.netservices.CallbackvodPlay;
import com.bbcvision.Multiscreen.tools.UIUtils;
import com.bbcvision.Multiscreen.view.MyWebView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.ArrayMap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class WebFragment extends BaseFragment {

	private View view;
	private static WebView wv_home;
	private JavaScriptInterface JavaScriptInterface;
	public String rstpurl;
	public boolean isVod;

	private String vodname;
	private String posterurl;

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.fragment_web, null);
		return view;
	}

	@Override
	public void initData() {

		wv_home = (WebView) view.findViewById(R.id.wv_home);
		wv_home.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

				String js = "function kysxplay(jsonStr) {"
						+ "   window.avodplay.toAndroid(jsonStr);" + "}";
				wv_home.loadUrl("javascript:" + js);
			}
		});
		wv_home.setEnabled(true);
		WebSettings settings = wv_home.getSettings();
		// settings.setUseWideViewPort(true);
		// settings.setLoadWithOverviewMode(true);
		// 濡傛灉璁块棶鐨勯〉闈腑鏈塉avascript锛屽垯webview蹇呴』璁剧疆鏀寔Javascript銆�
		settings.setJavaScriptEnabled(true);
		settings.setLoadsImagesAutomatically(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setDomStorageEnabled(true);
		// 瑙︽懜鐒︾偣璧蜂綔鐢�
		wv_home.requestFocus();
		// 鍙栨秷婊氬姩鏉�
		wv_home.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

		wv_home.setWebChromeClient(new WebChromeClient() {
			// JSalert()
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					final JsResult result) {
				AlertDialog.Builder b2 = new AlertDialog.Builder(getActivity())
						.setTitle(R.string.title)
						.setMessage(message)
						.setPositiveButton("ok",
								new AlertDialog.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										result.confirm();
										// MyWebView.this.finish();
									}
								});

				b2.setCancelable(false);
				b2.create();
				b2.show();
				return true;
			}
		});

		// 娣诲姞JS鎺ュ彛鍑芥暟

		wv_home.addJavascriptInterface(new AvodPlay(), "avodplay");

		//webHtml();

	}

	public class AvodPlay {

		
		@JavascriptInterface
		public void toAndroid(final String jsonStr) {
			// public void toAndroid(String rstp,String vodname,String
			// posterurl) {
			// rstpurl = rstp;
			
			Log.i("rstpurl", jsonStr);
			Handler mHandler = new Handler();

			mHandler.post(new Runnable() {

				public void run() {

					Map<String, String> strs = cutJsonStr(jsonStr);
					// Map<String, String> strs = new HashMap<String, String>();
					// strs.put("rstp", jsonStr);
					Log.i("rstp",
							strs.get("rstp") + "---" + strs.get("vodname"));
					if ("1".equals(userLoginInfo.getStatus())) {
						UIUtils.showToastSafe("璇峰厛缁戝畾棰戦亾");
					} else {
						if (isVod == true) {
							UIUtils.showToastSafe("鎮ㄥ凡缁忕偣鎾簡锛岃绋嶅悗");
						} else {
							isVod = true;
							vodname = strs.get("vodname");
							// posterurl = strs.get("posterurl");
							toPlay(strs.get("rstp"));
						}
					}

				}

			});

		}

		
	}

	public void webHtml() {
		String url = userLoginInfo.getUrl();
		// String url = "http://www.baidu.com";
		// String url =
		// "http://192.168.30.237:8080/NSCS/mobile/voddetail.jsp?name=1";
		// String url = "file:///android_asset/page.html";
		try {
			wv_home.loadUrl(url);
			Log.i("getUrl", userLoginInfo.getUrl() + "kk");

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public Map<String, String> cutJsonStr(String jsonStr) {
		Map<String, String> strs = new HashMap<String, String>();

		Matcher msg1 = Pattern.compile("rstp:(.*?),").matcher(jsonStr);
		while (msg1.find()) {
			strs.put("rstp", msg1.group(1));
		}

		Matcher msg2 = Pattern.compile("vodname:(.*?),").matcher(jsonStr);
		while (msg2.find()) {
			strs.put("vodname", msg2.group(1));
		}

		

		return strs;
	}

	public void toPlay(String rstp) {
		UIUtils.showToastSafe("姝ｅ湪鐐规挱");

		// String url = wv_home.getUrl();
		// String vodname = getVodname(url);
		// String posterurl = "/home/bbcv/"+vodname+".jsp";
		// Log.i("posterurl", posterurl);
		// rstp = "rtsp://192.168.100.11:8845/yjy_ipqam/8081/ypzjs.ts";
		restService.vodPlay(userLoginInfo, rstp, vodname, posterurl,
				new CallbackvodPlay() {

					@Override
					public void callbackVodPlay(VodPlayInfo vodInfo) {
						vodPlayInfo = vodInfo;
						// Log.i("getReturn_code",
						// userBingInfo.getReturn_code());
						if ("0".equals(vodPlayInfo.getReturn_code())) {
							UIUtils.showToastSafe("宸叉垚鍔熺偣鎾�");
							userLoginInfo.setNew_token(vodPlayInfo
									.getNew_token());
							userLoginInfo.setStatus(vodPlayInfo.getStatus());
							vodPlayInfo.setVodname(vodname);
							// getVolStatus.getVolStatu(vodPlayInfo.getStatus(),
							// callbackState)
						} else {
							UIUtils.showToastSafe("鐐规挱澶辫触("
									+ vodPlayInfo.getMsg() + ")");
						}
						isVod = false;
					}
				});
	}

	private String getVodname(String url) {
		int index = url.lastIndexOf("?name");
		char[] ch = url.toCharArray();
		String res = String.copyValueOf(ch, index + 1, ch.length - index - 1);
		return res;
	}

	public static boolean onKeyDown(int keyCoder, KeyEvent event) {
		if (wv_home.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK) {
			wv_home.goBack();
			return true;
		}
		return false;
	}
}
