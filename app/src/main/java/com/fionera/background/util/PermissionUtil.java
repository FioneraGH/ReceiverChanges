package com.fionera.background.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PermissionUtil {

    public static void openAppSetting(Activity context, int requestCode) {
        ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.applications.InstalledAppDetails");
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package:", context.getPackageName(), null));
        intent.setComponent(componentName);
        context.startActivityForResult(intent, requestCode);
    }

    public static void requestFloatPermission(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + activity.getPackageName()));
            activity.startActivityForResult(intent, requestCode);
        } else {
            openAppSetting(activity, requestCode);
        }
    }

    public static void requestPipPermission(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = new Intent("android.settings.PICTURE_IN_PICTURE_SETTINGS");
            intent.setData(Uri.parse("package:" + activity.getPackageName()));
            activity.startActivityForResult(intent, requestCode);
        }
    }

    public static final int OP_SYSTEM_ALERT_WINDOW = 24;
    public static final int OP_PICTURE_IN_PICTURE = 67;

    /**
     * 判断是否获取到了悬浮窗权限
     */
    public static boolean checkFloatPermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return checkOps(context, OP_SYSTEM_ALERT_WINDOW);
        } else {
            return Settings.canDrawOverlays(context);
        }
    }

    /**
     * 判断是否获取到了画中画权限
     */
    public static boolean checkPipPermission(Context context) {
            return checkOps(context, OP_PICTURE_IN_PICTURE);
    }

    private static boolean checkOps(Context context, int op) {
        try {
            Class<?> cls = Class.forName("android.content.Context");
            Field declaredField = cls.getDeclaredField("APP_OPS_SERVICE");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(cls);
            if (!(obj instanceof String)) {
                return false;
            }
            String str = (String) obj;
            obj = cls.getMethod("getSystemService", String.class).invoke(context, str);
            cls = Class.forName("android.app.AppOpsManager");
            Field declaredField2 = cls.getDeclaredField("MODE_ALLOWED");
            declaredField2.setAccessible(true);
            Method checkOp = cls.getMethod("checkOp", Integer.TYPE, Integer.TYPE, String.class);
            int result = (Integer) checkOp.invoke(obj, op, Binder.getCallingUid(), context.getPackageName());
            return result == declaredField2.getInt(cls);
        } catch (Exception e) {
            return false;
        }
    }
}
