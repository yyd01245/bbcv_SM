package com.bbcvision.Multiscreen.manager;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;


public class BaseApplication extends Application {
	
	private static BaseApplication mInstance;
	
	private static int mMainThreadId = -1;
	
	private static Thread mMainThread;
	
	private static Handler mMainThreadHandler;
	
	private static Looper mMainLooper;

	@Override
	public void onCreate() {
		mMainThreadId = android.os.Process.myTid();
		mMainThread = Thread.currentThread();
		mMainThreadHandler = new Handler();
		mMainLooper = getMainLooper();
		mInstance = this;
		super.onCreate();
	}

	public static BaseApplication getApplication() {
		return mInstance;
	}

	
	public static int getMainThreadId() {
		return mMainThreadId;
	}

	
	public static Thread getMainThread() {
		return mMainThread;
	}

	
	public static Handler getMainThreadHandler() {
		return mMainThreadHandler;
	}

	
	public static Looper getMainThreadLooper() {
		return mMainLooper;
	}
}
