package com.magispec.shield.utils;

import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {
    public static String StringTOSpiltString(String temp) throws UnsupportedEncodingException {
        String subString = temp;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < subString.length(); i++) {
            String spitString = temp.substring(i, i + 1);
            System.out.println(spitString);
            bytesToHexString(spitString.getBytes("gb2312"));

            sb.append(bytesToHexString(spitString.getBytes("gb2312")) + "*");
        }
        System.out.println(sb.toString());
        return sb.toString();
    }

    public static void StringToGB2312(String temp) {


    }


    public static int[] twoHexStringTOBytes(String s) {
        String a = s.substring(0, 2);
        String b = s.substring(2, 4);
        int[] c = new int[s.length() / 2];
        c[0] = Integer.valueOf(a, 16);
        c[1] = Integer.valueOf(b, 16);
        return c;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public synchronized static byte[] hexStringToByte(String hex) {
        hex = hex.toUpperCase();
        int len = hex.length() / 2;
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private synchronized static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }


    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    //验证是否有网络
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static double getMax(double[] a) {
        double b = a[0];
        for (int i = 1; i < a.length; i++) {
            if (b < a[i]) {

                b = a[i];
            }

        }


        return b;
    }

    public static double getMin(double[] a) {
        double b = a[0];
        for (int i = 1; i < a.length; i++) {
            if (b > a[i]) {

                b = a[i];
            }

        }

        return b;
    }


}


