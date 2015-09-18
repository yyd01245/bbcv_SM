package com.bbcvision.Multiscreen.fragments;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bbcvision.Multiscreen.R;
import com.bbcvision.Multiscreen.bean.UserLoginInfo;
import com.bbcvision.Multiscreen.bean.VodPlayInfo;
import com.bbcvision.Multiscreen.bean.VodplayInfo2;
import com.bbcvision.Multiscreen.jsInterface.GetRstp;
import com.bbcvision.Multiscreen.jsInterface.JavaScriptInterface;
import com.bbcvision.Multiscreen.netservices.CallbackvodPlay;
import com.bbcvision.Multiscreen.netservices.JSONService;
import com.bbcvision.Multiscreen.tools.UIUtils;
import com.bbcvision.Multiscreen.view.MyWebView;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.ArrayMap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 
 * @Title: web页面
 * 
 * @Description:
 * 
 * @author Nestor bbcvision.com
 * 
 * @date 2014-10-14 上午9:20:25
 * 
 * @version V1.0
 * 
 */
public class WebFragment extends BaseFragment {

	private View view;
	private static MyWebView wv_home;
	private JavaScriptInterface JavaScriptInterface;
	public String rstpurl;
	public boolean isVod;

	private String vodname;
	private String posterurl;
	private ViewPager viewPager;
	private RemoteFragment remoteFragment;
    private String url;

    @Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.fragment_web, null);
		return view;
	}

	@Override
	public void initData() {

		wv_home = (MyWebView) view.findViewById(R.id.wv_home);
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
						+ "   window.avodplay.toAndroid(jsonStr);}"
						+ "function kysxcollection(str) {"
						+ "   window.avodplay.toAndroidToast(str);}";
				wv_home.loadUrl("javascript:" + js);
			}
		});
		wv_home.setEnabled(true);
		WebSettings settings = wv_home.getSettings();
		// settings.setUseWideViewPort(true);
		// settings.setLoadWithOverviewMode(true);
		// 如果访问的页面中有Javascript，则webview必须设置支持Javascript。
		settings.setJavaScriptEnabled(true);
		settings.setLoadsImagesAutomatically(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setDomStorageEnabled(true);
		// 触摸焦点起作用
		wv_home.requestFocus();
		// 取消滚动条
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
		// 添加JS接口函数

		wv_home.addJavascriptInterface(new AvodPlay(), "avodplay");

		//webHtml();

	}

	public class AvodPlay {

		/**
		 * 该方法被浏览器端调用
		 */
		@JavascriptInterface
		public void toAndroidToast(String str) {
			UIUtils.showToastSafe(str);
		}

		@JavascriptInterface
		public void toAndroid(final String jsonStr) {
			// rstpurl = rstp;
			/*final String jsonStr = "{rstp:rtsp://192.168.100.11:8845/yjy_ipqam/8081/gyc.ts,"
					+ "vodname:aaa,posterurl:bbb}";*/
			//Log.i("rstpurl", jsonStr);
            LogUtils.i("#########################################"+jsonStr);
			Handler mHandler = new Handler();

			mHandler.post(new Runnable() {

				public void run() {

                    try {
                        VodplayInfo2 vodplayInfo2 = (VodplayInfo2)JSONService.jsonToBean(jsonStr,VodplayInfo2.class);
                        if ("1".equals(userLoginInfo.getStatus())) {
                            UIUtils.showToastSafe("请先绑定频道");
                        } else {
                            if (isVod == true) {
                                UIUtils.showToastSafe("您已经点播了，请稍后");
                            } else {
                                isVod = true;
                                vodname = vodplayInfo2.getVodname();
                                posterurl = vodplayInfo2.getPosterurl();
                                    toPlay(vodplayInfo2.getRtsp());
                            }
                        }
                    } catch (Exception e) {
                        UIUtils.showToastSafe("old_jsonStr");
                        e.printStackTrace();
                        Map<String, String> strs = cutJsonStr(jsonStr);
                        // Map<String, String> strs = new HashMap<String, String>();
                        // strs.put("rstp", jsonStr);
                        //Log.i("rstp",strs.get("rstp") + "---" + strs.get("vodname")+ "---" + strs.get("posterurl"));
                        LogUtils.i(strs.get("rstp") + "---" + strs.get("vodname")
                                + "---" + strs.get("posterurl"));
                        if ("1".equals(userLoginInfo.getStatus())) {
                            UIUtils.showToastSafe("请先绑定频道");
                        } else {
                            if (isVod == true) {
                                UIUtils.showToastSafe("您已经点播了，请稍后");
                            } else {
                                isVod = true;
                                vodname = strs.get("vodname");
                                posterurl = strs.get("posterurl");
                                if (strs.get("rstp") == null) {
                                    toPlay(jsonStr);
                                } else {
                                    toPlay(strs.get("rstp"));
                                }
                            }
                        }
                    }




				}

			});

		}

	}

	public void webHtml() {
        if (url!=null & userLoginInfo.getUrl().equals(url)){
            return;
        }
        url = userLoginInfo.getUrl();
		// String url = "http://www.baidu.com";
		// String url =
		// "http://192.168.30.237:8080/NSCS/mobile/voddetail.jsp?name=1";
		// String url = "file:///android_asset/page.html";
		try {
			wv_home.loadUrl(url);
			//Log.i("getUrl", userLoginInfo.getUrl() + "kk");

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

		Matcher msg3 = Pattern.compile("posterurl:(.*?)\\}").matcher(jsonStr);
		while (msg3.find()) {
			strs.put("posterurl", msg3.group(1));
		}

		return strs;
	}

	public void toPlay(String rstp) {
		UIUtils.showToastSafe("正在点播");

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
							UIUtils.showToastSafe("已成功点播");
							userLoginInfo.setNew_token(vodPlayInfo
									.getNew_token());
							userLoginInfo.setStatus(vodPlayInfo.getStatus());
							userLoginInfo.setVodname(vodname);
							//							Log.i("vodname", vodname);
							// getVolStatus.getVolStatu(vodPlayInfo.getStatus(),
							// callbackState)
							if (viewPager != null) {
								remoteFragment.setVolPlayPause();
								viewPager.setCurrentItem(2);
							}
						} else {
							UIUtils.showToastSafe("点播失败("
									+ vodPlayInfo.getMsg() + ")");
						}
						isVod = false;
					}
				});
	}

	/*private String getVodname(String url) {
		// 取得最后一个/的下标
		int index = url.lastIndexOf("?name");
		// 将字符串转为字符数组
		char[] ch = url.toCharArray();
		// 根据 copyValueOf(char[] data, int offset, int count) 取得最后一个字符串
		String res = String.copyValueOf(ch, index + 1, ch.length - index - 1);
		return res;
	}*/

	public static boolean onKeyDown(int keyCoder, KeyEvent event) {
		if (wv_home.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK) {
			wv_home.goBack();
			return true;
		}
		return false;
	}

	public void setViewPager(ViewPager mPager) {
		viewPager = mPager;
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("WebFragment"); //统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("WebFragment");
	}

	public void getRemoteFragment(RemoteFragment remoteFragment) {
		this.remoteFragment = remoteFragment;
	}
}
