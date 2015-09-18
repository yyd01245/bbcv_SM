package com.bbcvision.Multiscreen.tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by mwqi on 2014/6/19.
 */
public class PackageUtils {

	/** 根据packageName获取packageInfo */
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
		// 根据packageName获取packageInfo
		try {
			info = manager.getPackageInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
		} catch (PackageManager.NameNotFoundException e) {
			//LogUtils.e(e);
		}
		return info;
	}

	/** 获取本应用的VersionCode */
	public static int getVersionCode() {
		PackageInfo info = getPackageInfo(null);
		if (info != null) {
			return info.versionCode;
		} else {
			return -1;
		}
	}

	/** 获取本应用的VersionName */
	public static String getVersionName() {
		PackageInfo info = getPackageInfo(null);
		if (info != null) {
			return info.versionName;
		} else {
			return null;
		}
	}
}
