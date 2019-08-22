package ucrop.meicai.com.selectepic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.meicai.yunshan.picselected.PicUtils;

import java.io.File;
import java.util.List;

public class MainActivity extends Activity {
    private String TAG = MainActivity.class.getSimpleName();
//    private RNTakePhotoPopupWindow mTakePhotoPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     * 图库选择照片
     */

    public void selectFromPic(View view) {
        PicUtils picUtils = new PicUtils(this, false, 500, new PicUtils.TakePhotoListener() {
            @Override
            public void onSuccess(String fileUrl) {
                Log.e(TAG, "onSuccess: ======>lalallala,得到的图片===》"+fileUrl );

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onPhotoPermissionDenied() {

            }

            @Override
            public void onTakePhotoPermissionDenied() {

            }
        });
        picUtils.selectFromPic();
    }

    /**
     * 图库选择照片并裁减
     */
    public void selectFromPicAndCrop(View view) {
        PicUtils picUtils = new PicUtils(this, true, 500, new PicUtils.TakePhotoListener() {
            @Override
            public void onSuccess(String fileUrl) {
                Log.e(TAG, "onSuccess: ======>lalallala,得到的图片===》"+fileUrl );

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onPhotoPermissionDenied() {

            }

            @Override
            public void onTakePhotoPermissionDenied() {

            }
        });
        picUtils.selectFromPic();
    }

    /**
     * 相机拍照
     */
    public void selectFromCamera(View view) {
        PicUtils picUtils = new PicUtils(this, false, 500, new PicUtils.TakePhotoListener() {
            @Override
            public void onSuccess(String fileUrl) {
                Log.e(TAG, "onSuccess: ======>lalallala,得到的图片===》"+fileUrl );

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onPhotoPermissionDenied() {

            }

            @Override
            public void onTakePhotoPermissionDenied() {

            }
        });
        picUtils.takePhoto();
    }

    /**
     * 相机拍照并裁剪
     */
    public void selectFromCamearAndCrop(View view) {
        PicUtils picUtils = new PicUtils(this, true, 500, new PicUtils.TakePhotoListener() {
            @Override
            public void onSuccess(String fileUrl) {
                Log.e(TAG, "onSuccess: ======>lalallala,得到的图片===》"+fileUrl );
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onPhotoPermissionDenied() {

            }

            @Override
            public void onTakePhotoPermissionDenied() {

            }
        });
        picUtils.takePhoto();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PicUtils.onActivityResult(requestCode, resultCode, data);
    }
}
