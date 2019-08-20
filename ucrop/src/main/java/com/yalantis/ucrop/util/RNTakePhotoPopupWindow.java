package com.yalantis.ucrop.util;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.yalantis.ucrop.FileUtil;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.bean.PhotoConfigBean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class RNTakePhotoPopupWindow {
    public static final int REQ_TAKE_PHOTO = 10001;
    public static final int REQ_SELECT_PHOTO = 10002;
    public static final int REQ_CROP_PHOTO = 10003;
    private static final String TAG = "meicai";
    Activity mActivity;

    public TakePhotoListener mTakePhotoListener;


    private PhotoConfigBean mPhotoConfigBean;

    public String imgPath = "";

    public Uri mOutputUri = null;
    public File mInputFile;
    public File mOutputFile = null;

    //剪裁图片宽高比
    private int mAspectX = 1;
    private int mAspectY = 1;
    //剪裁图片大小
    private int mOutputX = 800;
    private int mOutputY = 480;

    public boolean mShouldCrop = false;//是否要裁剪（默认不裁剪）
    private boolean noLoading = false;//是否要加载了loading（默认显示）

    public RNTakePhotoPopupWindow(Activity context, PhotoConfigBean bean, TakePhotoListener takePhotoListener
    ) {
        mActivity = context;
        mTakePhotoListener = takePhotoListener;
        mPhotoConfigBean = bean;
        mShouldCrop = mPhotoConfigBean.isCrop();
        noLoading = mPhotoConfigBean.isLoading();
        imgPath = generateImgePath();
    }

    public void setCrop(boolean crop) {
        mShouldCrop = crop;
    }

    public void checkPhotoPermission() {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        if (!PermissionsUtil.hasPermission(mActivity, permissions)) {
            PermissionsUtil.requestPermission(mActivity, new PermissionListener() {
                @Override
                public void permissionGranted(@NonNull String[] permission) {
                    pickFromAlbum();
                }

                @Override
                public void permissionDenied(@NonNull String[] permission) {
                    if (mTakePhotoListener != null) {
                        mTakePhotoListener.onPhotoPermissionDenied();
                    }
                }
            }, permissions);
        } else {
            pickFromAlbum();
        }
    }


    public void checkCameraPermission() {
        String[] permissions = {Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        if (!PermissionsUtil.hasPermission(mActivity, permissions)) {
            PermissionsUtil.requestPermission(mActivity, new PermissionListener() {
                @Override
                public void permissionGranted(@NonNull String[] permission) {
                    takePhoto();
                }

                @Override
                public void permissionDenied(@NonNull String[] permission) {
                    if (mTakePhotoListener != null) {
                        mTakePhotoListener.onTakePhotoPermissionDenied();
                    }
                }
            }, permissions);
        } else {
            takePhoto();
        }
    }


    private void pickFromAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        mActivity.startActivityForResult(intent, REQ_SELECT_PHOTO);
    }

    private void takePhoto() {

        File imgFile = new File(imgPath);
        if (!imgFile.getParentFile().exists()) {
            imgFile.getParentFile().mkdirs();
        }
        Uri imgUri = null;

        /*
        * 1.现象
            在项目中调用相机拍照和录像的时候，android4.x,Android5.x,Android6.x均没有问题,在Android7.x下面直接闪退
           2.原因分析
            Android升级到7.0后对权限又做了一个更新即不允许出现以file://的形式调用隐式APP，需要用共享文件的形式：content:// URI
           3.解决方案
            下面是打开系统相机的方法，做了android各个版本的兼容:
        * */

        if (Build.VERSION.SDK_INT < 24) {
            // 从文件中创建uri
            imgUri = Uri.fromFile(imgFile);
        } else {
            //兼容android7.0 使用共享文件的形式
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, imgFile.getAbsolutePath());
            imgUri = mActivity.getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        }


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        mActivity.startActivityForResult(intent, REQ_TAKE_PHOTO);


    }

    /**
     * @saveFileId 图片保存的
     * @photoId 传给网商用的
     * @fileUrl 最终展示用的
     */
    public interface TakePhotoListener {
        void onSuccess(String fileUrl);

        void onCancel();

        void onPhotoPermissionDenied();

        void onTakePhotoPermissionDenied();
    }

    public void attachToActivityForResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQ_TAKE_PHOTO://拍照
                    mInputFile = new File(imgPath);
                    if (mShouldCrop) {//裁剪
                        mOutputFile = new File(generateImgePath());
                        mOutputUri = Uri.fromFile(mOutputFile);
                        startCrop(Uri.fromFile(mInputFile));
                        //
                    } else {//不裁剪
                        mOutputUri = Uri.fromFile(mInputFile);
                        lubanZip(mInputFile);
                    }
                    break;
                case REQ_SELECT_PHOTO://图库
                    if (data != null) {
                        Uri sourceUri = data.getData();
                        if (sourceUri == null) return;
                        String filePath = sourceUri.toString();
                        if (filePath.startsWith("file://")) { // 兼容某些文件管理器返回的 file 类型的路径统一转化成 content 路径
                            filePath = filePath.replace("file://", "");
                            sourceUri = getImageContentUri(mActivity, new File(filePath));
                        }
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor cursor = mActivity.managedQuery(sourceUri, proj, null, null, null);
                        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        String imgPath = cursor.getString(columnIndex);
                        mInputFile = new File(imgPath);

                        if (mShouldCrop) {//裁剪
                            mOutputFile = new File(generateImgePath());
                            mOutputUri = Uri.fromFile(mOutputFile);
                            startCrop(Uri.fromFile(mInputFile));
                        } else {//不裁剪
                            mOutputUri = Uri.fromFile(mInputFile);
                            lubanZip(mInputFile);
                        }
                    }
                    break;

                case REQ_CROP_PHOTO://crop
                    if (data != null) {
                    } else {
                        final Uri resultUri = UCrop.getOutput(data);
                        File file = new File(resultUri.getPath());
                        if (resultUri != null) {
                            lubanZip(file);
                        }
                    }
                    break;
            }
        } else {

            if (requestCode == REQ_TAKE_PHOTO) {
                if (data == null) {
                    String path = FileUtil.getPath();
                    if (path != null) {
                        File file = new File(path);
                        if (file.delete()) {
                            LogUtils.i("takephoto", "delete ok" + path);
                        } else {
                            LogUtils.i("takephoto", "delete fail" + path);
                        }
                    }
                }
            }
            if (mTakePhotoListener != null) {
                mTakePhotoListener.onCancel();
            }
        }
    }


//    public void zoomPhoto(File inputFile, File outputFile) {
//        File parentFile = outputFile.getParentFile();
//        if (!parentFile.exists()) {
//            parentFile.mkdirs();
//        }
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            intent.setDataAndType(getImageContentUri(mActivity, inputFile), "image/*");
//        } else {
//            intent.setDataAndType(Uri.fromFile(inputFile), "image/*");
//        }
//        intent.putExtra("crop", "true");
//        intent.putExtra("scale", true);
//
//        //设置剪裁图片宽高比
////        intent.putExtra("aspectX", mAspectX);
////        intent.putExtra("aspectY", mAspectY);
//
//        //设置剪裁图片大小
////        intent.putExtra("outputX", mOutputX);
////        intent.putExtra("outputY", mOutputY);
//
//        // 是否返回uri
//        intent.putExtra("return-data", false);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile));
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//
//        mActivity.startActivityForResult(intent, REQ_CROP_PHOTO);
//    }

    private void startCrop(@NonNull Uri uri) {
        File imgFile = new File(imgPath);
        int maxWidth;
        if (mPhotoConfigBean.getWidth() == 0) {
            maxWidth = 500;
        } else {
            maxWidth = mPhotoConfigBean.getWidth();
        }
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(imgFile)).withMaxResultSize(maxWidth, maxWidth);

        uCrop = advancedConfig(uCrop);

//    if (requestMode == REQUEST_SELECT_PICTURE_FOR_FRAGMENT) {       //if build variant = fragment
//        setupFragment(uCrop);
//    } else {                                                        // else start uCrop Activity
        uCrop.start(mActivity, REQ_CROP_PHOTO);
        // }

    }

    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.PNG);

        options.setCompressionQuality(90);

        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);
        options.setShowCropGrid(false);
        return uCrop.withOptions(options);
    }

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            // ResultActivity.startWithUri(SampleActivity.this, resultUri);
        } else {
            Toast.makeText(mActivity, "裁剪失败", Toast.LENGTH_SHORT).show();
        }
    }
    //////////////

    /**
     * 安卓7.0裁剪根据文件路径获取uri
     */
    private Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 产生图片的路径，带文件夹和文件名，文件名为当前毫秒数
     */
    public String generateImgePath() {
        return getExternalStoragePath() + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg";
        //        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg";//测试用
    }


    /**
     * 获取SD下的应用目录
     */
    private String getExternalStoragePath() {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        sb.append(File.separator);
        String ROOT_DIR = "Android/data/" + mActivity.getPackageName();
        sb.append(ROOT_DIR);
        sb.append(File.separator);
        return sb.toString();
    }

    public static File compressBmpToFile(Bitmap bmp, File file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;//个人喜欢从80开始,
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.size() > 2 * 1024 * 1024) {
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private void lubanZip(File file) {
        int maxSize = mPhotoConfigBean.getMaxSize() == 0 ? 500 : mPhotoConfigBean.getMaxSize();
        Luban.with(mActivity)
                .load(file)
                .ignoreBy(maxSize)
                .setTargetDir(getExternalStoragePath())
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        Log.e(TAG, "insertPhoto: =========>" + file.getAbsolutePath());
                        mTakePhotoListener.onSuccess(file.getAbsolutePath());
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                    }
                }).launch();
    }

//    private void insertPhoto(File file, Uri outputImageUri) {
//
//        int width, height;
//
//        File newFile = null;
//
//        width = mPhotoConfigBean.getWidth();
//        height = mPhotoConfigBean.getHeight();
//
//        if (file != null) {
//
//            try {
//
//                CompressHelper.Builder builder = new CompressHelper.Builder(mActivity)
//                        .setDestinationDirectoryPath(Environment.getExternalStorageDirectory().getPath() + "/com.meicai.lsez")
//                        .setQuality(80);    // 默认压缩质量为80
//
//                if (mPhotoConfigBean.getPhotoType() == null) {
//                    builder.setMaxWidth(width).setMaxHeight(height);
//                }
//
//                newFile = builder
//                        .build()
//                        .compressToFile(file);
//
//            } catch (NullPointerException e) {
//                LogUtils.e(e);
//            }
//
//            if (newFile != null) {
//                Log.e(TAG, "insertPhoto: =========>" + newFile.getAbsolutePath());
//                mTakePhotoListener.onSuccess(newFile.getAbsolutePath());
//            } else {
//                MCToastUtils.showShortToast(mActivity, "请选择jpeg png照片");
//            }
//
//        }
//    }

}
