package com.bbcvision.Multiscreen.activity;

import com.bbcvision.Multiscreen.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

public class HelpActivity extends BaseActivity {

	@Override
	protected void initView() {
		super.initView();
        PushAgent.getInstance(this).onAppStart();
		setContentView(R.layout.activity_help);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("HelpActivity"); 
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("HelpActivity"); 
		MobclickAgent.onPause(this);
	}
}
