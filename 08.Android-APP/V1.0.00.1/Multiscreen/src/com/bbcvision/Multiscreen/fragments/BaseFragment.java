package com.bbcvision.Multiscreen.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import com.bbcvision.Multiscreen.bean.UserBingInfo;
import com.bbcvision.Multiscreen.bean.UserLoginInfo;
import com.bbcvision.Multiscreen.bean.VodPlayInfo;
import com.bbcvision.Multiscreen.netservices.RESTService;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseFragment extends Fragment {

	private View view;
	public Context ct;
	public boolean is_load = false;
	public SlidingMenu SM;
	public SharedPreferences sp;
	public UserLoginInfo userLoginInfo;
	public RESTService restService;
	public UserBingInfo userBingInfo;
	public VodPlayInfo vodPlayInfo;
	public Display myDisplay;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// 初始化数据
		initData();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (sp == null) {
			sp = getActivity().getSharedPreferences("config",
					getActivity().MODE_PRIVATE);
		}

		Intent intent = getActivity().getIntent();
		if (intent != null) {
			userLoginInfo = (UserLoginInfo) intent
					.getSerializableExtra("userLoginInfo");
		}
		
		if(restService == null){
			restService = new RESTService();
		}
		
		if(userBingInfo == null){
			userBingInfo = new UserBingInfo();
		}
		
		if(vodPlayInfo == null){
			vodPlayInfo = new VodPlayInfo();
		}
		
		ct = getActivity();
		// 得到滑动菜单
		// SM = ((HomeActivity2)ct).getSlidingMenu();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 初始化View
		view = initView(inflater);
		return view;
	}

	public View getRootView() {
		return view;
	}
	
	public void setDisplay(Display display) {
		myDisplay = display;
	}

	/**
	 * 初始化View
	 * 
	 * @param inflater
	 * @return View
	 */
	public abstract View initView(LayoutInflater inflater);

	/**
	 * 初始化数据
	 */
	public abstract void initData();
	
}
