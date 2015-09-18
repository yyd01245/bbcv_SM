package com.bbcvision.Multiscreen.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.webkit.WebView;

public class MyWebView extends WebView {

	private float downX;
	private float downY;

	public MyWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyWebView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/*@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 得到父类，请求不要拦截----在滚动图片上touch事件响应自身的左右滑动，不响应下拉刷新。
			getParent().requestDisallowInterceptTouchEvent(true);
			
			downX = ev.getX();
			downY = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			float moveX = ev.getX();
			float moveY = ev.getY();
			// 如果x轴位移大，就响应父组件的touch事件
			if (Math.abs(downX - moveX) > Math.abs(downY - moveY)) {
				// 得到父类，请求拦截
				getParent().requestDisallowInterceptTouchEvent(false);
			} else {
				// 得到父类，请求不要拦截
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			break;
		case MotionEvent.ACTION_UP:
			
			break;
		}
		if(canScrollHor(SCROLLBAR_POSITION_RIGHT)){
			return true;
		}else{
			return true;
		}
		//getParent().requestDisallowInterceptTouchEvent(true);
		return super.dispatchTouchEvent(ev);
	}*/

	/**
	 * touch事件在不能左右滑动式传递给父控件
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		int screenHeight = display.getHeight();
		if (canScrollHor(SCROLLBAR_POSITION_RIGHT)) {
			getParent().requestDisallowInterceptTouchEvent(true);
			return true;
		} else {
			if(ev.getY()<(screenHeight/3)){
				getParent().requestDisallowInterceptTouchEvent(true);
				return true;
			}
			getParent().requestDisallowInterceptTouchEvent(false);
			return false;
		}
	/*	if (getScrollX()<0) {
		 	getParent().requestDisallowInterceptTouchEvent(false);
		     return false;
		 } else if (getScrollX() >= computeHorizontalScrollRange()) {  //- getWidth()
		 	getParent().requestDisallowInterceptTouchEvent(false);
		     return false;
		 } else {
		     getParent().requestDisallowInterceptTouchEvent(true);  //子view中,可以中断pager获取到事件
		     return true;
		 }*/
		//return super.onInterceptTouchEvent(ev);
	}

	public boolean canScrollHor(int direction) {
		final int offset = computeHorizontalScrollOffset();
		final int range = computeHorizontalScrollRange()
				- computeHorizontalScrollExtent();
		if (range == 0)
			return false;
		if (direction < 0) {
			return offset > 0;
		} else {
			return offset < range - 1;
			//return ( direction < 0 ) ? ( offset > 0 ) : ( offset < range - 1 );
		}
	}
}
