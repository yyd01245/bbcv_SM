package com.bbcvision.Multiscreen.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;


public class PreferenceTool {

    private static String PREFERENCE_FILE = "preference_file";

    public static boolean isFirstRun(Context context){
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
//            return packageInfo.versionCode != getLastVersionCode(context);
            return packageInfo.versionCode != getLastVersionCode(context);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    private static  String VERSION_CODE = "version_code";
    private static  String VERSION_NAME = "version_name";

    private static int getLastVersionCode(Context context){
        return getInt(context,VERSION_CODE,0);
    }

    private static String getLastVersionName(Context context){
        return getString(context,VERSION_NAME,"");
    }
    public void  saveVersionName(Context context){
        String vn = "";
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            vn =  packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        saveString(context,VERSION_NAME,vn);

    }

    public static void saveVersionCode(Context context){
        int vc = 0;
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            vc =  packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        saveInt(context,VERSION_CODE,vc);
    }

    private static void saveInt(Context context ,String key,int val){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,val);
        editor.commit();
    }

    private static void saveString(Context context ,String key,String val){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, val);
        editor.commit();
    }
    private static String getString(Context context,String key,String def){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, def);
    }
    private static  int getInt(Context context,String key ,int def){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, def);
    }

    private static String AREA_NAME = "area_name";
    private static String AREA_ID = "area_id";

    public  static  void saveAreaName(Context context ,String areaName){
        saveString(context,AREA_NAME,areaName);
    }
    public static String getAreaName(Context context){
        return getString(context,AREA_NAME,"璇烽�夋嫨鍖哄煙");
    }
    public  static  void saveAreaID(Context context ,String areaId){
        saveString(context, AREA_ID, areaId);
    }
    public static  String getAreaId(Context context ){
        return getString(context,AREA_ID, SysConfig.REGION_CODE1);
    }




}
