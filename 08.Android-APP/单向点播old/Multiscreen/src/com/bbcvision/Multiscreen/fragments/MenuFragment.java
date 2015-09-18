package com.bbcvision.Multiscreen.fragments;

import android.view.LayoutInflater;
import android.view.View;

import com.bbcvision.Multiscreen.R;
import com.lidroid.xutils.ViewUtils;

public class MenuFragment extends BaseFragment {

	//@ViewInject(R.id.lv_menu_news_center)
	//private ListView lv_menu_news_center;
	private View view;
	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.layout_left_menu, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData() {
		
	}

	

	
	
}
