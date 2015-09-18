package com.bbcvision.Multiscreen.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
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
import com.bbcvision.Multiscreen.bean.UnBindInfo;
import com.bbcvision.Multiscreen.bean.UserBingInfo;
import com.bbcvision.Multiscreen.bean.UserLoginInfo;
import com.bbcvision.Multiscreen.netservices.CallbackbindUesr;
import com.bbcvision.Multiscreen.netservices.CallbackunBind;
import com.bbcvision.Multiscreen.tools.UIUtils;
import com.bbcvision.myzxing.CaptureActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.client.HttpRequest;

public class ScanFragment extends BaseFragment implements OnClickListener{

	private View view;
	private RelativeLayout scan_bt;
	private TextView txt1;
	private LinearLayout ll_bdf;
	private LinearLayout ll_bdt;
	private TextView tv_bdid;
	private TextView tv_unbind;
	private String scanMsg;

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.fragment_scan, null);
		return view;
	}

	@Override
	public void initData() {
		ll_bdf = (LinearLayout) view.findViewById(R.id.ll_bdf);
		ll_bdt = (LinearLayout) view.findViewById(R.id.ll_bdt);
		
		tv_unbind = (TextView) view.findViewById(R.id.tv_unbind);
		tv_unbind.setOnClickListener(this);
		
		tv_bdid = (TextView) view.findViewById(R.id.tv_bdid);
		
		scan_bt = (RelativeLayout) view.findViewById(R.id.scan_bt);
		scan_bt.setOnClickListener(this);
		scan_bt.setOnTouchListener(new OnTouchListener() {
			
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
		});
	}

	
	private Boolean isBind() {
		if("1".equals(userLoginInfo.getStatus())){
			return false;
		}
		return true;
	}

	@Override
	public void onResume() {
		
		// 得到绑定信息
		scanMsg = sp.getString("scanMsg", null);
		//Log.i("scanMsg",scanMsg);
		Editor ed = sp.edit();
		if(scanMsg!=null){
			
			List<String> msgs = cutURL(scanMsg);
			
			final String url = msgs.get(0);
			final String stream_id = msgs.get(1);
			
			restService.bindUesr(userLoginInfo, stream_id, url, new CallbackbindUesr() {
				
				@Override
				public void callBackbindUesr(UserBingInfo BingInfo) {
					userBingInfo = BingInfo;
					//Log.i("getReturn_code", userBingInfo.getReturn_code());
					if("0".equals(userBingInfo.getReturn_code())){
						
						

						ll_bdf.setVisibility(View.GONE);
						ll_bdt.setVisibility(View.VISIBLE);
						userLoginInfo.setNew_token(userBingInfo.getNew_token());
						userLoginInfo.setStatus(userBingInfo.getStatus());
						userLoginInfo.setChannel_id(userBingInfo.getChannel_id());
						userLoginInfo.setStream_id(stream_id);
						userLoginInfo.setUrl(scanMsg+"&username="+userLoginInfo.getUsername());
						sp.edit().putString("Channel_id", userBingInfo.getChannel_id()).commit();
						tv_bdid.setText(userBingInfo.getChannel_id());
						
					}else{
						UIUtils.showToastSafe("绑定失败(" + userBingInfo.getMsg() + ")");
						ll_bdf.setVisibility(View.VISIBLE);
						ll_bdt.setVisibility(View.GONE);
					}
					
				}
			});
			
			ed.putString("scanMsg", null);
			ed.commit();
		}else{
			if(isBind()){
				ll_bdf.setVisibility(View.GONE);
				ll_bdt.setVisibility(View.VISIBLE);
				tv_bdid.setText(userLoginInfo.getChannel_id());
			}else{
				ll_bdf.setVisibility(View.VISIBLE);
				ll_bdt.setVisibility(View.GONE);
			}
			
			ed.putString("scanMsg", null);
			ed.commit();
		}
		super.onStart();
	}
	
	
	private List<String> cutURL(String scanMsg) {
		List<String> msgs= new ArrayList<String>();
		  int index = scanMsg.indexOf("/",8); 
		  char[] ch = scanMsg.toCharArray();  
		  String msg = String.copyValueOf(ch, 0, index);
		  msgs.add(0, msg);
		  
		  Matcher msg2=Pattern.compile("id=(.*?)&").matcher(scanMsg);
		  while(msg2.find()){
			  msgs.add(1, msg2.group(1));
		  }


		  int index2 = scanMsg.lastIndexOf("=");
		  char[] ch2 = scanMsg.toCharArray();  
		  String msg3 = String.copyValueOf(ch2, index2+1, ch2.length - index2 - 1);
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
			if("3".equals(userLoginInfo.getStatus())){
				UIUtils.showToastSafe("正在点播无法解除绑定");
				break;
			}
			restService.unBind(userLoginInfo, new CallbackunBind() {
				
				@Override
				public void callbackUnBind(UnBindInfo unBindInfo) {
					if("0".equals(unBindInfo.getReturn_code())){
						ll_bdf.setVisibility(View.VISIBLE);
						ll_bdt.setVisibility(View.GONE);
						UIUtils.showToastSafe("解绑成功");
						userLoginInfo.setNew_token(unBindInfo.getNew_token());
						userLoginInfo.setStatus(unBindInfo.getStatus());
					}else{
						UIUtils.showToastSafe("绑定失败");
						ll_bdf.setVisibility(View.GONE);
						ll_bdt.setVisibility(View.VISIBLE);
					}
				}
			});
			break;
		}
	}

}
