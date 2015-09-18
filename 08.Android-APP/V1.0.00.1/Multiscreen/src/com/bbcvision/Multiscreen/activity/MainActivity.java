package com.bbcvision.Multiscreen.activity;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.bbcvision.Multiscreen.R;
import com.bbcvision.Multiscreen.configs.NetConfig;
import com.bbcvision.Multiscreen.tools.PackageUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import cn.smssdk.SMSSDK;

public class MainActivity extends BaseActivity {

    @Override
    protected void initView() {
        super.initView();
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();
        setContentView(R.layout.activity_main);
        SMSSDK.initSDK(this, "403e2dced339", "39f935765f8501849632d96ae2cbd737");
        MobclickAgent.openActivityDurationTrack(false);

        String NC_HOST = sp.getString("NC_HOST", null);
        String NC_POST = sp.getString("NC_POST", null);
        if (NC_HOST != null && !("".equals(NC_HOST)) && NC_POST != null && !("".equals(NC_POST))) {
            NetConfig.NC_HOST = NC_HOST;
            NetConfig.NC_POST = NC_POST;
        }

        loding();

    }

    private void loding() {
        new Thread() {
            private boolean needTS = false;

            public void run() {
                try {
                    // 判断版本是否是新版本
                    int value = sp.getInt("ver", 0);
                    int new_value = PackageUtils.getVersionCode();
                    Log.i("newvar", new_value + "");
                    if (new_value > value) {
                        needTS = true;
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
                        if (needTS) {
                            intent = new Intent(MainActivity.this,
                                    GuideActivity.class);
                        } else {
                            intent = new Intent(MainActivity.this,
                                    LoginActivity.class);
                        }
                        startActivity(intent);

                        finish();
                    }
                });
            }

            ;
        }.start();

    }
}
