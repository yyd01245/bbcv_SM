package com.bbcvision.Multiscreen.fragments;

import java.util.ArrayList;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbcvision.Multiscreen.R;


public class HomeFragment extends BaseFragment {

	
	private View view;
	private ViewPager mPager;
	private ImageView iv_page;
	private WebView wv_home;
	private RemoteFragment remoteFragment;
	private WebFragment webFragment;
	
	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.frag_home, null);
		return view;
	}


	@Override
	public void initData() {
		iv_page = (ImageView) view.findViewById(R.id.iv_page);
		
		InitViewPager();
	}


	private void InitViewPager() {
		mPager = (ViewPager) view.findViewById(R.id.home_view);
		
		ArrayList<Fragment> fragmentArray = new ArrayList<Fragment>();
		fragmentArray.add(new ScanFragment());
		webFragment = new WebFragment();
		fragmentArray.add(webFragment);
		remoteFragment = new RemoteFragment();
		fragmentArray.add(remoteFragment);
		
		mPager.setAdapter(new FragmentAdapter(getActivity().getSupportFragmentManager(), fragmentArray));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
		
	}
	
	
	private void InitImageView() {
		
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
	}
	
	
	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			if(arg0 == 1){
				iv_page.setImageResource(R.drawable.bottom_page_2);
				webFragment.webHtml();
			}else if(arg0 == 2){
				iv_page.setImageResource(R.drawable.bottom_page_3);
				remoteFragment.getVolStatus();
			}else{
				iv_page.setImageResource(R.drawable.bottom_page_1);
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	
	public static boolean onKeyDown(int keyCoder, KeyEvent event) {
		return WebFragment.onKeyDown(keyCoder, event);
	}
}
