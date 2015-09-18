package com.bbcvision.Multiscreen.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.bbcvision.Multiscreen.R;
import com.bbcvision.Multiscreen.activity.AboutActivity;
import com.bbcvision.Multiscreen.activity.AlipayTest;
import com.bbcvision.Multiscreen.activity.HelpActivity;
import com.bbcvision.Multiscreen.activity.HomeActivity2;
import com.bbcvision.Multiscreen.activity.LoginActivity;
import com.bbcvision.Multiscreen.activity.UserActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @author Nestor blog.yzapp.cn
 * @version V1.0
 * @Title: 侧滑菜单
 * @Description:
 * @date 2014年8月29日 下午4:40:23
 */
public class MenuFragment extends BaseFragment implements OnClickListener {

    private View view;
    @ViewInject(R.id.tv_user)
    private TextView tv_user;
    @ViewInject(R.id.tv_about)
    private TextView tv_about;
    @ViewInject(R.id.tv_help)
    private TextView tv_help;
    @ViewInject(R.id.tv_alipay)
    private TextView tv_alipay;

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.layout_left_menu, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        tv_user.setOnClickListener(this);
        tv_about.setOnClickListener(this);
        tv_help.setOnClickListener(this);
        tv_alipay.setOnClickListener(this);
    }

    public void setFragment(Fragment fragment) {
        ((HomeActivity2) getActivity()).switchFragment(fragment);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        int id = v.getId();
        switch (id) {
            case R.id.tv_user:
                ((HomeActivity2) getActivity()).toggle();
                //intent = new Intent(getActivity(), LoginActivity.class);
                intent = new Intent(getActivity(), UserActivity.class);
                intent.putExtra("userLoginInfo", userLoginInfo);
                startActivity(intent);
                //getActivity().finish();
                break;
            case R.id.tv_about:
                // setFragment(new AboutFragment());
                ((HomeActivity2) getActivity()).toggle();
                intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_help:
                // setFragment(new HelpFragment());
                ((HomeActivity2) getActivity()).toggle();
                intent = new Intent(getActivity(), HelpActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_alipay:
                ((HomeActivity2) getActivity()).toggle();
                intent = new Intent(getActivity(), AlipayTest.class);
                startActivity(intent);
                break;
        }
    }

}
