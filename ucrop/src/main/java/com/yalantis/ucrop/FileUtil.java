package com.yalantis.ucrop;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.yalantis.ucrop.util.DateUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FileUtil {
    private static String path;

    private static String dir = null;

    static {

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            dir = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/xiaomei/";

        } else {
            dir = Environment.getRootDirectory().getAbsolutePath() + "/xiaomei/";
        }

        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private final static Map<String, String> MIME_MAP = new HashMap<String, String>() {
        {
            put(".3gp", "video/3gpp");
            put(".apk", "application/vnd.android.package-archive");
            put(".asf", "video/x-ms-asf");
            put(".avi", "video/x-msvideo");
            put(".bin", "image/bmp");
            put(".bmp", "image/bmp");
            put(".c", "text/plain");
            put(".class", "application/octet-stream");
            put(".conf", "text/plain");
            put(".cpp", "text/plain");
            put(".doc", "application/msword");
            put(".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            put(".xls", "application/vnd.ms-excel");
            put(".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            put(".exe", "application/octet-stream");
            put(".gif", "image/gif");
            put(".gtar", "application/x-gtar");
            put(".gz", "application/x-gzip");
            put(".h", "text/plain");
            put(".htm", "text/html");
            put(".html", "text/html");
            put(".jar", "application/java-archive");
            put(".java", "text/plain");
            put(".jpeg", "image/jpeg");
            put(".jpg", "image/jpeg");
            put(".js", "application/x-javascript");
            put(".log", "text/plain");
            put(".m3u", "audio/x-mpegurl");
            put(".m4a", "audio/mp4a-latm");
            put(".m4b", "audio/mp4a-latm");
            put(".m4p", "audio/mp4a-latm");
            put(".m4u", "video/vnd.mpegurl");
            put(".m4v", "video/x-m4v");
            put(".mov", "video/quicktime");
            put(".mp2", "audio/x-mpeg");
            put(".mp3", "audio/x-mpeg");
            put(".mp4", "video/mp4");
            put(".mpc", "application/vnd.mpohun.certificate");
            put(".mpe", "video/mpeg");
            put(".mpeg", "video/mpeg");
            put(".mpg", "video/mpeg");
            put(".mpg4", "video/mp4");
            put(".mpga", "audio/mpeg");
            put(".msg", "application/vnd.ms-outlook");
            put(".ogg", "audio/ogg");
            put(".pdf", "application/pdf");
            put(".png", "image/png");
            put(".pps", "application/vnd.ms-powerpoint");
            put(".ppt", "application/vnd.ms-powerpoint");
            put(".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
            put(".prop", "text/plain");
            put(".rc", "text/plain");
            put(".rmvb", "audio/x-pn-realaudio");
            put(".rtf", "application/rtf");
            put(".sh", "text/plain");
            put(".tar", "application/x-tar");
            put(".tgz", "application/x-compressed");
            put(".txt", "text/plain");
            put(".wav", "audio/x-wav");
            put(".wma", "audio/x-ms-wma");
            put(".wmv", "audio/x-ms-wmv");
            put(".wps", "application/vnd.ms-works");
            put(".xml", "text/plain");
            put(".z", "application/x-compress");
            put(".zip", "application/x-zip-compressed");
            put("", "*/*");
        }
    };


    public static boolean isCacheFileExist(String url) {

        String filePath = null;
        String fileName = "" + url.hashCode();
        filePath = dir + fileName;
        File file = new File(filePath);
        return file.exists();

    }

    public static String getCacheFilePath(String url) {

        String filePath = null;
        String fileName = "" + url.hashCode();
        filePath = dir + fileName;
        return filePath;
    }


    public static File readAppRootFile() {
        File file;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            file = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator + "xiaomei" + File.separator);

        } else {
            file = new File(Environment.getRootDirectory().getAbsolutePath() + File.separator + "xiaomei" + File.separator);
        }
        return file;
    }

    public static boolean save2File(String dirName, String fileName, String text) {
        File dir = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            dir = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator + "xiaomei" + File.separator + dirName + File.separator);

        } else {
            dir = new File(Environment.getRootDirectory().getAbsolutePath() + File.separator + "xiaomei" + File.separator + dirName + File.separator);
        }

        if (!dir.exists()) {
            boolean mkdirs = dir.mkdirs();
            if (!mkdirs) {
                return false;
            }
        }
        try {
            //存储路径，是sd卡的crash文件夹

            File file = new File(dir, fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(text.getBytes());
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static File saveBitmapFile(Bitmap bitmap) {
//        File dir = null;
//        if (Environment.getExternalStorageState().equals(
//                Environment.MEDIA_MOUNTED)) {
//            dir = new File(Environment.getExternalStorageDirectory()
//                    .getAbsolutePath() + File.separator + "xiaomei" + File.separator + "photo" + File.separator);
//
//        } else {
//            dir = new File(Environment.getRootDirectory().getAbsolutePath() + File.separator + "xiaomei" + File.separator + "photo" + File.separator);
//        }
//
//        if (!dir.exists()) {
//            boolean mkdirs = dir.mkdirs();
//            if (!mkdirs) {
//                return null;
//            }
//        }
//        File file = new File(dir, DateUtil.getFormatString(Calendar.getInstance(), "MM-dd_HH-mm-ss") + ".jpg");//将要保存图片的路径
        File file = getSaveBitmapFile();
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    public static File getSaveBitmapFile() {
        File dir = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            dir = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator + "lsyz" + File.separator + "photo" + File.separator);

        } else {
            dir = new File(Environment.getRootDirectory().getAbsolutePath() + File.separator + "lsyz" + File.separator + "photo" + File.separator);
        }

        if (!dir.exists()) {
            boolean mkdirs = dir.mkdirs();
            if (!mkdirs) {
                return null;
            }
        }
        File file = new File(dir, DateUtil.getFormatString(Calendar.getInstance(), "MM-dd_HH-mm-ss") + ".jpg");//将要保存图片的路径
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        path = file.getAbsolutePath();
        return file;
    }

//    public static void saveObject(BaseBean bean) {
//        FileOutputStream fileOutputStream = null;
//        ObjectOutputStream objectOutputStream = null;
//
//        try {
//            File file = new File(MainApplication.getInstance().getCacheDir().getAbsolutePath() + File.separator + "templateproduct");
//
//            if (!file.getParentFile().exists()) {
//                boolean mkdirs = file.getParentFile().mkdirs();
//                if (!mkdirs) {
//                    return;
//                }
//            }
//
//            if (file.exists()) {
//                file.delete();
//            }
//
//            file.createNewFile();
//            fileOutputStream = new FileOutputStream(file);
//            objectOutputStream = new ObjectOutputStream(fileOutputStream);
//            objectOutputStream.writeObject(bean);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (objectOutputStream != null) {
//                try {
//                    objectOutputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (fileOutputStream!=null) {
//                try {
//                    fileOutputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    public static BaseBean readObject() {
//        FileInputStream fileInputStream = null;
//        ObjectInputStream objectInputStream = null;
//        BaseBean bean = null;
//
//        File file = new File(MainApplication.getInstance().getCacheDir().getAbsolutePath() + File.separator + "templateproduct");
//
//        try {
//
//            if (file.exists()) {
//                fileInputStream = new FileInputStream(file);
//                objectInputStream = new ObjectInputStream(fileInputStream);
//                bean = (BaseBean) objectInputStream.readObject();
//
//                if (bean == null) {
//                    file.delete();
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            if (file.exists()) {
//                file.delete();
//            }
//        } finally {
//            if (objectInputStream != null) {
//                try {
//                    objectInputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (fileInputStream != null) {
//                try {
//                    fileInputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        return bean;
//    }


    public static String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名*/
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        String s = MIME_MAP.get(end);
        if (s != null) {
            type = s;
        }
        return type;
    }

    /**
     * 仅在getSaveBitmapFile后立即使用，没事别用
     *
     * @return 保存的照片路径
     */
    public static String getPath() {
        String s = path;
        path = null;
        return s;
    }


    /**
     * Method for return file path of Gallery image
     *
     * @param context
     * @param uri
     * @return path of the selected image file from gallery
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        // check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }


    public static void copyFile(String oldPath, String newPath) {
        try {
            int byteSum = 0, byteRead;
            File oldFile = new File(oldPath);
            if (oldFile.exists()) {

                InputStream is = new FileInputStream(oldPath);
                OutputStream os = new FileOutputStream(newPath);
                byte[] buffer = new byte[2048];
                while ((byteRead = is.read(buffer)) != -1) {
                    byteSum += byteRead;
                    Log.e("FileUtil", "copyFile: byte sum is " + byteSum);
                    os.write(buffer, 0, byteRead);
                }
                is.close();
                os.close();

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("FileUtil", "复制文件出错!");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("FileUtil", "复制文件出错!");
        }
    }
}
