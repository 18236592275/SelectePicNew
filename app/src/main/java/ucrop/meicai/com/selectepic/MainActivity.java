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
        PicUtils.selectFromPic(this, false, 500);
    }

    /**
     * 图库选择照片并裁减
     */
    public void selectFromPicAndCrop(View view) {
        PicUtils.selectFromPic(this, true, 500);
    }

    /**
     * 相机拍照
     */
    public void selectFromCamera(View view) {
        PicUtils.takePhoto(this, false, 500);
    }

    /**
     * 相机拍照并裁剪
     */
    public void selectFromCamearAndCrop(View view) {
        PicUtils.takePhoto(this, false, 500);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PicUtils.onActivityResult(requestCode, resultCode, data);
    }
}
