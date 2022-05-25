package com.xiaozeng.cameraapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private int takePhoto = 1;
    private Uri imageUri;
    private File outputImage;

    private Button btnTakePhoto;
    private ImageView imageView;
    private int choose_phone = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        imageView = findViewById(R.id.imageView);

        Button chooseFromAlbum = findViewById(R.id.choose_from_album);


        //  拍照按钮
        btnTakePhoto.setOnClickListener(view -> {
            //  创建File对象，用于存储拍照后的图片,命名为output_image.jdp
            //  存放在手机SD卡的应用关联缓存目录下
            outputImage = new File(getExternalCacheDir(), "output_image.jpg");
            if (outputImage.exists()) {
                outputImage.delete();
            }
            try {
                outputImage.createNewFile();
                //  如果运行设备的系统高于Android 7.0
                //  就调用FileProvider的getUriForFile()方法将File对象转换成一个封装过的Uri对象。
                //  该方法接收3个参数：Context对象， 任意唯一的字符串， 创建的File对象。
                //  这样做的原因：Android 7.0 开始，直接使用本地真实路径的Uri是被认为是不安全的，会抛出FileUriExposedException异常；
                //      而FileProvider是一种特殊的ContentProvider，他使用了和ContentProvider类似的机制对数据进行保护，可以选择性地将封装过的Uri共享给外部。
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = FileProvider.getUriForFile(this, "com.example.permissiontest.fileprovider", outputImage);
                } else {
                    //  否则，就调用Uri的fromFile()方法将File对象转换成Uri对象
                    imageUri = Uri.fromFile(outputImage);
                }
                //  启动相机
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                //  指定图片的输出地址,这样拍下的照片会被输出到output_image.jpg中。
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, takePhoto);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    openAlbum();
                }
            }
        });
    }

    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,choose_phone);//打开相册
    }
    @Override
    public void onRequestPermissionResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    Toast.makeText(this,"you denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == takePhoto) {
            if (resultCode == Activity.RESULT_OK) {
                //  将拍摄的照片显示出来
                try {
                    //  decodeStream()可以将output_image.jpg解析成Bitmap对象。
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    imageView.setImageBitmap(rotateIfRequired(bitmap));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 为了防止横屏照片时，返回产生的旋转，所以这里做的是将照片始终正常的竖屏显示
     * @param bitmap
     * @return
     */
    private Bitmap rotateIfRequired(Bitmap bitmap) {
        try {
            ExifInterface exifInterface = new ExifInterface(outputImage.getPath());
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) return rotateBitmap(bitmap, 90);
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) return rotateBitmap(bitmap, 180);
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) return rotateBitmap(bitmap, 270);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap rotateBitmap(Bitmap bitmap, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float)degree);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        //  将不再需要的Bitmap对象回收
        bitmap.recycle();
        return rotatedBitmap;
    }
}
