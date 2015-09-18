package com.bbcvision.Multiscreen.activity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.bbcvision.Multiscreen.netservices.RESTService;

public class BaseActivity extends Activity {

	private static BaseActivity mForegroundActivity = null;
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

	public static void finishAll() {
		List<BaseActivity> copy;
		synchronized (mActivities) {
			copy = new ArrayList<BaseActivity>(mActivities);
		}
		for (BaseActivity activity : copy) {
			activity.finish();
		}
	}

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

	public static boolean hasActivity() {
		return mActivities.size() > 0;
	}

	public static BaseActivity getForegroundActivity() {
		return mForegroundActivity;
	}

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

	public void exitApp() {
		finishAll();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
