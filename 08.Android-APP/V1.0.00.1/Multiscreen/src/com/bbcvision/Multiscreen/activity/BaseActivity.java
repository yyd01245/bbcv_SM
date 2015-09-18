package com.bbcvision.Multiscreen.activity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.bbcvision.Multiscreen.netservices.RESTService;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends Activity {

	/** 记录处于前台的Activity */
	private static BaseActivity mForegroundActivity = null;
	/** 记录所有活动的Activity */
	private static final List<BaseActivity> mActivities = new LinkedList<BaseActivity>();
	protected SharedPreferences sp;
	protected RESTService restService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initView();
		initActionBar();
	}

	@Override
	protected void onResume() {
		mForegroundActivity = this;
		super.onResume();
	}

	@Override
	protected void onPause() {
		mForegroundActivity = null;
		super.onPause();
	}

	protected void init() {
		if(sp == null){
			sp = getSharedPreferences("config", MODE_PRIVATE);
		}
		if(restService == null){
			restService = new RESTService();
		}
	}

	protected void initView() {
	}

	protected void initActionBar() {
	}

	/** 关闭所有Activity */
	public static void finishAll() {
		List<BaseActivity> copy;
		synchronized (mActivities) {
			copy = new ArrayList<BaseActivity>(mActivities);
		}
		for (BaseActivity activity : copy) {
			activity.finish();
		}
	}

	/** 关闭所有Activity，除了参数传递的Activity */
	public static void finishAll(BaseActivity except) {
		List<BaseActivity> copy;
		synchronized (mActivities) {
			copy = new ArrayList<BaseActivity>(mActivities);
		}
		for (BaseActivity activity : copy) {
			if (activity != except)
				activity.finish();
		}
	}

	/** 是否有启动的Activity */
	public static boolean hasActivity() {
		return mActivities.size() > 0;
	}

	/** 获取当前处于前台的activity */
	public static BaseActivity getForegroundActivity() {
		return mForegroundActivity;
	}

	/** 获取当前处于栈顶的activity，无论其是否处于前台 */
	public static BaseActivity getCurrentActivity() {
		List<BaseActivity> copy;
		synchronized (mActivities) {
			copy = new ArrayList<BaseActivity>(mActivities);
		}
		if (copy.size() > 0) {
			return copy.get(copy.size() - 1);
		}
		return null;
	}

	/** 推出应用 */
	public void exitApp() {
		finishAll();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
