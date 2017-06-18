package com.wenld.app_multitypeadapter.stickheader.bean;

import com.wenld.app_multitypeadapter.manyData.bean.Bean01;
import com.wenld.app_multitypeadapter.manyData.bean.Bean02;

import java.io.Serializable;
import java.util.List;

/**
 * Author: 温利东 on 2017/6/17 13:12.
 * http://www.jianshu.com/u/99f514ea81b3
 * github: https://github.com/LidongWen
 */
public class GroupSingleBean implements Serializable{
    private String title;
    private List<String>  childs;

    public GroupSingleBean(String title, List<String> childs) {
        this.title = title;
        this.childs = childs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getChilds() {
        return childs;
    }

    public void setChilds(List<String> childs) {
        this.childs = childs;
    }
}
