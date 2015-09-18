package com.bbcvision.Multiscreen.activity;

import java.util.Map;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bbcvision.Multiscreen.R;
import com.bbcvision.Multiscreen.bean.UserLoginInfo;
import com.bbcvision.Multiscreen.netservices.CallbackaccessUesr;
import com.bbcvision.Multiscreen.netservices.CallbackloginUesr;
import com.bbcvision.Multiscreen.netservices.CallbackregUesr;
import com.bbcvision.Multiscreen.tools.CommonTool;
import com.bbcvision.Multiscreen.tools.UIUtils;
import com.bbcvision.Multiscreen.view.LoginItemView;


public class LoginActivity extends BaseActivity {

	private LoginItemView liv_pasword;
	private TextView tv_reg;
	private LinearLayout ll_reg;
	private LinearLayout ll_login;
	private TextView tv_reg2;
	private LinearLayout ll_btlogin;
	private LinearLayout ll_btreg;
	private LoginItemView liv_regpasword;
	private LoginItemView liv_regpasworded;
	private LoginItemView liv_regnumber;
	private LoginItemView liv_vcode;
	private String regnumber;
	private String regpsw;
	private String regrepsw;
	private String regvcode;
	private TextView bt_login;
	private LoginItemView liv_number;
	private String number;
	private String psw;
	private long firstBackTime;
	private RelativeLayout rl_loding;
	private CheckBox cb_remumber;

	@Override
	protected void initView() {
		super.initView();
		setContentView(R.layout.activity_login);

		ll_reg = (LinearLayout) findViewById(R.id.ll_reg);
		ll_login = (LinearLayout) findViewById(R.id.ll_login);
		ll_btlogin = (LinearLayout) findViewById(R.id.ll_btlogin);
		ll_btreg = (LinearLayout) findViewById(R.id.ll_btreg);

		liv_number = (LoginItemView) findViewById(R.id.liv_number);
		liv_number.setText(sp.getString("username", null));
		liv_pasword = (LoginItemView) findViewById(R.id.liv_pasword);
		liv_pasword.isPassword(true);
		cb_remumber = (CheckBox) findViewById(R.id.cb_remumber);
		if(sp.getString("pwd", null)!=null){
			liv_pasword.setText(sp.getString("pwd", null));
			cb_remumber.setChecked(true);
		}else{
			cb_remumber.setChecked(false);
		}

		liv_regpasword = (LoginItemView) findViewById(R.id.liv_regpasword);
		liv_regpasword.isPassword(true);
		liv_regpasworded = (LoginItemView) findViewById(R.id.liv_regpasworded);
		liv_regpasworded.isPassword(true);
		liv_regnumber = (LoginItemView) findViewById(R.id.liv_regnumber);
		liv_regnumber.setmBackground(R.drawable.login_tv2);
		liv_vcode = (LoginItemView) findViewById(R.id.liv_vcode);
		
		rl_loding = (RelativeLayout) findViewById(R.id.rl_loding);
		
		
		Editor ed = sp.edit();
		ed.putString("scanMsg", null);
		ed.commit();
		
		bt_login = (TextView) findViewById(R.id.bt_login);
		bt_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!hasNet()) {
					return;
				}
				login();
			}
		});

		tv_reg = (TextView) findViewById(R.id.bt_reg);
		tv_reg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!hasNet()) {
					return;
				}
				ll_login.setVisibility(View.GONE);
				ll_reg.setVisibility(View.VISIBLE);
				ll_btlogin.setVisibility(View.GONE);
				ll_btreg.setVisibility(View.VISIBLE);
			}
		});

		tv_reg2 = (TextView) findViewById(R.id.bt_reg2);
		tv_reg2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!hasNet()) {
					return;
				}
				reg();
			}
		});
	}

	protected boolean hasNet() {
		if (CommonTool.isNetworkAvailable(getApplication()) == 0) {
			UIUtils.showToastSafe("璇烽摼鎺ョ綉缁滃悗閲嶈瘯");
			return false;
		}
		return true;
	}

	protected void login() {
		Boolean complete = isComplete2();
		if (!complete) {
			return;
		}
		checkLogin();
	}

	private void checkLogin() {
		//UIUtils.showToastSafe("姝ｅ湪鐧诲叆锛岃绋嶅悗锛�");
		rl_loding.setVisibility(View.GONE);
		restService.accessUesr(number, psw, new CallbackaccessUesr() {

			@Override
			public void callbackAccessUesr(Map<String, String> results) {
				Log.i("results", results + "");
				int code;
				if (results == null) {
					code = -1;
				} else {
					code = Integer.parseInt(results.get("return_code"));
					Log.i("code", code + "");
				}
				if (code == 0) {
					restService.loginUesr(results, number,
							new CallbackloginUesr() {
								@Override
								public void callbackLoginUesr(
										UserLoginInfo userLoginInfo) {
									if("0".equals(userLoginInfo.getReturn_code())){
										UIUtils.showToastSafe("鐧诲叆鎴愬姛");

										//sp.edit().putString("username", number).commit();
										Editor ed = sp.edit();
										ed.putString("username", number);
										// TODO
										if(userLoginInfo.getUrl()!=null){
											ed.putString("homeURL", userLoginInfo.getUrl());
										}else{
											userLoginInfo.setUrl(sp.getString("homeURL", null));
										}
										if(cb_remumber.isChecked()){
											ed.putString("pwd", psw);
										}else{
											ed.putString("pwd", null);
										}
										ed.commit();
										
										userLoginInfo.setChannel_id(sp.getString("Channel_id", null));
										
										Intent intent = new Intent(
												LoginActivity.this,
												HomeActivity2.class);
										intent.putExtra("userLoginInfo", userLoginInfo);
										startActivity(intent);

										finish();
									}else{
										UIUtils.showToastSafe("鐧诲叆澶辫触(" + userLoginInfo.getReturn_code() + ")");
										login_v();
									}
									
								}
							});

				} else if (code == -1) {
					rl_loding.setVisibility(View.VISIBLE);
					UIUtils.showToastSafe("鐧诲叆澶辫触,杩炴帴鍑洪敊");
					sp.edit().putString("username", null).commit();
					login_v();
				} else {
					rl_loding.setVisibility(View.VISIBLE);
					UIUtils.showToastSafe("鎺ュ叆澶辫触(" + code + ")");
					sp.edit().putString("username", null).commit();
					login_v();
				}

			}
		});

	}

	private Boolean isComplete2() {
		number = liv_number.getText();
		psw = liv_pasword.getText();

		if (!CommonTool.isMobileNumber(number)) {
			UIUtils.showToastSafe("璇疯緭鍏ユ纭殑鎵嬫満鍙�");
			return false;
		}

		if (psw.length() < 6) {
			UIUtils.showToastSafe("瀵嗙爜蹇呴』澶т簬6浣�");
			return false;
		}

		return true;
	}

	private void reg() {
		Boolean complete = isComplete();
		if (!complete) {
			return;
		}

		askReg();

	}

	private void askReg() {
		restService.regUesr(regnumber, regpsw, new CallbackregUesr() {
			@Override
			public void callbackRegUesr(int code) {
				if (code == 0) {
					UIUtils.showToastSafe("娉ㄥ唽鎴愬姛锛岃鐧诲叆锛�");

					login_v();
				} else if (code == -1) {
					UIUtils.showToastSafe("娉ㄥ唽澶辫触,杩炴帴鍑洪敊");
				} else {
					UIUtils.showToastSafe("娉ㄥ唽澶辫触(" + code + ")锛屽彲鑳界敤鎴峰悕宸茬粡琚敞鍐岋紒");
				}
			}
		});
	}

	

	private Boolean isComplete() {
		regnumber = liv_regnumber.getText();
		regpsw = liv_regpasword.getText();
		regrepsw = liv_regpasworded.getText();
		regvcode = liv_vcode.getText();

		if (regnumber.isEmpty() || regpsw.isEmpty() || regrepsw.isEmpty()
				|| regvcode.isEmpty()) {
			UIUtils.showToastSafe("璇峰～鍐欏畬鏁�");
			return false;
		}

		if (!CommonTool.isMobileNumber(regnumber)) {
			UIUtils.showToastSafe("璇疯緭鍏ユ纭殑鎵嬫満鍙�");
			return false;
		}

		if (regpsw.length() < 6) {
			UIUtils.showToastSafe("瀵嗙爜蹇呴』澶т簬6浣�");
			return false;
		}

		if (!regpsw.equals(regrepsw)) {
			UIUtils.showToastSafe("涓ゆ瀵嗙爜涓嶄竴鑷�");
			return false;
		}

		return true;
	}

	@Override
	public void onBackPressed() {
		login_v();
		
		if(firstBackTime>0){
			long sencondBackTime = System.currentTimeMillis();
			if((sencondBackTime-firstBackTime)<500){
				finish();
				android.os.Process.killProcess(android.os.Process.myPid());
			}
			firstBackTime = 0;
			return ;
		}
		firstBackTime = System.currentTimeMillis();
		new Thread(){
			public void run() {
				try {
					Thread.sleep(600);
					firstBackTime = 0;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
		UIUtils.showToastSafe("杩炵画鎸変袱娆¤繑鍥為��鍑哄簲鐢�");
	}
	
	public void login_v(){
		ll_login.setVisibility(View.VISIBLE);
		ll_reg.setVisibility(View.GONE);
		ll_btlogin.setVisibility(View.VISIBLE);
		ll_btreg.setVisibility(View.GONE);
	}
}
