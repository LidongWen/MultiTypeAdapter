package com.wenld.multitypeadapter.sticky;

/**
 * Author: 温利东 on 2017/6/17 13:57.
 * http://www.jianshu.com/u/99f514ea81b3
 * github: https://github.com/LidongWen
 */
public class StickyControl {
    //多种形式
    public static StickyAnyHeader any(){
        return new StickyAnyHeader();
    }
    //
    public static StickyAnyHeader2 anyHeader2(){
        return new StickyAnyHeader2();
    }
}
