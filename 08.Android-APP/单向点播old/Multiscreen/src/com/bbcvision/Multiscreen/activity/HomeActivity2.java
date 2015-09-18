package com.bbcvision.Multiscreen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.bbcvision.Multiscreen.R;
import com.bbcvision.Multiscreen.bean.UserLoginInfo;
import com.bbcvision.Multiscreen.fragments.HomeFragment;
import com.bbcvision.Multiscreen.fragments.MenuFragment;
import com.bbcvision.Multiscreen.fragments.WebFragment;
import com.bbcvision.Multiscreen.tools.UIUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;


public class HomeActivity2 extends SlidingFragmentActivity {

	private UserLoginInfo userLoginInfo;
	private SlidingMenu sm;
	private MenuFragment menu;
	private long firstBackTime;
	private HomeFragment homeFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_home);

		Intent intent = getIntent();
		userLoginInfo = (UserLoginInfo) intent
				.getSerializableExtra("userLoginInfo");

		slidingMenu();

		homePage();

	}

	private void homePage() {
		homeFragment = new HomeFragment();
		
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.home, homeFragment, "HOME").commit();

	}

	private void slidingMenu() {
		setBehindContentView(R.layout.fragment_menu);
		sm = getSlidingMenu();
		
		sm.setMode(SlidingMenu.LEFT);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setShadowWidthRes(R.dimen.shadow_width);

		menu = new MenuFragment();
		
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu, menu, "MENU").commit();
	}

	
	
	public void switchFragment(Fragment fragment) {
	}

	
	
	@Override
	public void onBackPressed() {
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu); 
		return true; 
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId(); 
		if (id == R.id.item_exit) {
			finish();
			android.os.Process.killProcess(android.os.Process.myPid());
			return true; 
		} 
		return super.onOptionsItemSelected(item); 
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(homeFragment.onKeyDown(keyCode, event)){
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
}
