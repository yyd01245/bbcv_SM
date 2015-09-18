package com.bbcvision.Multiscreen.activity;

import android.telephony.TelephonyManager;
import android.transition.ArcMotion;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.bbcvision.Multiscreen.R;
import com.bbcvision.Multiscreen.tools.PackageUtils;
import com.bbcvision.Multiscreen.tools.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

/**
 * @Title: 关于
 * @Description:
 * @author Nestor bbcvision.com
 * @date 2014-10-31 上午10:36:00
 * @version V1.0
 */
public class AboutActivity extends BaseActivity {

	@ViewInject(R.id.tv_update)
	private TextView tv_update;
	
	@Override
	protected void initView() {
		super.initView();
        PushAgent.getInstance(this).onAppStart();
		setContentView(R.layout.activity_about);

        //String num = getPhoneNumber();

		((TextView)findViewById(R.id.tv_ver)).setText("版本："+PackageUtils.getVersionName());

		
		ViewUtils.inject(this);
		
		tv_update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				UmengUpdateAgent.forceUpdate(AboutActivity.this);
			}
		});

        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
                switch (updateStatus) {
                    case UpdateStatus.Yes: // has update
                        //UmengUpdateAgent.showUpdateDialog(mContext, updateInfo);
                        break;
                    case UpdateStatus.No: // has no update
                        UIUtils.showToastSafe("木有新版本~");
                        break;
                    case UpdateStatus.NoneWifi: // none wifi
                        break;
                    case UpdateStatus.Timeout: // time out
                        UIUtils.showToastSafe("请链接网络后重试");
                        break;
                }
            }
        });
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("AboutActivity"); //统计页面
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("AboutActivity"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

    private String getPhoneNumber(){
        TelephonyManager mTelephonyMgr;
        mTelephonyMgr = (TelephonyManager)  getSystemService(TELEPHONY_SERVICE);
        return mTelephonyMgr.getLine1Number();
        //return mTelephonyMgr.getSubscriberId();

    }

}
