package com.wenld.app_multitypeadapter.manyData.bean;

import android.support.annotation.DrawableRes;

import java.io.Serializable;

/**
 * <p/>
 * Author: 温利东 on 2017/6/14 11:47.
 * http://www.jianshu.com/u/99f514ea81b3
 * github: https://github.com/LidongWen
 */

public class Bean03 implements Serializable {
    public String title;
    public @DrawableRes int imgRes;
    public String imgUrl = "http://upload-images.jianshu.io/upload_images/she.png";
    public Bean03(String title) {
        this.title=title;
    }
}
