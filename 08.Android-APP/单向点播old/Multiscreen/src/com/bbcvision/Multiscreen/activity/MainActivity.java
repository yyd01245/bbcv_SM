package com.bbcvision.Multiscreen.activity;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.bbcvision.Multiscreen.R;
import com.bbcvision.Multiscreen.tools.PackageUtils;

public class MainActivity extends BaseActivity {

	@Override
	protected void initView() {
		super.initView();
		setContentView(R.layout.activity_main);

		loding();

	}

	private void loding() {
		new Thread() {
			private boolean needTS = false;

			public void run() {
				try {
					int value = sp.getInt("ver", 0);
					int new_value = PackageUtils.getVersionCode();
					Log.i("newvar", new_value+"");
					if(new_value > value){
						needTS  = true;
					}
					Editor editor = sp.edit();
					editor.putInt("ver", new_value);
					editor.commit();
					
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Intent intent;
						if(needTS){
							intent = new Intent(MainActivity.this,
									GuideActivity.class);
						}else{
							intent = new Intent(MainActivity.this,
									LoginActivity.class);
						}
						startActivity(intent);

						finish();
					}
				});
			};
		}.start();

	}

	
}
