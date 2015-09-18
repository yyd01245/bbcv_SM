package com.bbcvision.Multiscreen.view;

import com.bbcvision.Multiscreen.R;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class MyViewPager extends ViewPager {

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {

		/*MyWebView view = (MyWebView) v.findViewById(R.id.wv_home); //res ID
		if (view != null) {
			return view.canScrollHor(-dx);
		} else {
			return super.canScroll(v, checkV, dx, x, y);
		}*/
		
		if (v instanceof MyWebView) {
            return ((MyWebView) v).canScrollHor(-dx);
        } else {
            return super.canScroll(v, checkV, dx, x, y);
        }

	}
}
