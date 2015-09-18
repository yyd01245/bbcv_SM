package com.bbcvision.Multiscreen.fragments;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.bbcvision.Multiscreen.R;
import com.bbcvision.Multiscreen.view.FixedSpeedScroller;
import com.bbcvision.Multiscreen.view.MyViewPager;
import com.bbcvision.Multiscreen.view.MyWebView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * @author Nestor bbcvision.com
 * @version V1.0
 * @Title: 滑动页面
 * @Description:
 * @date 2014-10-14 上午9:09:25
 */
public class HomeFragment extends BaseFragment {

    private View view;
    private MyViewPager mPager;
    private ImageView iv_page;
    private RemoteFragment remoteFragment;
    private WebFragment webFragment;
    private ImageView im_menu;
    private SlidingMenu mysm;
    private ScanFragment scanFragment;
    private FixedSpeedScroller mScroller;

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.frag_home, null);
        return view;
    }

    public void setSlidingMenu(SlidingMenu sm) {
        mysm = sm;
    }

    @Override
    public void initData() {
        iv_page = (ImageView) view.findViewById(R.id.iv_page);

        im_menu = (ImageView) view.findViewById(R.id.im_menu);
        im_menu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mysm.toggle();
            }
        });

        InitViewPager();
    }

    private void InitViewPager() {
        mPager = (MyViewPager) view.findViewById(R.id.home_view);

        ArrayList<Fragment> fragmentArray = new ArrayList<Fragment>();
        scanFragment = new ScanFragment();
        fragmentArray.add(scanFragment);
        webFragment = new WebFragment();
        fragmentArray.add(webFragment);
        remoteFragment = new RemoteFragment();
        fragmentArray.add(remoteFragment);
        webFragment.getRemoteFragment(remoteFragment);
        
        mPager.setAdapter(new FragmentAdapter(getActivity()
                .getSupportFragmentManager(), fragmentArray));
        mPager.setCurrentItem(0);
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            //设置加速度 ，通过改变FixedSpeedScroller这个类中的mDuration来改变动画时间（如mScroller.setmDuration(mMyDuration);）
            mScroller = new FixedSpeedScroller(mPager.getContext(),
                    new AccelerateInterpolator());
            mField.set(mPager, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());

        scanFragment.setViewPager(mPager);
        webFragment.setViewPager(mPager);
        remoteFragment.setViewPager(mPager);
    }

    /**
     * 初始化动画
     */
/*	private void InitImageView() {
        // cursor = (ImageView) findViewById(R.id.active_bar);

		// bmpW = BitmapFactory.decodeResource(getResources(),
		// R.drawable.a).getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		// m_screenW = screenW;
		// offset = (screenW / nTabCnt - bmpW) / 2f;// 计算偏移量
	}*/

    /**
     * 页卡切换监听
     */
    public class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            if (arg0 == 1) {
                iv_page.setImageResource(R.drawable.bottom_page_2);
                webFragment.webHtml();
                // 侧边滑动TOUCHMODE_MARGIN
                mysm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
            }else if (arg0 == 11) {
                iv_page.setImageResource(R.drawable.bottom_page_2);
                // 不改变页面地址
                //webFragment.webHtml();
                // 侧边滑动TOUCHMODE_MARGIN
                mysm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
            } else if (arg0 == 2) {
                iv_page.setImageResource(R.drawable.bottom_page_3);
                remoteFragment.setTvText();
                remoteFragment.getVolStatus();
                remoteFragment.getNewState();
                mysm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
            } else {
                iv_page.setImageResource(R.drawable.bottom_page_1);
                scanFragment.getNewState();
                mysm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
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

    @Override
    public void setDisplay(Display display) {
        super.setDisplay(display);
    }

}
