package com.wenld.app_multitypeadapter.group;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wenld.app_multitypeadapter.R;
import com.wenld.app_multitypeadapter.decoration.ItemDecoration;
import com.wenld.app_multitypeadapter.manyData.adapter.ItemVIew01;
import com.wenld.app_multitypeadapter.manyData.adapter.ItemVIew02;
import com.wenld.app_multitypeadapter.manyData.adapter.ItemVIew03;
import com.wenld.app_multitypeadapter.manyData.adapter.ItemVIewNormal;
import com.wenld.app_multitypeadapter.manyData.bean.Bean01;
import com.wenld.app_multitypeadapter.manyData.bean.Bean02;
import com.wenld.app_multitypeadapter.manyData.bean.Bean03;
import com.wenld.multitypeadapter.base.OnItemClickListener;
import com.wenld.multitypeadapter.base.ViewHolder;
import com.wenld.multitypeadapter.bean.GroupStructure;
import com.wenld.multitypeadapter.wrapper.GroupWrapper;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {
    private static final int SPAN_COUNT = 4;
    GroupWrapper groupWrapper;
    List<GroupStructure> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multidata);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rlv_multidata);
        groupWrapper=new GroupWrapper(recyclerView);
        groupWrapper.register(String.class, new ItemVIewNormal());
        groupWrapper.register(Bean01.class, new ItemVIew01());
        groupWrapper.register(Bean02.class, new ItemVIew02());
        groupWrapper.register(Bean03.class, new ItemVIew03());

        final GridLayoutManager layoutManager = new GridLayoutManager(this, SPAN_COUNT);
        GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                Object item = groupWrapper.getHelper().adapterList.get(position);
                if (item instanceof Bean01) {
                    return 1;
                }
                if (item instanceof Bean02) {
                    return 2;
                }
                if (item instanceof Bean03) {
                    return SPAN_COUNT;
                }
                return SPAN_COUNT;
            }
        };
        layoutManager.setSpanSizeLookup(spanSizeLookup);
        recyclerView.setLayoutManager(layoutManager);
        int space = getResources().getDimensionPixelSize(R.dimen.normal_space);
        recyclerView.addItemDecoration(new ItemDecoration(space));


        recyclerView.setAdapter(groupWrapper);

        initData();


    }

    private void initData() {
        items = new ArrayList<>();

        GroupStructure groupStructure1=new GroupStructure();
        String title1="title1";
        List<Object> children1=new ArrayList<>();
        children1.add(new Bean01("bean01_" +title1));
        children1.add(new Bean02("bean02_" + title1));
        children1.add(new Bean02("bean02_" + title1));

        groupStructure1.parent=title1;
        groupStructure1.children=children1;


        GroupStructure groupStructure2=new GroupStructure();
        String title2="title2";
        List<Object> children2=new ArrayList<>();
        children2.add(new Bean01("bean01_" +title2));
        children2.add(new Bean02("bean02_" + title2));
        children2.add(new Bean03("bean03_" + title2));

        groupStructure2.parent=title2;
        groupStructure2.children=children2;

        items.add(groupStructure1);
        items.add(groupStructure2);

        groupWrapper.setGroupList(items);
        groupWrapper.setListener(new GroupWrapper.IExpandListener() {
            @Override
                public void onExpand(RecyclerView.ViewHolder holder, Object o, int position) {
                if(holder!=null)
                ((ViewHolder)holder).setImageResource(R.id.imageView,R.mipmap.less);
            }

            @Override
            public void onShrink(RecyclerView.ViewHolder holder, Object o, int position) {
                if(holder!=null)
                    ((ViewHolder)holder).setImageResource(R.id.imageView,R.mipmap.more_unfold);
            }
        });
        groupWrapper.expandOrShrikGroup(0);
        groupWrapper.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {
                return false;
            }
        });
        groupWrapper.getHelper().isAnimtor=true;
//        groupWrapper.notifyDataSetChanged();
    }

}
