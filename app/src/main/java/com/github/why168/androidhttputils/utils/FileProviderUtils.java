package com.github.why168.androidhttputils.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;

import java.io.File;
import java.util.List;

/**
 * FileProviderUtils
 * <p>
 * <root-path/> 代表设备的根目录new File("/");
 * <files-path/> 代表context.getFilesDir()
 * <cache-path/> 代表context.getCacheDir()
 * <external-path/> 代表Environment.getExternalStorageDirectory()
 * <external-files-path/> 代表context.getExternalFilesDirs()
 * <external-cache-path/> 代表getExternalCacheDirs()
 *
 * @author Edwin.Wu
 * @version 2017/6/20 18:18
 * @since JDK1.8
 */
public class FileProviderUtils {
    /**
     * File convert Uri
     *
     * @param context
     * @param file
     * @return
     */
    private static Uri getUriForFile(Context context, File file) {
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = getUriForFile24(context, file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

    private static Uri getUriForFile24(Context context, File file) {
        return android.support.v4.content.FileProvider.getUriForFile(context,
                context.getPackageName() + ".fileprovider",
                file);
    }

    /**
     * 安装APK
     *
     * @param context   this
     * @param intent    intent
     * @param type      "application/vnd.android.package-archive"
     * @param file      file
     * @param writeAble true
     */
    public static void setIntentDataAndType(Context context,
                                            Intent intent,
                                            String type,
                                            File file,
                                            boolean writeAble) {
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setDataAndType(getUriForFile(context, file), type);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), type);
        }
    }

    public static void grantPermissions(Context context, Intent intent, Uri uri, boolean writeAble) {
        int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
        if (writeAble) {
            flag |= Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
        }
        intent.addFlags(flag);
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, flag);
        }
    }
}
