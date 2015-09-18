package com.bbcvision.Multiscreen.fragments;

import com.bbcvision.Multiscreen.R;
import com.bbcvision.Multiscreen.bean.KeySendInfo;
import com.bbcvision.Multiscreen.bean.VodPlayInfo;
import com.bbcvision.Multiscreen.netservices.CallbackkeySend;
import com.bbcvision.Multiscreen.netservices.CallbackvodPlay;
import com.bbcvision.Multiscreen.tools.UIUtils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class RemoteFragment extends BaseFragment implements OnClickListener{

	private View view;
	private TextView iv_remote_control_tittle;
	private TextView iv_remote_control_tittleNUM;
	private TextView iv_remote_control_tittle2;
	private ImageButton btn_remote_control_play_pause;
	private ImageButton btn_remote_control_up;
	private ImageButton btn_remote_control_dowm;
	private ImageButton btn_remote_control_left;
	private ImageButton btn_remote_control_right;
	private Button btn_remote_control_stop;
	private Button btn_remote_control_quik_back;
	private Button btn_remote_control_quik_forward;
	private TextView btn_remote_control_back;
	private TextView btn_remote_control_exit;
	
	public boolean isChick=false;

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.fragment_remote2, null);
		return view;
	}

	@Override
	public void initData() {
		iv_remote_control_tittle = (TextView) view.findViewById(R.id.iv_remote_control_tittle);
		iv_remote_control_tittle2 = (TextView) view.findViewById(R.id.iv_remote_control_tittle2);
		iv_remote_control_tittleNUM = (TextView) view.findViewById(R.id.iv_remote_control_tittleNUM);
		// TODO Auto-generated method stub
		
		btn_remote_control_play_pause =(ImageButton) view.findViewById(R.id.btn_remote_control_play_pause);
		btn_remote_control_up =(ImageButton) view.findViewById(R.id.btn_remote_control_up);
		btn_remote_control_dowm =(ImageButton) view.findViewById(R.id.btn_remote_control_dowm);
		btn_remote_control_left =(ImageButton) view.findViewById(R.id.btn_remote_control_left);
		btn_remote_control_right =(ImageButton) view.findViewById(R.id.btn_remote_control_right);
		
		btn_remote_control_stop = (Button) view.findViewById(R.id.btn_remote_control_stop);
		btn_remote_control_quik_back = (Button) view.findViewById(R.id.btn_remote_control_quik_back);
		btn_remote_control_quik_forward = (Button) view.findViewById(R.id.btn_remote_control_quik_forward);
		
		btn_remote_control_back = (TextView) view.findViewById(R.id.btn_remote_control_back);
		btn_remote_control_exit = (TextView) view.findViewById(R.id.btn_remote_control_exit);
		
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

	}
	
	@Override
	public void onResume() {
		super.onResume();
		getVolStatus();
	}

	public void getVolStatus() {
		String status = userLoginInfo.getStatus();
		if("2".equals(status)){
			iv_remote_control_tittle.setText("本地频道号：");
			iv_remote_control_tittleNUM.setText(userLoginInfo.getChannel_id());
			iv_remote_control_tittle2.setText("请点播节目");
			isChick = false;
		}else if("3".equals(status)){
			iv_remote_control_tittle.setText("本地频道号：");
			iv_remote_control_tittleNUM.setText(userLoginInfo.getChannel_id());
			iv_remote_control_tittle2.setText("正在点播："+vodPlayInfo.getVodname());
			isChick = false;
		}else{
			iv_remote_control_tittle.setText("请绑定频道后进行点播");
			iv_remote_control_tittleNUM.setText("");
			iv_remote_control_tittle2.setText("");
			isChick = true;
		}
	}

	@Override
	public void onClick(View v) {
		String keyId = null;
		String key_type = "1001";
		int id = v.getId();
		switch (id) {
		case R.id.btn_remote_control_play_pause:
			keyId = "0x1f";
			keySend(key_type,keyId);
			break;
		case R.id.btn_remote_control_up:
			keyId = "0x00";
			keySend(key_type,keyId);
			break;
		case R.id.btn_remote_control_dowm:
			keyId = "0x01";
			keySend(key_type,keyId);
			break;
		case R.id.btn_remote_control_left:
			keyId = "0x03";
			keySend(key_type,keyId);
			break;
		case R.id.btn_remote_control_right:
			keyId = "0x02";
			keySend(key_type,keyId);
			break;
		case R.id.btn_remote_control_stop:
			keyId = "";
			keySend(key_type,keyId);
			break;
		case R.id.btn_remote_control_quik_back:
			keyId = "0x04";
			keySend(key_type,keyId);
			break;
		case R.id.btn_remote_control_quik_forward:
			keyId = "0x08";
			keySend(key_type,keyId);
			break;
		case R.id.btn_remote_control_back:
			keyId = "0x1c";
			keySend(key_type,keyId);
			break;
		case R.id.btn_remote_control_exit:
			keyId = "0x1d";
			keySend(key_type,keyId);
			break;
		}
		
	}

	private void keySend(String key_type,String keyId) {
		if(!isChick){
			isChick=true;
			restService.keySend(userLoginInfo, key_type, keyId, new CallbackkeySend() {
				@Override
				public void callbackKeySend(KeySendInfo keySendInfo) {
					if("0".equals(keySendInfo.getReturn_code())){
						userLoginInfo.setStatus(keySendInfo.getStatus());
						//Log.i("keySendInfoStatus", keySendInfo.getStatus());
						getVolStatus();
					}else{
						UIUtils.showToastSafe(keySendInfo.getMsg());
					}
					isChick=false;
				}
			});
		}
	}

}
