package com.lzn.voice.util;

import java.lang.reflect.Method;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

public class Utils {
    public static final String TAG = "LznVoice";

    public static final int CMCC_DEVICECMEI_LENGTH = 15;

    public static final String PROPERTY_CMCC_DEVICETYPE   = "ro.product.cmcc_id";
    public static final String PROPERTY_CMCC_DEVICECMEI   = "ro.product.cmcc_cmei";
    public static final String PROPERTY_CMCC_PRODUCTTOKEN = "ro.product.cmcc_token";
    public static final String PROPERTY_CMCC_PRODUCTCLASS = "ro.product.cmcc_class";
    public static final String PROPERTY_CMCC_DMAPPID      = "ro.product.cmcc_appid";

    private static boolean mBVoiceInit = false;

    public static boolean getVoiceInit() {
        return mBVoiceInit;
    }

    public static void setVoiceInit(boolean bInit) {
        mBVoiceInit = bInit;
    }

    public static final String getCmccDeviceType () {
        return getProperty(PROPERTY_CMCC_DEVICETYPE);
    }

    public static final String getCmccDeviceCmei() {
        return getProperty(PROPERTY_CMCC_DEVICECMEI);
    }

    public static final String getCmccProductToken() {
        return getProperty(PROPERTY_CMCC_PRODUCTTOKEN);
    }

    public static final String getCmccProductClass() {
        return getProperty(PROPERTY_CMCC_PRODUCTCLASS);
    }

    public static final String getCmccSwVersion() {
        return Build.DISPLAY;
    }

    public static final String getCmccDeviceMac(Context ctx) {
        return getWirelessMacAddress(ctx);
    }

    public static final String getCmccDmAppId() {
        return getProperty(PROPERTY_CMCC_DMAPPID);
    }

    public static final boolean isWifiConnected(Context ctx) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mNetworkInfo.isConnected();
    }

    public static final String getWirelessMacAddress(Context ctx) {
        WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        if (null != info) {
            String macAddr = info.getMacAddress();
            if (null != macAddr) {
                Log.d(TAG, "get wifi macaddr:" + macAddr);
                return macAddr;
            }
        }
        Log.e(TAG, "Fail to get wifi macaddr.");
        return null;
    }

    public static final String getProperty(String name) {
        String id = null;

        try {
            Class c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", new Class[] {String.class});
            id = (String) get.invoke(c, new Object[] {name});
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Error", e);
        }

        return id;
    }

    public static void setProperty(String key, String value) {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method set = c.getMethod("set", String.class, String.class);
            set.invoke(c, key, value );
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Error", e);
        }

        return ;
    }

}

