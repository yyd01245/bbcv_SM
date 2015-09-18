package com.bbcvision.Multiscreen.tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;


public class PackageUtils {

	
	public static PackageInfo getPackageInfo(String packageName) {
		Context context = UIUtils.getContext();
		if (null == context) {
			return null;
		}
		if (StringUtils.isEmpty(packageName)) {
			packageName = context.getPackageName();
		}
		PackageInfo info = null;
		PackageManager manager = context.getPackageManager();
		try {
			info = manager.getPackageInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
		} catch (PackageManager.NameNotFoundException e) {
			//LogUtils.e(e);
		}
		return info;
	}

	
	public static int getVersionCode() {
		PackageInfo info = getPackageInfo(null);
		if (info != null) {
			return info.versionCode;
		} else {
			return -1;
		}
	}

	
	public static String getVersionName() {
		PackageInfo info = getPackageInfo(null);
		if (info != null) {
			return info.versionName;
		} else {
			return null;
		}
	}
}
