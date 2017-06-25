package com.wenld.app_multitypeadapter.customLayoutManager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.wenld.app_multitypeadapter.R;
import com.wenld.app_multitypeadapter.customLayoutManager.adapter.CardItemViewAdapter;
import com.wenld.app_multitypeadapter.customLayoutManager.layoutManager.CardLayoutManager;
import com.wenld.app_multitypeadapter.decoration.ItemDecoration;
import com.wenld.multitypeadapter.MultiTypeAdapter;

import java.util.ArrayList;
import java.util.List;

public class CardLayoutActivity extends AppCompatActivity {
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
        adapter.register(String.class, new CardItemViewAdapter());
    }

    private void setLayoutManager(RecyclerView recyclerView) {

        recyclerView.setLayoutManager(new CardLayoutManager(3,true));
    }

    private void initData() {
        items = new ArrayList<>();
        for (int j = 0; j < 50; j++) {
            items.add("item_"+j);
        }
    }
}
