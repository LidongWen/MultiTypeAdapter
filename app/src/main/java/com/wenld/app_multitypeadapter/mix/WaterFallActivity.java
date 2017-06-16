package com.wenld.app_multitypeadapter.mix;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.wenld.app_multitypeadapter.R;
import com.wenld.app_multitypeadapter.decoration.ItemDecoration;
import com.wenld.app_multitypeadapter.manyData.adapter.ItemVIew01;
import com.wenld.app_multitypeadapter.manyData.adapter.ItemVIew02;
import com.wenld.app_multitypeadapter.manyData.adapter.ItemVIew03;
import com.wenld.app_multitypeadapter.manyData.adapter.ItemVIewNormal;
import com.wenld.app_multitypeadapter.manyData.bean.Bean01;
import com.wenld.app_multitypeadapter.manyData.bean.Bean02;
import com.wenld.app_multitypeadapter.manyData.bean.Bean03;
import com.wenld.app_multitypeadapter.one2many.Bean04;
import com.wenld.app_multitypeadapter.one2many.adapter.ItemVIew06;
import com.wenld.multitypeadapter.MultiTypeAdapter;

import java.util.ArrayList;
import java.util.List;

public class WaterFallActivity extends AppCompatActivity {
    private MultiTypeAdapter adapter;
    List<Object> items;
    int SPAN_COUNT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multidata);

        adapter = new MultiTypeAdapter();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rlv_multidata);

        initRegister();

        setLayoutManager(recyclerView);

        int space = getResources().getDimensionPixelSize(R.dimen.normal_space);
        recyclerView.addItemDecoration(new ItemDecoration(space));
        recyclerView.setAdapter(adapter);

        initData();

//        layoutManager.setSpanSizeLookup(spanSizeLookup);

        adapter.setItems(items);
        adapter.notifyDataSetChanged();
    }

    private void initRegister() {
        adapter.register(String.class, new ItemVIewNormal());
        adapter.register(Bean01.class, new ItemVIew01());
        adapter.register(Bean02.class, new ItemVIew02());
        adapter.register(Bean03.class, new ItemVIew03());
        adapter.register(Bean04.class, new ItemVIew06());
    }

    private void setLayoutManager(RecyclerView recyclerView) {
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                Object item = items.get(position);
                if (item instanceof Bean01) {
                    return 1;
                }
                if (item instanceof Bean02) {
                    return 1;
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
                return 1;
            }
        };
//        layoutManager.setSpanSizeLookup(spanSizeLookup);
//        layoutManager.setSpanCount(2);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initData() {
        items = new ArrayList<>();
        for (int j = 0; j < 10; j++) {
            items.add("混合式 瀑布流 \n 多数据 -> 多类型  单数据 -> 多类型");

            for (int i = 0; i < 1; i++) {
                items.add(new Bean02("bean02_" + i));
            }
            for (int i = 0; i < 2; i++) {
                items.add(new Bean01("bean01_" + i));
            }
            for (int i = 0; i < 1; i++) {
                items.add(new Bean02("bean02_" + i));
            }
            for (int i = 0; i < 2; i++) {
                items.add(new Bean01("bean01_" + i));
            }
            for (int i = 0; i < 3; i++) {
                Bean04 bean04 = new Bean04("bean04_" + i);
                if (i % 1 == 0) {
                    bean04.setType(Bean04.TYPE_ONE);
                }
                if (i % 2 == 0) {
                    bean04.setType(Bean04.TYPE_TWO);
                }
                if (i % 3 == 0) {
                    bean04.setType(Bean04.TYPE_THREE);
                }
                items.add(bean04);
            }
            for (int i = 0; i < 1; i++) {
                items.add(new Bean03("bean03_" + i));
            }
        }
    }
}
