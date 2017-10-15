package com.wenld.multitypeadapter;

import com.wenld.multitypeadapter.bean.GroupStructure;
import com.wenld.multitypeadapter.wrapper.GroupWrapper;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    
    
    @Test
    public void calculateList(){
        List<GroupStructure> data=new ArrayList<>();
        List<GroupStructure> openedList = new ArrayList<>();

        GroupStructure groupStructure1=new GroupStructure();
        String title1="title1";
        List<String> children1=new ArrayList<>();
        children1.add("child1");
        children1.add("child2");
        groupStructure1.parent=title1;
        groupStructure1.children=children1;


        GroupStructure groupStructure2=new GroupStructure();
        String title2="title2";
        List<String> children2=new ArrayList<>();
        children2.add("child2");
        children2.add("child2");
        groupStructure2.parent=title2;
        groupStructure2.children=children2;

        GroupStructure groupStructure3=new GroupStructure();
        String title3="title3";
        List<String> children3=new ArrayList<>();
        children3.add("child3");
        children3.add("child3");
        groupStructure3.parent=title3;
        groupStructure3.children=children3;

        data.add(groupStructure1);
        data.add(groupStructure2);
        data.add(groupStructure3);

        openedList.add(groupStructure1);
        
        List<Object> expandList=new ArrayList<>();
        
        if(expandList==null) {
            expandList = new ArrayList<>();
        }
        expandList.clear();
        // TODO: 2017/10/3 各种变换操作
        GroupStructure objGroupStructure;
        boolean isEqual = false;
        lableBreak:
        for (int j = 0; j < data.size(); j++) {
            isEqual=false;
            objGroupStructure = data.get(j);
            for (int i = 0; i < openedList.size(); i++) {
                if (objGroupStructure.equalParent(openedList.get(i).parent)) {
                    isEqual = true;
                    break;
                }
            }

            if (objGroupStructure.hasHeader()) {
                expandList.add(objGroupStructure.parent);
            }

            if (isEqual) {
                if (objGroupStructure.getChildrenCount() > 0) {
                    expandList.addAll(objGroupStructure.children);
                }
            }
        }

        for(Object obj:expandList){
            System.out.println(FastJsonUtil.toJsonObject(obj));
        }
    }

    @Test
    public void testGroupWrapperOnItemClick(){
        GroupWrapper groupWrapper=new GroupWrapper();
        List<GroupStructure> data=new ArrayList<>();

        GroupStructure groupStructure1=new GroupStructure();
        String title1="title1";
        List<String> children1=new ArrayList<>();
        children1.add("child1");
        children1.add("child2");
        groupStructure1.parent=title1;
        groupStructure1.children=children1;


        GroupStructure groupStructure2=new GroupStructure();
        String title2="title2";
        List<String> children2=new ArrayList<>();
        children2.add("child2");
        children2.add("child2");
        groupStructure2.parent=title2;
        groupStructure2.children=children2;

        GroupStructure groupStructure3=new GroupStructure();
        String title3="title3";
        List<String> children3=new ArrayList<>();
        children3.add("child3");
        children3.add("child3");
        groupStructure3.parent=title3;
        groupStructure3.children=children3;

        data.add(groupStructure1);
        data.add(groupStructure2);
        data.add(groupStructure3);


        groupWrapper.setGroupList(data);
        List<Object> expandList=new ArrayList<>();
        try {
            expandList = (List<Object>) getField(groupWrapper,"adapterList");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("--------------first");
        for(Object obj:expandList){
            System.out.println(FastJsonUtil.toJsonObject(obj));
        }
        groupWrapper.onItemClick(null,null,groupStructure1.parent,0);
        System.out.println("--------------展开后");
        for(Object obj:expandList){
            System.out.println(FastJsonUtil.toJsonObject(obj));
        }
        groupWrapper.onItemClick(null,null,groupStructure1.parent,0);
        System.out.println("--------------收缩后");
        for(Object obj:expandList){
            System.out.println(FastJsonUtil.toJsonObject(obj));
        }
    }
    public static Object getField(Object owner, String fieldName) throws Exception {
        Class<?> ownerClass = owner.getClass();

        Field field = ownerClass.getField(fieldName);
        field.setAccessible(true);
        Object property = field.get(owner);

        return property;
    }
}