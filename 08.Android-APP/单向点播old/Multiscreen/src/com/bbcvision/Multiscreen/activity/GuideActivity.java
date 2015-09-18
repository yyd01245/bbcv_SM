package com.bbcvision.Multiscreen.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bbcvision.Multiscreen.R;

public class GuideActivity extends BaseActivity {
	Button button;
	@Override
	protected void initView() {
		super.initView();
		setContentView(R.layout.activity_guide);
		
		ViewPager view_pager = (ViewPager) findViewById(R.id.view_pager);
		button = (Button) findViewById(R.id.guide_bt);
		List<View> lists = new ArrayList<View>();
		ImageView imageView1 = new ImageView(this);
		imageView1.setBackgroundResource(R.drawable.guide_01);
		ImageView imageView2 = new ImageView(this);
		imageView2.setBackgroundResource(R.drawable.guide_02);
		ImageView imageView3 = new ImageView(this);
		imageView3.setBackgroundResource(R.drawable.guide_03);
		
		lists.add(imageView1);
		lists.add(imageView2);
		lists.add(imageView3);
		
		ViewPagerAdapter adapter = new ViewPagerAdapter(lists);
		
		view_pager.setAdapter(adapter);
		
		view_pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if(position == 2){
					button.setVisibility(View.VISIBLE);
				}else{
					button.setVisibility(View.GONE);
				}
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(GuideActivity.this, LoginActivity.class));
				finish();
			}
		});
	}
	
	private class ViewPagerAdapter extends PagerAdapter {
		private List<View> pages;
		public ViewPagerAdapter(List<View> lists) {
			this.pages = lists;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager)container).removeView(pages.get(position));
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((ViewPager)container).addView(pages.get(position), 0);
			return pages.get(position);
		}
		@Override
		public int getCount() {
			return pages.size();
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}
}
