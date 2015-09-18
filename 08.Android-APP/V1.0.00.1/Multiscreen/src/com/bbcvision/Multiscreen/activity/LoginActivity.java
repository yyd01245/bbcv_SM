package com.bbcvision.Multiscreen.activity;

import java.util.Map;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bbcvision.Multiscreen.R;
import com.bbcvision.Multiscreen.bean.UserLoginInfo;
import com.bbcvision.Multiscreen.configs.NetConfig;
import com.bbcvision.Multiscreen.netservices.CallbackaccessUesr;
import com.bbcvision.Multiscreen.netservices.CallbackloginUesr;
import com.bbcvision.Multiscreen.netservices.CallbackregUesr;
import com.bbcvision.Multiscreen.tools.CommonTool;
import com.bbcvision.Multiscreen.tools.UIUtils;
import com.bbcvision.Multiscreen.view.LoginItemView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;

import org.json.JSONException;
import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 
 * @Title: 登入、注册
 * 
 * @Description:
 * 
 * @author Nestor bbcvision.com
 * 
 * @date 2014-10-10 上午10:36:00
 * 
 * @version V1.0
 * 
 */
public class LoginActivity extends BaseActivity {

	@ViewInject(R.id.liv_pasword)
	private LoginItemView liv_pasword;
	@ViewInject(R.id.bt_reg)
	private TextView tv_reg;
	@ViewInject(R.id.ll_reg)
	private LinearLayout ll_reg;
	@ViewInject(R.id.ll_login)
	private LinearLayout ll_login;
	@ViewInject(R.id.bt_reg2)
	private TextView tv_reg2;
	@ViewInject(R.id.ll_btlogin)
	private LinearLayout ll_btlogin;
	@ViewInject(R.id.ll_btreg)
	private LinearLayout ll_btreg;
	@ViewInject(R.id.liv_regpasword)
	private LoginItemView liv_regpasword;
	@ViewInject(R.id.liv_regpasworded)
	private LoginItemView liv_regpasworded;
	@ViewInject(R.id.liv_regnumber)
	private LoginItemView liv_regnumber;
	@ViewInject(R.id.liv_vcode)
	private LoginItemView liv_vcode;
	private String regnumber;
	private String regpsw;
	private String regrepsw;
	private String regvcode;
	@ViewInject(R.id.bt_login)
	private TextView bt_login;
	@ViewInject(R.id.liv_number)
	private LoginItemView liv_number;
	private String number;
	private String psw;
	private long firstBackTime;
	@ViewInject(R.id.rl_loding)
	private RelativeLayout rl_loding;
	@ViewInject(R.id.cb_remumber)
	private CheckBox cb_remumber;
	private InputMethodManager manager;
    @ViewInject(R.id.tv_getVCode)
    private TextView tv_getVCode;

    private EventHandler handler = new EventHandler() {
        @SuppressWarnings("unchecked")
        public void afterEvent(final int event, final int result, final Object data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                        /*if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }*/
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                            // 请求支持国家列表
                            //onCountryListGot((ArrayList<HashMap<String,Object>>) data);
                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            //请求验证码后，跳转到验证码填写页面
                            //afterVerificationCodeRequested();
                        }else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            //验证成功
                            askReg();
                        }
                    } else {
                        // 根据服务器返回的网络错误，给toast提示
                        try {
                            ((Throwable) data).printStackTrace();
                            Throwable throwable = (Throwable) data;

                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");
                            if (!TextUtils.isEmpty(des)) {
                                Toast.makeText(LoginActivity.this, des, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // 如果木有找到资源，默认提示
                            /*int resId = getStringRes(activity, "smssdk_network_error");
                            if (resId > 0) {
                                Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show();
                            }*/
                    }
                }
            });
        }
    };
    private String regnumberYZ;

    @Override
	protected void initView() {
		super.initView();
        PushAgent.getInstance(this).onAppStart();
		setContentView(R.layout.activity_login);

        ViewUtils.inject(this);
		
		UmengUpdateAgent.update(this);

        SMSSDK.registerEventHandler(handler);
		
		manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);  

		liv_number.setText(sp.getString("username", null));
		
		liv_pasword.isPassword(true);
		
		if(sp.getString("pwd", null)!=null){
			liv_pasword.setText(sp.getString("pwd", null));
			cb_remumber.setChecked(true);
		}else{
			cb_remumber.setChecked(false);
		}

		liv_regpasword.isPassword(true);
		
		liv_regpasworded.isPassword(true);
		
		liv_regnumber.setmBackground(R.drawable.login_tv2);
		
		Editor ed = sp.edit();
		ed.putString("scanMsg", null);
		ed.commit();
		
		bt_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!hasNet()) {
					return;
				}
				login();
			}
		});

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

		tv_reg2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!hasNet()) {
					return;
				}
				reg();
			}
		});

        tv_getVCode.setOnClickListener(new OnClickListener() {
            public boolean canVCode = true;
            public boolean canVCode2 = true;
            @Override
            public void onClick(View v) {
                if(canVCode){
                    regnumberYZ = liv_regnumber.getText();
                    if (!CommonTool.isMobileNumber(regnumberYZ)) {
                        UIUtils.showToastSafe("请输入正确的手机号！");
                    }else{
                        canVCode = false;
                        Toast.makeText(LoginActivity.this, "正在获取验证码", Toast.LENGTH_SHORT).show();
                        SMSSDK.getVerificationCode("86", regnumberYZ);
                    }
                }else{
                    UIUtils.showToastSafe("15秒后可再次发送验证码");
                    if(canVCode2){
                        canVCode2 = false;
                        new Thread(){
                            @Override
                            public void run() {
                                SystemClock.sleep(15000);
                                canVCode=true;
                                canVCode2=true;
                            }
                        }.start();
                    }
                }

            }
        });
	}

	protected boolean hasNet() {
		if (CommonTool.isNetworkAvailable(getApplication()) == 0) {
			UIUtils.showToastSafe("请链接网络后重试");
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
		//UIUtils.showToastSafe("正在登入，请稍后！");
		rl_loding.setVisibility(View.GONE);
		// 接入接口
		restService.accessUesr(number, psw, new CallbackaccessUesr() {

			@Override
			public void callbackAccessUesr(Map<String, String> results) {
				//Log.i("results", results + "");
                LogUtils.i( results + "");
				int code;
				if (results == null) {
					code = -1;
				} else {
					code = Integer.parseInt(results.get("return_code"));
					//Log.i("code", code + "");
                    LogUtils.i( code + "");
				}
				if (code == 0) {
					// 登入
					restService.loginUesr(results, number,
							new CallbackloginUesr() {
								@Override
								public void callbackLoginUesr(
										UserLoginInfo userLoginInfo) {
									if("0".equals(userLoginInfo.getReturn_code())){
										UIUtils.showToastSafe("登入成功");

										//sp.edit().putString("username", number).commit();
										Editor ed = sp.edit();
										ed.putString("username", number);

										if(cb_remumber.isChecked()||true){
											ed.putString("pwd", psw);
										}else{
											ed.putString("pwd", null);
										}
										ed.commit();

                                        SMSSDK.unregisterEventHandler(handler);
                                        //LogUtils.e(userLoginInfo.getChannel_id()+"##"+sp.getString("Channel_id", null));
										//userLoginInfo.setChannel_id(sp.getString("Channel_id", null));
                                        if("1".equals(userLoginInfo.getStatus())){
                                            sp.edit().putString("homeurl",userLoginInfo.getUrl()).commit();
                                        }

                                        LogUtils.i("###URL:"+userLoginInfo.getUrl());

										Intent intent = new Intent(
												LoginActivity.this,
												HomeActivity2.class);
										intent.putExtra("userLoginInfo", userLoginInfo);
										startActivity(intent);

										finish();
									}else{
										UIUtils.showToastSafe("登入失败(" + userLoginInfo.getReturn_code() + ")");
										login_v();
									}
									
								}
							});

				} else if (code == -1) {
					rl_loding.setVisibility(View.VISIBLE);
					UIUtils.showToastSafe("登入失败,连接出错");
					sp.edit().putString("username", null).commit();
					login_v();
				} else {
					rl_loding.setVisibility(View.VISIBLE);
					UIUtils.showToastSafe("接入失败(" + code + ")");
					sp.edit().putString("username", null).commit();
					login_v();
				}

			}
		});

	}

	/**
	 * 登入信息检测
	 * @return
	 */
	private Boolean isComplete2() {
		number = liv_number.getText();
		psw = liv_pasword.getText();

		if (!CommonTool.isMobileNumber(number)) {
			UIUtils.showToastSafe("请输入正确的手机号");
			return false;
		}

		if (psw.length() < 6) {
			UIUtils.showToastSafe("密码必须大于6位");
			return false;
		}

		return true;
	}

	private void reg() {
		Boolean complete = isComplete();
		if (!complete) {
			return;
		}

        SMSSDK.submitVerificationCode("86", regnumber, regvcode);

		//askReg();
	}

	/**
	 * 请求注册
	 */
	private void askReg() {
		restService.regUesr(regnumber, regpsw, new CallbackregUesr() {
			@Override
			public void callbackRegUesr(int code) {
				if (code == 0) {
					UIUtils.showToastSafe("注册成功，请登入！");
					liv_number.setText(regnumber);
					liv_pasword.setText(regpsw);
					login_v();
				} else if (code == -1) {
					UIUtils.showToastSafe("注册失败,连接出错");
				} else {
					UIUtils.showToastSafe("注册失败(" + code + ")，可能用户名已经被注册！");
				}
			}
		});
	}


	/**
	 * 验证注册信息
	 * @return
	 */
	private Boolean isComplete() {
		regnumber = liv_regnumber.getText();
		regpsw = liv_regpasword.getText();
		regrepsw = liv_regpasworded.getText();
		regvcode = liv_vcode.getText();

		if (regnumber.isEmpty() || regpsw.isEmpty() || regrepsw.isEmpty()
				|| regvcode.isEmpty()) {
			UIUtils.showToastSafe("请填写完整");
			return false;
		}

		if (!CommonTool.isMobileNumber(regnumber)) {
			UIUtils.showToastSafe("请输入正确的手机号");
			return false;
		}

        if(!regnumberYZ.equals(regnumber)){
            UIUtils.showToastSafe("验证号码与注册号码不一致！");
            return false;
        }

		if (regpsw.length() < 6) {
			UIUtils.showToastSafe("密码必须大于6位");
			return false;
		}

		if (!regpsw.equals(regrepsw)) {
			UIUtils.showToastSafe("两次密码不一致");
			return false;
		}

		return true;
	}

	@Override
	public void onBackPressed() {
		login_v();
		
		doubleBackExit();
	}

	private void doubleBackExit() {
		if(firstBackTime>0){
			long sencondBackTime = System.currentTimeMillis();
			if((sencondBackTime-firstBackTime)<500){
                SMSSDK.unregisterEventHandler(handler);
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
		UIUtils.showToastSafe("连续按两次返回退出应用");
	}
	
	public void login_v(){
		ll_login.setVisibility(View.VISIBLE);
		ll_reg.setVisibility(View.GONE);
		ll_btlogin.setVisibility(View.VISIBLE);
		ll_btreg.setVisibility(View.GONE);
	}
	
    
    @Override  
     public boolean onTouchEvent(MotionEvent event) {  
      // 点击空白区域隐藏软键盘 
      if(event.getAction() == MotionEvent.ACTION_DOWN){  
         if(getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null){  
           manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);  
         }  
      }  
      return super.onTouchEvent(event);  
     }
    
    @Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("LoginActivity"); 
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("LoginActivity"); 
		MobclickAgent.onPause(this);
	}
    
    @OnClick({R.id.bt_toaliact,R.id.tv_setIP})
    private void toalipay(View v){
        switch (v.getId()){
            case R.id.bt_toaliact:
                Intent intent = new Intent(this, AlipayTest.class);
                startActivity(intent);
                break;
            case R.id.tv_setIP:
                setIP();
                break;
        }

    }

    private void setIP() {
        Intent intent = new Intent(this, SetIPActivity.class);
        startActivity(intent);
    }

}
