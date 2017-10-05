package com.wenld.multitypeadapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Vamoose on 2016/1/11.
 */

public class FastJsonUtil {//利用FastJson解析JSON字符串

    public FastJsonUtil() {
    }

    /**
     * @param jsonString
     * @param cls
     * @param <T>
     * @return
     * @function 得到一个Object
     */
    public static <T> T getObject(String jsonString, Class<T> cls) {
        T t = null;
        try {
            t = JSON.parseObject(jsonString, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * @param jsonString
     * @param cls
     * @param <T>
     * @return
     * @function 得到一个List<T>
     */
    public static <T> List<T> getListObjects(String jsonString, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        try {
            list = JSON.parseArray(jsonString, cls);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<T>();
        }
        return list;
    }

    /**
     * @param jsonString
     * @return
     * @function 得到一个List<Map<String, Object>>
     */
    public static List<Map<String, Object>> getListMaps(String jsonString) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            list = JSON.parseObject(jsonString, new TypeReference<List<Map<String, Object>>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * @param jsonString
     * @return
     * @function 得到一个List<Map<String, Object>>
     */
    public static List<Map<String, String>> getListMapss(String jsonString) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        try {
            list = JSON.parseObject(jsonString, new TypeReference<List<Map<String, String>>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * @param obj
     * @return
     * @function 将对象转成Json字符串
     */
    public static String toJsonObject(Object obj) {
        return JSONObject.toJSONString(obj);
    }

}
