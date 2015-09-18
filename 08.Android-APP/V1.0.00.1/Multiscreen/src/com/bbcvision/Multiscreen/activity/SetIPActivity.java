package com.bbcvision.Multiscreen.activity;/**
 * @Title:
 * @Description:
 * @Author: Nestor bbcvision.com 
 * @Date: 2014-11-10 11:03 
 * @Version V1.0
 */


import android.view.View;
import android.widget.EditText;

import com.bbcvision.Multiscreen.R;

import com.bbcvision.Multiscreen.configs.NetConfig;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.message.PushAgent;


/**
 * Created by kysx_100 on 14/11/10.
 */
public class SetIPActivity extends BaseActivity {

    @ViewInject(R.id.editText)
    private EditText ed_ip;
    @ViewInject(R.id.editText2)
    private EditText ed_post;

    @Override
    protected void initView() {
        super.initView();
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.activity_setip);
        ViewUtils.inject(this);
    }

    @OnClick(R.id.bt_ok)
    public void clickPay(View v) {
        sp.edit().putString("NC_HOST",ed_ip.getText().toString().trim()).commit();
        sp.edit().putString("NC_POST",ed_post.getText().toString().trim()).commit();
        if(!"".equals(ed_ip.getText().toString().trim())&&!"".equals(ed_post.getText().toString().trim())){
            NetConfig.NC_HOST=ed_ip.getText().toString().trim();
            NetConfig.NC_POST=ed_post.getText().toString().trim();
        }
        finish();
    }


}
