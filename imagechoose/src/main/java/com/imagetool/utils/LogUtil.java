package com.imagetool.utils;

import android.text.TextUtils;
import android.util.Log;

import com.imagetool.imagechoose.BuildConfig;

/**
 * 类名称：LogUtil
 * 创建者：Create by liujc
 * 创建时间：Create on 2016/11/15 13:39
 * 描述：log工具类
 * 最近修改时间：2016/11/15 13:39
 * 修改人：Modify by liujc
 */
public class LogUtil {
    public static final boolean bOpenLog = BuildConfig.LOG_DEBUG;
    public LogUtil() {
    }

    public static int d(String msg) {
        if (bOpenLog){
            String[] classMethod = getClassMethod(new Exception(), "LogUtil", "d");
            return d(classMethod[0], classMethod[1], msg);
        }else {
            return 0;
        }
    }

    public static int d(String tag, String method, String msg) {
        boolean ret = true;
        int ret1 = Log.d(tag, method + "-" + msg);
        return ret1;
    }

    public static int i(String tag, String method, String sipId, String status, String msg) {
        boolean ret = true;
        int ret1 = Log.i(tag, method + "-" + sipId + "-" + status + "-" + msg);
        return ret1;
    }

    public static int begin(String msg) {
        if (bOpenLog) {
            String[] classMethod = getClassMethod(new Exception(), "LogUtil", "begin");
            return i(classMethod[0], classMethod[1], "#", "begin", msg);
        }else {
            return 0;
        }
    }

    public static int end(String msg) {
        if (bOpenLog) {
            String[] classMethod = getClassMethod(new Exception(), "LogUtil", "end");
            return i(classMethod[0], classMethod[1], "#", "end", msg);
        }else {
            return 0;
        }
    }

    public static int e(String msg, Throwable e) {
        if (bOpenLog){
            String[] classMethod = getClassMethod(new Exception(), "LogUtil", "e");
            return e(classMethod[0], classMethod[1], msg, e);
        }else {
            return 0;
        }
    }

    public static int e(String tag, String method, String msg, Throwable e) {
        boolean ret = true;
        int ret1 = Log.e(tag, method + "-" + msg, e);
        return ret1;
    }


    public static String[] getClassMethod(Exception e, String defaultClass,
                                          String defaultMethod) {
        String methodName = "";
        String className = "";
        StackTraceElement el = null;
        try {
            el = e.getStackTrace()[1];
            className = el.getClassName();
            className = className.substring(className.lastIndexOf(".") + 1);
            methodName = el.getMethodName();
        } catch (Exception ex) {
            if (TextUtils.isEmpty(className)) {
                className = defaultClass;
            }
            if (TextUtils.isEmpty(methodName)) {
                methodName = defaultMethod;
            }
        }
        el = null;
        return new String[]{className, methodName};
    }
}
