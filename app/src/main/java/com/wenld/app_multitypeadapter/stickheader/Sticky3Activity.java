package com.wenld.app_multitypeadapter.stickheader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wenld.app_multitypeadapter.R;
import com.wenld.app_multitypeadapter.manyData.adapter.ItemVIew01;
import com.wenld.app_multitypeadapter.manyData.adapter.ItemVIew02;
import com.wenld.app_multitypeadapter.manyData.adapter.ItemVIew03;
import com.wenld.app_multitypeadapter.manyData.adapter.ItemVIewNormal;
import com.wenld.app_multitypeadapter.manyData.bean.Bean01;
import com.wenld.app_multitypeadapter.manyData.bean.Bean02;
import com.wenld.app_multitypeadapter.manyData.bean.Bean03;
import com.wenld.app_multitypeadapter.one2many.Bean04;
import com.wenld.multitypeadapter.MultiTypeAdapter;

import java.util.ArrayList;

/**
 * Created by wenld on 2017/10/14.
 */

public class Sticky3Activity extends AppCompatActivity {
    private MultiTypeAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky_three);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rlv_multiType);
        recyclerView.setNestedScrollingEnabled(false);
        setlayoutManager(recyclerView);

        adapter = new MultiTypeAdapter();
        adapter.register(String.class, new ItemVIewNormal());
        adapter.register(Bean01.class, new ItemVIew01());
        adapter.register(Bean02.class, new ItemVIew02());
        adapter.register(Bean03.class, new ItemVIew03());
        recyclerView.setAdapter(adapter);
        initData();
    }
    ArrayList<Object> items;
    private void initData() {

         items = new ArrayList<>();
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 8; i++) {
                items.add(new Bean01("bean01_" + i));
                items.add(j+"");
            }
            for (int i = 0; i < 2; i++) {
                items.add(new Bean02("bean02_" + i));
            }
//            for (int i = 0; i < 1; i++) {
//                items.add(new Bean03("bean03_" + j + "_" + i));
//            }
        }

        adapter.setItems(items);
        adapter.notifyDataSetChanged();
    }
    private  int SPAN_COUNT = 4;
    private void setlayoutManager(RecyclerView recyclerView) {
        final GridLayoutManager layoutManager = new GridLayoutManager(this, SPAN_COUNT);
        GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                Object item = items.get(position);
                if (item instanceof Bean01) {
                    return 1;
                }
                if (item instanceof Bean02) {
                    return 2;
                }
                if (item instanceof Bean03) {
                    return SPAN_COUNT;
                }
                if (item instanceof Bean04) {
                    return SPAN_COUNT;
                }
                if (item instanceof String) {
                    return SPAN_COUNT;
                }
                return SPAN_COUNT;
            }
        };
        layoutManager.setSpanSizeLookup(spanSizeLookup);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
