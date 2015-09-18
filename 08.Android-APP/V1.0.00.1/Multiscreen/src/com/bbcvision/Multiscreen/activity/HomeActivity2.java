package com.bbcvision.Multiscreen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bbcvision.Multiscreen.R;
import com.bbcvision.Multiscreen.bean.UserLoginInfo;
import com.bbcvision.Multiscreen.fragments.HomeFragment;
import com.bbcvision.Multiscreen.fragments.MenuFragment;
import com.bbcvision.Multiscreen.fragments.WebFragment;
import com.bbcvision.Multiscreen.tools.UIUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

/**
 * 
 * @Title: 首页
 * 
 * @Description:
 * 
 * @author Nestor bbcvision.com
 * 
 * @date 2014-10-13 上午10:46:48
 * 
 * @version V1.0
 * 
 */
public class HomeActivity2 extends SlidingFragmentActivity {

	private UserLoginInfo userLoginInfo;
	private SlidingMenu sm;
	private MenuFragment menu;
	private long firstBackTime;
	public HomeFragment homeFragment;
	private ImageButton im_menu;
	private Display display;
	public static HomeActivity2 instance;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
		setContentView(R.layout.fragment_home);
		instance = this;
		// 得到认证信息
		Intent intent = getIntent();
		userLoginInfo = (UserLoginInfo) intent
				.getSerializableExtra("userLoginInfo");
		// Log.i("userLoginInfo", userLoginInfo.getUrl());

		slidingMenu();

		homePage();
		
		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
	    display = wm.getDefaultDisplay();
	   // screenWidth = display.getWidth(); // 屏幕的宽度
	    //screenHeight = display.getHeight(); // 屏幕的高度

	}

	private void homePage() {
		//ScanFragment scanFragment = new ScanFragment();
		homeFragment = new HomeFragment();
		homeFragment.setSlidingMenu(sm);
		homeFragment.setDisplay(display);
		/**
		 * beginTransaction()开启事务，.commit()提交 第一个参数是需要进行替换的id
		 * 第二个参数是需要进行替换的fragment 第三个参数是标签的意思tag(别名) 替换R.id.menu页面为menu
		 */
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.home, homeFragment, "HOME").commit();

	}

	private void slidingMenu() {
		// 设置滑动菜单的页面
		setBehindContentView(R.layout.fragment_menu);
		// 获得滑动菜单的对象
		sm = getSlidingMenu();
		// 设置滑动菜单的模式
		/**
		 * SlidingMenu.LEFT 设置滑动菜单 在左边 SlidingMenu.RIGHT 设置滑动菜单在右边
		 * SlidingMenu.LEFT_RIGHT 设置滑动菜单左右都有
		 * sm.setMenu第1个滑动菜单，sm.setSecondaryMenu第2个滑动菜单
		 */
		sm.setMode(SlidingMenu.LEFT);
		// 当滑动菜单出来之后，内容页面的剩余宽度
		sm.setBehindWidthRes(R.dimen.slidingmenu_width);
		// sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置滑动菜touch的模式
		/**
		 * SlidingMenu.TOUCHMODE_FULLSCREEN 设置滑动菜单全屏展示
		 * SlidingMenu.TOUCHMODE_MARGIN 设置滑动菜单只能在边沿滑动 SlidingMenu.TOUCHMODE_NONE
		 * 设置滑动菜单不能滑动
		 */
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// 设置滑动菜单的阴影
		sm.setShadowDrawable(R.drawable.shadow);
		// 设置滑动菜单阴影的宽度
		sm.setShadowWidthRes(R.dimen.shadow_width);

		// 侧滑菜单Fragment
		menu = new MenuFragment();
		/**
		 * beginTransaction()开启事务，.commit()提交 第一个参数是需要进行替换的id
		 * 第二个参数是需要进行替换的fragment 第三个参数是标签的意思tag(别名) 替换R.id.menu页面为menu
		 */
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu, menu, "MENU").commit();
	}

	/**
	 * 改变setContentView(R.layout.content_frame)的内容
	 * 
	 * @param fragment
	 */
	public void switchFragment(Fragment fragment) {
		// R.id.content：内容页面的id;替换内容页面为fragment
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.home, fragment, "HOME").commit();
		// 点击菜单项目后隐藏菜单
		toggle();
	}

	/*// 得到当前页面的菜单
	public MenuFragment2 getMenuFragment2() {
		menu = (MenuFragment2) getSupportFragmentManager().findFragmentByTag(
				"MENU");
		return menu;
	}

	// 提供homeFragment对象
	public HomeFragment getHomeFragment() {
		home = (HomeFragment) getSupportFragmentManager().findFragmentByTag(
				"HOME");
		return home;
	}*/

	@Override
	public void onBackPressed() {
		doubleBackExit();
	}

	private void doubleBackExit() {
		if (firstBackTime > 0) {
			long sencondBackTime = System.currentTimeMillis();
			if ((sencondBackTime - firstBackTime) < 700) {
				exitapp();
			}
			firstBackTime = 0;
			return;
		}
		firstBackTime = System.currentTimeMillis();
		new Thread() {
			public void run() {
				try {
					Thread.sleep(1200);
					firstBackTime = 0;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
		/*if("3".equals(userLoginInfo.getStatus())){
			UIUtils.showToastSafe("在遥控器界面按退出键结束点播后，按返回退出应用");
		}else{*/
			UIUtils.showToastSafe("再按一次返回退出应用");
		//}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
//		if (id == R.id.item_exit) {
//			exitapp();
//			return true;
//		}
		return super.onOptionsItemSelected(item);
	}

	private void exitapp() {
		/*if("3".equals(userLoginInfo.getStatus())){
			UIUtils.showToastSafe("请先在遥控器界面按退出键结束点播！");
		}else{*/
			finish();
			android.os.Process.killProcess(android.os.Process.myPid());
		//}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (homeFragment.onKeyDown(keyCode, event)) {
			return true;
		} else /*if(getSupportFragmentManager().findFragmentByTag(
				"HOME").equals(homeFragment))*/{
			return super.onKeyDown(keyCode, event);
		}
		/*getSupportFragmentManager().beginTransaction()
		.replace(R.id.home, homeFragment, "HOME").commit();
		return true;*/
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
