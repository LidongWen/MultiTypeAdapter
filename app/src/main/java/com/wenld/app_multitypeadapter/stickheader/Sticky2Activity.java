package com.wenld.app_multitypeadapter.stickheader;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wenld.app_multitypeadapter.R;

/**
 * Created by wenld on 2017/10/14.
 */

public class Sticky2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sticky);
        ImageView iv= (ImageView) findViewById(R.id.iv_sticky);
        Glide.with(iv.getContext())
                .load("http://upload-images.jianshu.io/upload_images/1599843-90dec187459c3612.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240")
//                .placeholder(R.mipmap.ic_launcher) // can also be a drawable
//                .error(R.mipmap.cheese) // will be displayed if the image cannot be loaded
                .centerCrop()
                .into(iv);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Log.e("开始替换图片","开始");
                        Glide.with(iv.getContext())
                                .load("http://upload-images.jianshu.io/upload_images/1599843-6bccd8b6bf4a90b7.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240")
//                .placeholder(R.mipmap.ic_launcher) // can also be a drawable
//                .error(R.mipmap.cheese) // will be displayed if the image cannot be loaded
                                .centerCrop()
                                .into(iv);
            }
        },2000);

    }
}
