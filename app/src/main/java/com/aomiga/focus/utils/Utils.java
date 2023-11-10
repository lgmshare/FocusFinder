package com.aomiga.focus.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;

import com.aomiga.focus.R;

/**
 * @author lim
 * @description: TODO
 * @email lgmshare@gmail.com
 * @datetime 2016/9/14 11:06
 */
public class Utils {


    //得到绝对地址
    private static String getRealPathFromURI(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String fileStr = cursor.getString(column_index);
        cursor.close();
        return fileStr;
    }

    /**
     * 获取视频文件截图
     *
     * @param path 视频文件的路径
     * @return Bitmap 返回获取的Bitmap
     */
    public static Bitmap getVideoThumb(String path) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        return media.getFrameAtTime();
    }

    public static ContentValues getImageContentValues(Context context, File file, long time) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, file.getName());
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, file.getName());
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.Images.Media.DATE_TAKEN, Long.valueOf(time));
        contentValues.put(MediaStore.Images.Media.DATE_MODIFIED, Long.valueOf(time));
        contentValues.put(MediaStore.Images.Media.DATE_ADDED, Long.valueOf(time));
        contentValues.put(MediaStore.Images.Media.ORIENTATION, Integer.valueOf(0));
        contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        contentValues.put(MediaStore.Images.Media.SIZE, Long.valueOf(file.length()));
        return contentValues;
    }

    public static ContentValues getVideoContentValues(Context context, File file, long time) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Video.Media.TITLE, file.getName());
        contentValues.put(MediaStore.Video.Media.DISPLAY_NAME, file.getName());
        contentValues.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        contentValues.put(MediaStore.Video.Media.DATE_TAKEN, Long.valueOf(time));
        contentValues.put(MediaStore.Video.Media.DATE_MODIFIED, Long.valueOf(time));
        contentValues.put(MediaStore.Video.Media.DATE_ADDED, Long.valueOf(time));
        contentValues.put(MediaStore.Video.Media.DATA, file.getAbsolutePath());
        contentValues.put(MediaStore.Video.Media.SIZE, Long.valueOf(file.length()));
        return contentValues;
    }

    public static Uri insertImageToAlbum(Context context, File file) {
        try {
            ContentResolver resolver = context.getContentResolver();
            //ContentValues contentValues = getImageContentValues(context, file, System.currentTimeMillis());
            //resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            String path = MediaStore.Images.Media.insertImage(resolver, file.getAbsolutePath(), file.getName(), context.getString(R.string.app_name));
            Uri uri = Uri.parse(path);
            File uriFile = new File(getRealPathFromURI(context, uri));
            //更新图库
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(uriFile));
            context.sendBroadcast(intent);
            return uri;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void insertVideoToAlbum(Context context, File file) {
        try {
            ContentResolver resolver = context.getContentResolver();
            ContentValues contentValues = getVideoContentValues(context, file, System.currentTimeMillis());
            Uri uri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
            //更新图库
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(uri);
            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Uri getUriFromFile(Context context, File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

}
