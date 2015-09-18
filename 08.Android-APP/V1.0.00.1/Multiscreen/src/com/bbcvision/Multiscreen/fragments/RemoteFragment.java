package com.bbcvision.Multiscreen.fragments;

import com.bbcvision.Multiscreen.R;
import com.bbcvision.Multiscreen.bean.ChooseTimeInfo;
import com.bbcvision.Multiscreen.bean.KeySendInfo;
import com.bbcvision.Multiscreen.bean.SessionQueryInfo;
import com.bbcvision.Multiscreen.bean.VodPlayInfo;
import com.bbcvision.Multiscreen.configs.NetConfig;
import com.bbcvision.Multiscreen.netservices.CallbackchooseTime;
import com.bbcvision.Multiscreen.netservices.CallbackkeySend;
import com.bbcvision.Multiscreen.netservices.CallbacksessionQuery;
import com.bbcvision.Multiscreen.netservices.CallbackvodPlay;
import com.bbcvision.Multiscreen.tools.CommonTool;
import com.bbcvision.Multiscreen.tools.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.MobclickAgent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * @author Nestor bbcvision.com
 * @version V1.0
 *          V1.2 1.按钮不要做限制      ok
 *          2.前进和进度条最后5秒不能拖动   ok
 *          3.5秒查一次状态               oK
 *          4.退出后返回web页面           ok
 *          V1.3 快进、快退操作后对应按键灰掉，3S后方可再次操作(体验待确定)
 * @Title: 遥控器页面
 * @Description:
 * @date 2014年10月31日 下午5:30:45
 */
public class RemoteFragment extends BaseFragment implements OnClickListener {
    // 返回按钮后跳转的高标清地址
    //public static String resolutionUrl = "http://192.168.100.11:8882/NSCS/mobile/index.jsp?resolution=";
    //public static String resolutionUrl = NetConfig.RESOLUTIONURL;

    public static String key_type = "1001";

    private View view;
    @ViewInject(R.id.iv_remote_control_tittle)
    private TextView iv_remote_control_tittle;
    @ViewInject(R.id.iv_remote_control_tittleNUM)
    private TextView iv_remote_control_tittleNUM;
    @ViewInject(R.id.iv_remote_control_tittle2)
    private TextView iv_remote_control_tittle2;
    @ViewInject(R.id.btn_remote_control_play_pause)
    private ImageButton btn_remote_control_play_pause;
    @ViewInject(R.id.btn_remote_control_up)
    private ImageButton btn_remote_control_up;
    @ViewInject(R.id.btn_remote_control_dowm)
    private ImageButton btn_remote_control_dowm;
    @ViewInject(R.id.btn_remote_control_left)
    private ImageButton btn_remote_control_left;
    @ViewInject(R.id.btn_remote_control_right)
    private ImageButton btn_remote_control_right;
    @ViewInject(R.id.btn_remote_control_stop)
    private Button btn_remote_control_stop;
    @ViewInject(R.id.btn_remote_control_quik_back)
    private Button btn_remote_control_quik_back;
    @ViewInject(R.id.btn_remote_control_quik_forward)
    private Button btn_remote_control_quik_forward;
    @ViewInject(R.id.btn_remote_control_back)
    private TextView btn_remote_control_back;
    @ViewInject(R.id.btn_remote_control_exit)
    private TextView btn_remote_control_exit;
    @ViewInject(R.id.sb_vodpart)
    private SeekBar sb_vodpart;
    @ViewInject(R.id.tv_vodTime)
    private TextView tv_vodTime;
    @ViewInject(R.id.tv_vodlong)
    private TextView tv_vodlong;

    public boolean isChick = false;
    protected KeySendInfo myKeySendInfo;
    private boolean pause;
    private boolean pause2 = false;
    protected int Total_time;
    protected int Current_time;
    private CountDownTimer countDownTimer;
    private ViewPager viewPager;
    private boolean isExit = false;
    private boolean quikBack = true;
    private boolean quikForward = true;

    private int loadno = 0;

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_remote2, null);
        return view;
    }

    @Override
    public void initData() {
        ViewUtils.inject(this, view);

        tv_vodlong.setText(null);
        tv_vodTime.setText(null);

        btn_remote_control_play_pause.setOnClickListener(this);
        btn_remote_control_up.setOnClickListener(this);
        btn_remote_control_dowm.setOnClickListener(this);
        btn_remote_control_left.setOnClickListener(this);
        btn_remote_control_right.setOnClickListener(this);
        btn_remote_control_stop.setOnClickListener(this);
        btn_remote_control_quik_back.setOnClickListener(this);
        btn_remote_control_quik_forward.setOnClickListener(this);
        btn_remote_control_back.setOnClickListener(this);
        btn_remote_control_exit.setOnClickListener(this);

        sb_vodpart.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());

        //v1.2 5秒查一次状态 (倒计时完成并执行onFinish的时间，onTick间隔时间)
        countDownTimer = new CountDownTimer(1000 * 5, 1000 * 5) {
            @Override
            public void onTick(long millisUntilFinished) {
                getNewState();
                //UIUtils.showToastSafe("onTick间隔时间");
            }

            @Override
            public void onFinish() {
                // 完成后回调可以重复执行
                countDownTimer.start();
            }
        };
        countDownTimer.start();

    }

    public class MyOnSeekBarChangeListener implements OnSeekBarChangeListener {

        private int myProgress;
        private int myStartProgress;

        //在拖动中--即值在改变
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // v1.2 前进和进度条最后5秒不能拖动
            if (progress >= (sb_vodpart.getMax() - 5)) {
                myProgress = sb_vodpart.getMax() - 5;
            } else {
                myProgress = progress;
            }
            tv_vodTime.setText(getTime(progress));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            myStartProgress = sb_vodpart.getProgress();
            //getVolStatus();
            //getNewState();
        }

        //停止拖动
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (!isChick) {

                //UIUtils.showToastSafe("progress:" + myProgress);
                restService.chooseTime(userLoginInfo, myProgress + "",
                        new CallbackchooseTime() {
                            @Override
                            public void callbackChooseTime(
                                    ChooseTimeInfo chooseTimeInfo) {
                                if ("0".equals(chooseTimeInfo.getReturn_code())) {
                                    //sb_vodpart.setProgress(myProgress);
                                } else {
                                    UIUtils.showToastSafe("改变进度失败："
                                            + chooseTimeInfo.getReturn_code());
                                    sb_vodpart.setProgress(myStartProgress);
                                }

                            }
                        });
            } else {
                if ("1".equals(userLoginInfo.getStatus())) {
                    UIUtils.showToastSafe("请绑定频道");
                    sb_vodpart.setProgress(myStartProgress);
                    tv_vodTime.setText(null);
                }
                if ("2".equals(userLoginInfo.getStatus())) {
                    UIUtils.showToastSafe("请点播节目");
                    sb_vodpart.setProgress(myStartProgress);
                    tv_vodTime.setText(null);
                }
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("RemoteFragment"); //统计页面
        getVolStatus();
        if (userLoginInfo.getStream_id() != null) {
            sp.edit().putString("Stream_id", userLoginInfo.getStream_id()).commit();
        } else {
            userLoginInfo.setStream_id(sp.getString("Stream_id", null));
        }
    }

    public void setVolPlayPause() {
        btn_remote_control_play_pause
                .setImageResource(R.drawable.btn_remote_control_pause);
    }

    public void getVolStatus() {

        String status = userLoginInfo.getStatus();
        if ("2".equals(status)) {
            iv_remote_control_tittle.setText("本地频道号：");
            iv_remote_control_tittleNUM.setText(userLoginInfo.getChannel_id());
            iv_remote_control_tittle2.setText("请点播节目");
            isChick = true;
            btn_remote_control_play_pause
                    .setImageResource(R.drawable.btn_remote_control_play);
            pause = true;
        } else if ("3".equals(status)) {
            if (userLoginInfo.getVodname() != null) {
                sp.edit().putString("Vodname", userLoginInfo.getVodname()).commit();
            } else {
                userLoginInfo.setVodname(sp.getString("Vodname", null));
            }
            iv_remote_control_tittle.setText("本地频道号：");
            iv_remote_control_tittleNUM.setText(userLoginInfo.getChannel_id());
            iv_remote_control_tittle2.setText("正在点播 "
                    + userLoginInfo.getVodname());
            isChick = false;
            pause = false;
            if ((tv_vodlong.getText() == null || "".equals(tv_vodlong.getText())) && !isExit) {
                String key_type = "1001";
                String keyId = "0x00";
                keySend(key_type, keyId);
            }

            //setNowVolTime();
        } else {
            iv_remote_control_tittle.setText("请绑定频道后进行点播");
            iv_remote_control_tittleNUM.setText("");
            iv_remote_control_tittle2.setText("");
            isChick = true;
            btn_remote_control_play_pause
                    .setImageResource(R.drawable.btn_remote_control_play);
            pause = true;
        }
    }

    private void setNowVolTime() {
        if (myKeySendInfo != null) {
            int Total_time = 0;
            int Current_time = 1000;
            try {
                if (myKeySendInfo.getTotal_time() != null) {
                    Total_time = (int) Double.parseDouble(myKeySendInfo
                            .getTotal_time());
                    tv_vodlong.setText(getTime(Total_time));
                } else {
                    tv_vodlong.setText(null);
                }
                if (myKeySendInfo.getCurrent_time() != null) {
                    Current_time = (int) Double.parseDouble(myKeySendInfo
                            .getCurrent_time());
                    tv_vodTime.setText(getTime(Current_time));
                } else {
                    tv_vodTime.setText(null);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                tv_vodlong.setText(null);
                tv_vodlong.setText(null);
            } finally {
                sb_vodpart.setMax(Total_time);
                sb_vodpart.setProgress(Current_time);
            }
        }
    }

    @Override
    public void onClick(View v) {
        isExit = false;
        final String keyId;
        getVolStatus();
        getNewState();
        /**
         * V1.2 1.按钮不要做限制
         */
        /*if ("1".equals(userLoginInfo.getStatus())) {
			UIUtils.showToastSafe("请绑定频道");
			return;
		}
		if ("2".equals(userLoginInfo.getStatus())) {
			UIUtils.showToastSafe("请点播节目");
			return;
		}*/
        int id = v.getId();
        switch (id) {
            case R.id.btn_remote_control_play_pause:
                if (pause) {//没有播放
                    btn_remote_control_play_pause
                            .setImageResource(R.drawable.btn_remote_control_play);
                } else {
                    if (pause2) {//正在播放
                        btn_remote_control_play_pause
                                .setImageResource(R.drawable.btn_remote_control_pause);
                        pause2 = false;
                    } else {//暂停中
                        btn_remote_control_play_pause
                                .setImageResource(R.drawable.btn_remote_control_play_2);
                        pause2 = true;
                    }
                }
                keyId = "0x1f";
                keySend(key_type, keyId);
                break;
            case R.id.btn_remote_control_up:
                //LogUtils.i("KeyUp");
                keyId = "0x00";
                keySend(key_type, keyId);
                break;
            case R.id.btn_remote_control_dowm:
                keyId = "0x01";
                keySend(key_type, keyId);
                break;
            case R.id.btn_remote_control_left:
                keyId = "0x03";
                keySend(key_type, keyId);
                break;
            case R.id.btn_remote_control_right:
                keyId = "0x02";
                keySend(key_type, keyId);
                break;
            case R.id.btn_remote_control_stop:
                keyId = "";
                keySend(key_type, keyId);
                break;
            case R.id.btn_remote_control_quik_back:
                keyId = "0x04";

                // V1.3 快进、快退操作后对应按键灰掉，3S后方可再次操作(体验待确定)
                if (quikBack) {
                    keySend(key_type, keyId);
                    btn_remote_control_quik_back.setClickable(false);
                    UIUtils.showToastSafe("操作成功！");
                    //btn_remote_control_quik_back.setBackgroundResource(R.drawable.remote_control_quik_bak_normal2_ds);
                    quikBack = false;
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            UIUtils.runInMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    btn_remote_control_quik_back.setClickable(true);
                                    btn_remote_control_quik_back.setBackgroundResource(R.drawable.selector_remote_control_quik_back);
                                }
                            });
                            quikBack = true;
                        }
                    }.start();
                }
                break;
            case R.id.btn_remote_control_quik_forward:
                keyId = "0x08";
                // V1.3 快进、快退操作后对应按键灰掉，3S后方可再次操作(体验待确定)
                if (quikForward) {
                    btn_remote_control_quik_forward.setClickable(false);
                    UIUtils.showToastSafe("操作成功！");
                    //btn_remote_control_quik_forward.setBackgroundResource(R.drawable.remote_control_quik_go_normal2_ds);
                    quikForward = false;
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            UIUtils.runInMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    btn_remote_control_quik_forward.setClickable(true);
                                    btn_remote_control_quik_forward.setBackgroundResource(R.drawable.selector_remote_control_quik_go);
                                }
                            });

                            quikForward = true;
                        }
                    }.start();
                    if ((Current_time != 0) && (Total_time != 0)
                            && Current_time >= (Total_time - 5)) {
                        break;
                    }
                    keySend(key_type, keyId);
                }
                break;
            case R.id.btn_remote_control_back:
                // 返回resolution
                //if ("0x1c".equals(keyId)){
                String resolution = sp.getString("resolution","1");
                if (viewPager != null && sp.getString("homeurl",null)!=null) {
                   /* userLoginInfo.setUrl(*//*resolutionUrl*//*userLoginInfo.getUrl()+"&resolution="+resolution+"&username="
                            + userLoginInfo.getUsername()+"&streamid"+userLoginInfo.getStream_id());*/
                    userLoginInfo.setUrl(sp.getString("homeurl",null)+"&resolution="+resolution+"&username="
                            + userLoginInfo.getUsername()+"&streamid="+userLoginInfo.getStream_id()+"&loadno="+(loadno++));
                    LogUtils.e(userLoginInfo.getUrl());
                    viewPager.setCurrentItem(1);
                }
                //}
                keyId = "0x1c";
                keySend(key_type, keyId);
                break;
            case R.id.btn_remote_control_exit:
                isExit = true;
                keyId = "0x1d";
                keySend(key_type, keyId);
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        UIUtils.runInMainThread(new Runnable() {
                            @Override
                            public void run() {
                                needExit();
                            }
                        });

                    }
                }.start();

                break;
        }

    }

    private void needExit() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("退出播放");
        builder.setMessage("确定退出播放？");
        builder.setCancelable(false);
        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                keySend(key_type, "0x1d");
                // V1.2 退出成功返回web页面
                //if(!"3".equals(userLoginInfo.getStatus())){
                //userLoginInfo.getService_url();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        UIUtils.runInMainThread(new Runnable() {
                            @Override
                            public void run() {
                                String resolution = sp.getString("resolution","1");
                                userLoginInfo.setUrl(sp.getString("homeurl",null)+"&resolution="+resolution+"&username="
                                        + userLoginInfo.getUsername()+"&streamid="+userLoginInfo.getStream_id()+"&loadno="+(loadno++));
                                if (viewPager != null) {
                                    viewPager.setCurrentItem(1);
                                }
                            }
                        });

                    }
                }.start();
               // }
                isExit = false;
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                keySend(key_type, "0x00");
                isExit = false;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();// 显示对话框
    }

    private void keySend(String key_type, final String keyId) {
        LogUtils.i("############"+keyId+"############");
        if (!isChick) {
            isChick = true;
            restService.keySend(userLoginInfo, key_type, keyId,
                    new CallbackkeySend() {
                        @Override
                        public void callbackKeySend(KeySendInfo keySendInfo) {
                            if ("0".equals(keySendInfo.getReturn_code())) {
                                myKeySendInfo = keySendInfo;
                                userLoginInfo.setStatus(keySendInfo.getStatus());
                                //Log.i("keySendInfoStatus", keySendInfo.getStatus());
                               // Log.i("getTotal_time",
                                 //       keySendInfo.getTotal_time());
                                LogUtils.i( keySendInfo.getTotal_time());
                                LogUtils.i( keySendInfo.getStatus());


                                Total_time = 1000;
                                Current_time = 0;
                                try {
                                    if (keySendInfo.getTotal_time() != null) {
                                        Total_time = (int) Double
                                                .parseDouble(keySendInfo
                                                        .getTotal_time());
                                        tv_vodlong.setText(getTime(Total_time));
                                    } else {
                                        tv_vodlong.setText(" ");
                                    }
                                    if (keySendInfo.getCurrent_time() != null) {
                                        Current_time = (int) Double
                                                .parseDouble(keySendInfo
                                                        .getCurrent_time());

                                        tv_vodTime
                                                .setText(getTime(Current_time));
                                    } else {
                                        tv_vodTime.setText(" ");
                                    }
                                } catch (NumberFormatException e) {
                                    tv_vodlong.setText(" ");
                                    tv_vodTime.setText(" ");
                                    e.printStackTrace();
                                } finally {
                                    if (Total_time > 0) {
                                        sb_vodpart.setMax(Total_time);
                                    }

                                    if (Current_time >= 0) {
                                        sb_vodpart.setProgress(Current_time);
                                    }
                                    getVolStatus();
                                }
                            } else {
                                UIUtils.showToastSafe(keySendInfo.getMsg()
                                        + keySendInfo.getReturn_code());
                                isChick = false;
                            }
                        }
                    });
        }
    }

    public void setViewPager(ViewPager mPager) {
        viewPager = mPager;
    }

    public void getNewState() {
        if ("1".equals(userLoginInfo.getStatus())) {
            return;
        }
        // 判断是否绑定超时
        restService.sessionQuery(userLoginInfo, new CallbacksessionQuery() {
            @Override
            public void callbackSessionQuery(SessionQueryInfo sessionQueryInfo) {
                if ("0".equals(sessionQueryInfo.getReturn_code())) {
                    userLoginInfo.setStatus(sessionQueryInfo.getStatus());
                    getVolStatus();
                }
            }
        });
    }

    public void setTvText() {
        tv_vodlong.setText(null);
        tv_vodTime.setText(null);
    }

    public String getTime(int time) {
        int fen = time / 60;
        int miao = (time % 60);

        if (fen >= 1) {
            return fen + ":" + miao;
        } else {
            return miao + "";
        }
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("RemoteFragment");
    }
}
