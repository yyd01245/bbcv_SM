package com.bbcvision.Multiscreen.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import com.bbcvision.Multiscreen.bean.UserBingInfo;
import com.bbcvision.Multiscreen.bean.UserLoginInfo;
import com.bbcvision.Multiscreen.bean.VodPlayInfo;
import com.bbcvision.Multiscreen.netservices.RESTService;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

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

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = initView(inflater);
		return view;
	}

	public View getRootView() {
		return view;
	}

	
	public abstract View initView(LayoutInflater inflater);

	
	public abstract void initData();
	
	
}
