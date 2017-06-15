package com.wenld.app_multitypeadapter.one2many;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wenld.app_multitypeadapter.R;
import com.wenld.app_multitypeadapter.decoration.ItemDecoration;
import com.wenld.app_multitypeadapter.manyData.adapter.ItemVIewNormal;
import com.wenld.app_multitypeadapter.one2many.adapter.ItemVIew06;
import com.wenld.multitypeadapter.MultiTypeAdapter;

import java.util.ArrayList;
import java.util.List;

public class One2ManyActivity extends AppCompatActivity {
    private MultiTypeAdapter adapter;
    List<Object> items;
    int SPAN_COUNT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multidata);

        adapter = new MultiTypeAdapter();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rlv_multidata);
        adapter.register(Bean04.class, new ItemVIew06());
        adapter.register(String.class, new ItemVIewNormal());

        final GridLayoutManager layoutManager = new GridLayoutManager(this, SPAN_COUNT);
        GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                Object item = items.get(position);
                if (item instanceof Bean04) {
                    return SPAN_COUNT;
                }
                if (item instanceof String) {
                    return SPAN_COUNT;
                }
                return 1;
            }
        };
        layoutManager.setSpanSizeLookup(spanSizeLookup);
        recyclerView.setLayoutManager(layoutManager);
        int space = getResources().getDimensionPixelSize(R.dimen.normal_space);
        recyclerView.addItemDecoration(new ItemDecoration(space));
        recyclerView.setAdapter(adapter);

        items = new ArrayList<>();
        for(int j=0;j<10;j++) {
            items.add(" 单数据 -> 多类型  ");
            for (int i = 0; i < 6; i++) {
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
        }

        adapter.setItems(items);
        adapter.notifyDataSetChanged();
    }
}
