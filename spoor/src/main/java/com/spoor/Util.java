package com.spoor;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.ThreadFactory;

/**
 * Desc:
 * Author:Created by huibin
 * Date: Created on 18/3/26 14:22
 */

final class Util {

    private Util(){}

    public static String getCodeLoc(int level) {
        StackTraceElement ste = new Exception().getStackTrace()[level];
        return ste.getFileName() + " method:" + ste.getMethodName() + " line:" + ste.getLineNumber();
    }

    public static ThreadFactory threadFactory(final String name, final boolean daemon) {

        return new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable runnable) {
                Thread result = new Thread(runnable, name);
                result.setDaemon(daemon);
                return result;
            }
        };

    }

    /**
     * Returns true if the string is null or 0-length.
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static String getAppName(@NonNull Context ctx) {
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (pkgInfo==null) return "";
        return pkgInfo.applicationInfo.loadLabel(ctx.getPackageManager()).toString();
    }

    public static String getVersionName(@NonNull Context ctx)  {
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (pkgInfo==null) return "";
        return pkgInfo.versionName;
    }

    public static int getVersionNo(Context ctx) {
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (pkgInfo==null) return -1;
        return pkgInfo.versionCode;
    }

}
