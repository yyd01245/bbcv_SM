package com.bbcvision.myzxing.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
/**
 * 
 * @Title: 自定义ViewPager
 *		
 * @Description: 
 *		本控件不响应触摸事件。
 *		继承重新的ViewPager：LazyViewPager使ViewPager只加载当前页
 * @author Nestor blog.yzapp.cn 
 *		
 * @date 2014年8月28日 下午8:05:07
 *		
 * @version V1.0  
 *
 */
public class MyViewPager extends LazyViewPager {

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public MyViewPager(Context context) {
		super(context);
		/*this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});*/
	}
	
	// onTouch优先于onTouchEvent执行
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		/*switch (arg0.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			break;
		}*/
		
		//return super.onTouchEvent(arg0);
		// 返回false,本控件不响应触摸事件；return super.onTouchEvent(arg0)：交给父控件处理
		return false;
	}
	
	// 最早执行，然后执行onTouchEvent；onTouch和onTouchEvent在dispatchTouchEvent内部。
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}
	
	// 拦截触摸事件
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// 如果返回true，拦截所有触摸事件
		return super.onInterceptTouchEvent(arg0);
	}
	
}
