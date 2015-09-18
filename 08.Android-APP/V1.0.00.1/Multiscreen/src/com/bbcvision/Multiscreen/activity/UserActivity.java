package com.bbcvision.Multiscreen.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bbcvision.Multiscreen.R;
import com.bbcvision.Multiscreen.bean.KeySendInfo;
import com.bbcvision.Multiscreen.bean.UserLoginInfo;
import com.bbcvision.Multiscreen.netservices.CallbackkeySend;
import com.bbcvision.Multiscreen.tools.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

/**
 * 
 * @Title: 
 *		
 * @Description: 
 *		
 * @author Nestor bbcvision.com 
 *		
 * @date 2014年11月4日 上午11:20:17
 *		
 * @version V1.0  
 *
 */
public class UserActivity extends BaseActivity {
	@ViewInject(R.id.tv_username)
	private TextView tv_username;
	/*@ViewInject(R.id.tv_reloning)
	private TextView tv_reloning;
	@ViewInject(R.id.tv_exitapp)
	private TextView tv_exitapp;*/
	private UserLoginInfo userLoginInfo;
	private String status;
	private int exitVodNum;

	@Override
	protected void initView() {
		super.initView();
        PushAgent.getInstance(this).onAppStart();
		setContentView(R.layout.activity_user);
		ViewUtils.inject(this);
		Intent intent = getIntent();
		userLoginInfo = (UserLoginInfo) intent
				.getSerializableExtra("userLoginInfo");
		status = userLoginInfo.getStatus();

		tv_username.setText(userLoginInfo.getUsername());
	}

	@OnClick({ R.id.tv_reloning, R.id.tv_exitapp })
	public void userButtonClick(View v) {
		switch (v.getId()) {
		case R.id.tv_reloning:
			reloning();
			break;
		case R.id.tv_exitapp:
			exitapp();
			break;
		}
	}

	private void exitapp() {
		if ("3".equals(status)) {
			UIUtils.showToastSafe("正在退出，请稍后！");
			exitVodNum=0;
			exitVod(0);
		} else {
			HomeActivity2.instance.finish();
			exitApp();
		}

	}

	private void exitVod(final int type) {
		if(exitVodNum>3){
			UIUtils.showToastSafe("操作失败，请检查网路！");
			return;
		}
		restService.keySend(userLoginInfo, "1001", "0x1d",
				new CallbackkeySend() {
					@Override
					public void callbackKeySend(KeySendInfo keySendInfo) {
						if ("0".equals(keySendInfo.getReturn_code())) {
							if("3".equals(keySendInfo.getStatus())){
								new Thread(){
									@Override
									public void run() {
										try {
											Thread.sleep(2500);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										exitVodNum++;
										exitVod(type);
									}
								}.start();
							}else{
								if(type==0){
									HomeActivity2.instance.finish();
									exitApp();
								}else{
									Intent intent = new Intent(UserActivity.this, LoginActivity.class);
									startActivity(intent);
									finish();
								}
							}
						}else{
							if(exitVodNum>=2){
								UIUtils.showToastSafe("停止点播失败！");
							}
						}
					}
		});
	}

	private void reloning() {
		if ("3".equals(status)) {
			UIUtils.showToastSafe("正在注销，请稍后！");
			exitVodNum=0;
			exitVod(1);
		} else {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("UserActivity");
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("UserActivity");
		MobclickAgent.onPause(this);
	}
}
