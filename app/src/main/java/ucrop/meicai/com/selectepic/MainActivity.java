package ucrop.meicai.com.selectepic;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.yalantis.ucrop.bean.CropBean;
import com.yalantis.ucrop.bean.PhotoConfigBean;
import com.yalantis.ucrop.util.RNTakePhotoPopupWindow;
import com.yalantis.ucrop.util.SizeUtil;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private RNTakePhotoPopupWindow mTakePhotoPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        switch (mediaType) {
//            case "gallery": // 打开图库
//                mTakePhotoPopupWindow.checkPhotoPermission();
//                break;
//            case "camera": // 打开相机
//                mTakePhotoPopupWindow.checkCameraPermission();
//                break;
//            default:
//                mPromise.reject(E_FAILED, "mediaType 只能是 'gallery' 或 'camera'");
//                mPromise = null;
//        }
    }

    public void initTakePhoto(CropBean cropBean) {
        boolean isCrop = cropBean.isCropping();
        PhotoConfigBean bean = new PhotoConfigBean();
        if (cropBean.getMaxWidth() == 0) {
            bean.setWidth(SizeUtil.dp2px(this, 300));
        } else {
            bean.setWidth(cropBean.getMaxWidth());
        }
        bean.setHeight(SizeUtil.dp2px(this, 300));
        bean.setPhotoType("-10000");
        bean.setCrop(isCrop);
        mTakePhotoPopupWindow = new RNTakePhotoPopupWindow(this, bean, new RNTakePhotoPopupWindow.TakePhotoListener() {
            @Override
            public void onSuccess(String fileUrl) {
                Log.e(TAG, "onSuccess: ========meicai========>" + fileUrl);
                fileUrl = "file://" + fileUrl;
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onPhotoPermissionDenied() {
                mTakePhotoPopupWindow = null;
            }

            @Override
            public void onTakePhotoPermissionDenied() {
            }
        });
    }

    /**
     * 图库选择照片
     */

    public void selectFromPic(View view) {
        CropBean cropBean = new CropBean();
        initTakePhoto(cropBean);

        mTakePhotoPopupWindow.checkPhotoPermission();

    }

    /**
     * 图库选择照片并裁减
     */
    public void selectFromPicAndCrop(View view) {
        CropBean cropBean = new CropBean();
        cropBean.setCropping(true);
        cropBean.setMaxSize(400);
        initTakePhoto(cropBean);
        mTakePhotoPopupWindow.checkPhotoPermission();

    }

    /**
     * 相机拍照
     */
    public void selectFromCamera(View view) {
        CropBean cropBean = new CropBean();
        initTakePhoto(cropBean);

        mTakePhotoPopupWindow.checkCameraPermission();

    }

    /**
     * 相机拍照并裁剪
     */
    public void selectFromCamearAndCrop(View view) {
        CropBean cropBean = new CropBean();
        cropBean.setCropping(true);
        cropBean.setMaxSize(400);
        initTakePhoto(cropBean);

        mTakePhotoPopupWindow.checkCameraPermission();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTakePhotoPopupWindow.attachToActivityForResult(requestCode, resultCode, data);
    }
}
