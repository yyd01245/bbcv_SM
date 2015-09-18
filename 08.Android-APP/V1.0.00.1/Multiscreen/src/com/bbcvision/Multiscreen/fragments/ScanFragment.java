package com.bbcvision.Multiscreen.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bbcvision.Multiscreen.R;
import com.bbcvision.Multiscreen.bean.ErweimaInfo;
import com.bbcvision.Multiscreen.bean.SessionQueryInfo;
import com.bbcvision.Multiscreen.bean.UnBindInfo;
import com.bbcvision.Multiscreen.bean.UserBingInfo;
import com.bbcvision.Multiscreen.bean.UserLoginInfo;
import com.bbcvision.Multiscreen.netservices.CallbackbindUesr;
import com.bbcvision.Multiscreen.netservices.Callbackeweima;
import com.bbcvision.Multiscreen.netservices.CallbacksessionQuery;
import com.bbcvision.Multiscreen.netservices.CallbackunBind;
import com.bbcvision.Multiscreen.netservices.JSONService;
import com.bbcvision.Multiscreen.netservices.RESTService;
import com.bbcvision.Multiscreen.tools.UIUtils;
import com.bbcvision.zxingdemonew.CaptureActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.MobclickAgent;

/**
 * @author Nestor bbcvision.com
 * @version V1.0
 * @Title: 扫描页面
 * @Description:
 * @date 2014-10-14 上午9:20:02
 */
public class ScanFragment extends BaseFragment implements OnClickListener {

    private View view;
    @ViewInject(R.id.scan_bt)
    private RelativeLayout scan_bt;
    @ViewInject(R.id.ll_bdf)
    private LinearLayout ll_bdf;
    @ViewInject(R.id.ll_bdt)
    private LinearLayout ll_bdt;
    @ViewInject(R.id.tv_bdid)
    private TextView tv_bdid;
    @ViewInject(R.id.tv_unbind)
    private RelativeLayout tv_unbind;
    private String scanMsg;
    private ViewPager viewPager;
    //private ErweimaInfo erweimaInfo;

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_scan, null);
        return view;
    }

    @Override
    public void initData() {
        ViewUtils.inject(this, view);

        tv_unbind.setOnClickListener(this);
        tv_unbind.setClickable(true);

        scan_bt.setOnClickListener(this);
        /*scan_bt.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					//手指按到了控件上
					scan_bt.setBackgroundResource(R.drawable.scan_bt_2);
					break;
				case MotionEvent.ACTION_MOVE:
					
					break;
					
				case MotionEvent.ACTION_UP:
					// 手指离开
					scan_bt.setBackgroundResource(R.drawable.scan_bt_1);
					break;
				}
				return false;
			}
		});*/
    }

    /**
     * 是否绑定
     *
     * @return
     */
    private Boolean isBind() {
        if ("1".equals(userLoginInfo.getStatus())) {
            return false;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ScanFragment"); //统计页面
        getScanState();
    }

    private void getScanState() {

        // 得到绑定信息
        scanMsg = sp.getString("scanMsg", null);

        Editor ed = sp.edit();
        if (scanMsg != null) {
            LogUtils.i(scanMsg);
            //clearUsername();
            // 新二维码
            /*RESTService.getEweimaInfo(scanMsg, new Callbackeweima() {
                @Override
                public void callbackEweima(ErweimaInfo erweimaInfo) {
                    if ("0".equals(erweimaInfo.getRetcode())) {
                        toBind(erweimaInfo);
                    } else {
                        UIUtils.showToastSafe("获取绑定信息失败 " + erweimaInfo.getRetcode());
                    }
                }
            });*/

            // 旧二维码
            List<String> msgs = new ArrayList<String>();
            msgs.add(0, null);
            msgs.add(1, null);
            try {
                msgs = cutURL(scanMsg);
            } catch (Exception e) {

                e.printStackTrace();
            }
            if (msgs != null) {
                toBind_old(msgs);
            } else {
                // 新二维码
                RESTService.getEweimaInfo(scanMsg, new Callbackeweima() {
                    @Override
                    public void callbackEweima(ErweimaInfo erweimaInfo) {
                        if ("0".equals(erweimaInfo.getRetcode())) {
                            toBind(erweimaInfo);
                        } else {
                            UIUtils.showToastSafe("获取绑定信息失败 " + erweimaInfo.getRetcode());
                        }
                    }
                });
                UIUtils.showToastSafe("微信二维码！" );
            }


            ed.putString("scanMsg", null).commit();
        } else {
            if (isBind()) {
                ll_bdf.setVisibility(View.GONE);
                ll_bdt.setVisibility(View.VISIBLE);
                tv_bdid.setText(userLoginInfo.getChannel_id());
            } else {
                ll_bdf.setVisibility(View.VISIBLE);
                ll_bdt.setVisibility(View.GONE);
            }

            ed.putString("scanMsg", null).commit();
        }
    }

    private void toBind(final ErweimaInfo erweimaInfo) {
        try {
            int index = erweimaInfo.getUrl().indexOf("/", 8);
            //将字符串转为字符数组
            char[] ch = erweimaInfo.getUrl().toCharArray();
            //根据 copyValueOf(char[] data, int offset, int count) 取得最后一个字符串
            String  msg = String.copyValueOf(ch, 0, index);
            //final String url = /*msgs.get(0);*/ erweimaInfo.getUrl();
            final String url = msg;
            final String stream_id = /*msgs.get(1);*/erweimaInfo.getMsg().getChannel();
            final String resolution = /*msgs.get(2);*/erweimaInfo.getMsg().getVodid();
            sp.edit().putString("resolution", resolution).commit();

            restService.bindUesr(userLoginInfo, stream_id, url,
                    new CallbackbindUesr() {

                        @Override
                        public void callBackbindUesr(UserBingInfo BingInfo) {
                            userBingInfo = BingInfo;
                            //LogUtils.i("getReturn_code", userBingInfo.getReturn_code());
                            if ("0".equals(userBingInfo.getReturn_code())) {

                                try {
                                    HttpUtils http = new HttpUtils();
                                    http.send(
                                            HttpRequest.HttpMethod.GET,
                                            erweimaInfo.getUrl() + "&username="
                                                    + userLoginInfo.getUsername(),
                                            null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                // LogUtils.i( scanMsg + "&username=" + userLoginInfo.getUsername());

                                ll_bdf.setVisibility(View.GONE);
                                ll_bdt.setVisibility(View.VISIBLE);
                                userLoginInfo.setNew_token(userBingInfo
                                        .getNew_token());
                                userLoginInfo.setStatus(userBingInfo
                                        .getStatus());
                                userLoginInfo.setChannel_id(userBingInfo
                                        .getChannel_id());
                                userLoginInfo.setStream_id(stream_id);
                                userLoginInfo.setUrl(erweimaInfo.getUrl() + "&username="
                                        + userLoginInfo.getUsername());

                               /* LogUtils.e("############"+erweimaInfo.getUrl() + "&username="
                                        + userLoginInfo.getUsername());*/
                                sp.edit().putString("Channel_id", userBingInfo.getChannel_id()).commit();
                                tv_bdid.setText(userBingInfo.getChannel_id());
                                tv_unbind.setClickable(true);
                                if (viewPager != null) {
                                    viewPager.setCurrentItem(1);
                                }
                            } else {
                                UIUtils.showToastSafe("绑定失败("
                                        + userBingInfo.getMsg() + ")");
                                LogUtils.e(stream_id + ":" + resolution + ":" + url);
                                ll_bdf.setVisibility(View.VISIBLE);
                                ll_bdt.setVisibility(View.GONE);
                            }

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            UIUtils.showToastSafe("二维码参数变了，没解析出来！");
        }
    }

    private void toBind_old(List<String> msgs) {
        try {
            final String url = msgs.get(0);
            final String stream_id = msgs.get(1);
            final String resolution = msgs.get(2);
            sp.edit().putString("resolution", resolution).commit();

            restService.bindUesr(userLoginInfo, stream_id, url,
                    new CallbackbindUesr() {

                        @Override
                        public void callBackbindUesr(UserBingInfo BingInfo) {
                            userBingInfo = BingInfo;
                            //LogUtils.i("getReturn_code", userBingInfo.getReturn_code());
                            if ("0".equals(userBingInfo.getReturn_code())) {

                                try {
                                    HttpUtils http = new HttpUtils();
                                    http.send(
                                            HttpRequest.HttpMethod.GET,
                                            scanMsg + "&username="
                                                    + userLoginInfo.getUsername(),
                                            null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                // LogUtils.i( scanMsg + "&username=" + userLoginInfo.getUsername());

                                ll_bdf.setVisibility(View.GONE);
                                ll_bdt.setVisibility(View.VISIBLE);
                                userLoginInfo.setNew_token(userBingInfo
                                        .getNew_token());
                                userLoginInfo.setStatus(userBingInfo
                                        .getStatus());
                                userLoginInfo.setChannel_id(userBingInfo
                                        .getChannel_id());
                                userLoginInfo.setStream_id(stream_id);
                                userLoginInfo.setUrl(scanMsg + "&username="
                                        + userLoginInfo.getUsername());
                                sp.edit().putString("Channel_id", userBingInfo.getChannel_id()).commit();
                                tv_bdid.setText(userBingInfo.getChannel_id());
                                tv_unbind.setClickable(true);
                                if (viewPager != null) {
                                    viewPager.setCurrentItem(1);
                                }
                            } else {
                                UIUtils.showToastSafe("绑定失败("
                                        + userBingInfo.getMsg() + ")");
                                LogUtils.e(stream_id + ":" + resolution + ":" + url);
                                ll_bdf.setVisibility(View.VISIBLE);
                                ll_bdt.setVisibility(View.GONE);
                            }

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            // 新二维码
            RESTService.getEweimaInfo(scanMsg, new Callbackeweima() {
                @Override
                public void callbackEweima(ErweimaInfo erweimaInfo) {
                    if ("0".equals(erweimaInfo.getRetcode())) {
                        toBind(erweimaInfo);
                    } else {
                        UIUtils.showToastSafe("获取绑定信息失败 " + erweimaInfo.getRetcode());
                    }
                }
            });
            UIUtils.showToastSafe("微信二维码！" );
        }
    }

    // TODO 写死了
    private void clearUsername() {
        HttpUtils http = new HttpUtils();
        http.send(
                HttpRequest.HttpMethod.GET,
                "http://192.168.100.11:8882/NSCS/ClearVodidServlet?username="
                        + userLoginInfo.getUsername(),
                null);
    }

    /**
     * 分割URL:http://192.168.100.11:8882/NSCS/a?id=3&vp=10&r=0
     *
     * @param scanMsg
     * @return
     */
    private List<String> cutURL(String scanMsg) {
        List<String> msgs = new ArrayList<String>();
        //取得/的下标
        int index = scanMsg.indexOf("/", 8);
        //将字符串转为字符数组
        char[] ch = scanMsg.toCharArray();
        //根据 copyValueOf(char[] data, int offset, int count) 取得最后一个字符串
        String msg = String.copyValueOf(ch, 0, index);
        msgs.add(0, msg);//http://192.168.100.11:8882

        Matcher msg2 = Pattern.compile("id=(.*?)&").matcher(scanMsg);
        while (msg2.find()) {
            msgs.add(1, msg2.group(1));
        }

        //取得最后一个/的下标
        int index2 = scanMsg.lastIndexOf("=");
        //将字符串转为字符数组
        char[] ch2 = scanMsg.toCharArray();
        //根据 copyValueOf(char[] data, int offset, int count) 取得最后一个字符串
        String msg3 = String.copyValueOf(ch2, index2 + 1, ch2.length - index2
                - 1);
        msgs.add(2, msg3);

        return msgs;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.scan_bt:
                startActivity(new Intent(getActivity(), CaptureActivity.class));
                break;
            case R.id.tv_unbind:
                Editor ed = sp.edit();
                ed.putString("scanMsg", null);
                ed.commit();
                if ("3".equals(userLoginInfo.getStatus())) {
                    UIUtils.showToastSafe("正在点播无法解除绑定");
                    tv_unbind.setClickable(true);
                    break;
                }
                tv_unbind.setClickable(false);
                restService.unBind(userLoginInfo, new CallbackunBind() {

                    @Override
                    public void callbackUnBind(UnBindInfo unBindInfo) {
                        if ("0".equals(unBindInfo.getReturn_code())) {
                            ll_bdf.setVisibility(View.VISIBLE);
                            ll_bdt.setVisibility(View.GONE);
                            UIUtils.showToastSafe("解绑成功");
                            userLoginInfo.setNew_token(unBindInfo.getNew_token());
                            userLoginInfo.setStatus(unBindInfo.getStatus());
                            tv_unbind.setClickable(false);
                        } else {
                            UIUtils.showToastSafe("解绑失败");
                            ll_bdf.setVisibility(View.GONE);
                            ll_bdt.setVisibility(View.VISIBLE);
                            tv_unbind.setClickable(true);
                            getNewState();
                        }
                    }
                });
                break;
        }
    }

    public void getNewState() {
        restService.sessionQuery(userLoginInfo, new CallbacksessionQuery() {

            @Override
            public void callbackSessionQuery(SessionQueryInfo sessionQueryInfo) {
                if ("0".equals(sessionQueryInfo.getReturn_code())) {
                    userLoginInfo.setStatus(sessionQueryInfo.getStatus());
                    getScanState();
                }
            }
        });
    }

    public void setViewPager(ViewPager mPager) {
        viewPager = mPager;
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ScanFragment");
    }

    @Override
    public void onStart() {
        super.onStart();
        tv_bdid.setText(userBingInfo.getChannel_id());
    }
}
