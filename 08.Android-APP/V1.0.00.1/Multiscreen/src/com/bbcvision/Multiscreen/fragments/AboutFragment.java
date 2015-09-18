package com.bbcvision.Multiscreen.fragments;

import com.bbcvision.Multiscreen.activity.HomeActivity2;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

public class AboutFragment extends BaseFragment {

	@Override
	public View initView(LayoutInflater inflater) {
		return null;
	}

	@Override
	public void initData() {

	}
	
	/*public boolean onKeyDown(int keyCoder, KeyEvent event) {
		if (keyCoder == KeyEvent.KEYCODE_BACK) {
			((HomeActivity2) getActivity()).switchFragment(((HomeActivity2) getActivity()).homeFragment);
			Log.i("KEYCODE_BACK", "111");
			return true;
		}
		return false;
	}*/

}
