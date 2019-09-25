package com.sunmi.sunmit2demo.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;


import java.io.File;

public class InstallApkUtils {
    public static String smilePkgName = "com.alipay.zoloz.smile";
    public static int smileVersion = 322;

    public static void installApp(Context context, boolean isVertical) {
        if (!checkApkExist(context, smilePkgName)
                 || InstallApkUtils.getVersionCode(context, InstallApkUtils.smilePkgName) < InstallApkUtils.smileVersion
                ) {
            File file = new File(
                    Environment.getExternalStorageDirectory().getPath()
                    , isVertical ? "smile_K.apk" : "smile_T.apk");
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri =
                    FileProvider.getUriForFile(context, "com.sunmi.sunmit2demo.s", file);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            context.startActivity(intent);
        }
    }


    public static boolean checkApkExist(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * get App versionCode * @param context * @return
     */
    public static int getVersionCode(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        int versionCode = 0;
        try {
            packageInfo = packageManager.getPackageInfo(packageName, 0);
            versionCode = packageInfo.versionCode;
            Log.e("@@@","versionCode=="+versionCode+" ");

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }


}
