package com.taihe.template.base.util;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

public class FileUtil {
    private static Context context;

    public static void init(Application a) {
        context = a;
    }

    public static File getCacheDir() {
        File f = null;
        f = context.getExternalCacheDir();
        if (f == null) {
            f = context.getCacheDir();
        }
        return f;
    }

    public static File getFileDir() {
        File f = null;
        f = context.getExternalFilesDir("");
        if (f == null) {
            f = context.getFilesDir();
        }
        return f;
    }

    public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(index);
                }
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("module.cloud.file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getFormatedFileSize(long fileSize) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileSize < 1024L)
            fileSizeString = df.format(fileSize) + "B";
        else if (fileSize < 1048576L)
            fileSizeString = df.format(fileSize / 1024.0D) + "K";
        else if (fileSize < 1073741824L)
            fileSizeString = df.format(fileSize / 1048576.0D) + "M";
        else {
            fileSizeString = df.format(fileSize / 1073741824.0D) + "G";
        }
        return fileSizeString;
    }

    public static String getFileMD5(File file) {
        String value = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            MessageDigest digester = MessageDigest.getInstance("MD5");
            byte[] bytes = new byte[8192];
            int byteCount;
            while ((byteCount = in.read(bytes)) > 0) {
                digester.update(bytes, 0, byteCount);
            }
            byte[] digest = digester.digest();
            value = new String(digest);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return value;
    }
}