package com.imagetool.utils;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;

/**
 * 类名称：PermissionUtil
 * 创建者：Create by liujc
 * 创建时间：Create on 2017/11/14 21:22
 * 描述：TODO
 */

public class PermissionUtil {
    final static String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };
    //检查所需的全部权限
    public static boolean doCheckPermission(Activity activity) {
        PermissionsChecker mPermissionsChecker = new PermissionsChecker(activity);
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS, 0x12);
            return false;
        }
        return true;
    }
}
