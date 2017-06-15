package com.wenld.app_multitypeadapter.manyData.bean;

import android.support.annotation.DrawableRes;

import java.io.Serializable;

/**
 * <p/>
 * Author: 温利东 on 2017/6/14 11:47.
 * blog: http://blog.csdn.net/sinat_15877283
 * github: https://github.com/LidongWen
 */

public class Bean03 implements Serializable {
    public String title;
    public @DrawableRes int imgRes;
    public String imgUrl = "http://upload-images.jianshu.io/upload_images/1599843-876468433f5dfe91.jpg";
    public Bean03(String title) {
        this.title=title;
    }
}
