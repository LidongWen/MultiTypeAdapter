package com.wenld.app_multitypeadapter.itemTouch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

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
import com.wenld.multitypeadapter.MultiTypeAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemTouchActivity extends AppCompatActivity implements ItemTouchMoveListener, StartDragListener {
    private static final int SPAN_COUNT = 4;
    private MultiTypeAdapter adapter;
    List<Object> items;
    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multidata);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rlv_multidata);

        adapter = new MultiTypeAdapter();
        adapter.register(String.class,new ItemVIewNormal());
        adapter.register(Bean01.class, new ItemVIew01());
        adapter.register(Bean02.class, new ItemVIew02());
        adapter.register(Bean03.class, new ItemVIew03());


        setLayoutManager(recyclerView);
        int space = getResources().getDimensionPixelSize(R.dimen.normal_space);
        recyclerView.addItemDecoration(new ItemDecoration(space));
        recyclerView.setAdapter(adapter);

        initData();

        //条目触摸帮助类
        ItemTouchHelper.Callback callback = new MyItemTouchHelperCallback(this);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        // 1.数据交换；2.刷新
        Collections.swap(items, fromPosition, toPosition);
        adapter.notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public boolean onItemRemove(int position) {
        items.remove(position);
        adapter.notifyItemRemoved(position);
        return true;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    private void initData() {
        items = new ArrayList<>();
        for(int j=0;j<10;j++) {
            items.add(" 多数据 -> 多类型  ");
            for (int i = 0; i < 6; i++) {
                items.add(new Bean01("bean01_" + i));
            }
            for (int i = 0; i < 2; i++) {
                items.add(new Bean02("bean02_" + i));
            }
//            for (int i = 0; i < 2; i++) {
//                items.add(new Bean03("bean03_" + i));
//            }
        }

        adapter.setItems(items);
        adapter.notifyDataSetChanged();
    }

    private void setLayoutManager(RecyclerView recyclerView) {
        final GridLayoutManager layoutManager = new GridLayoutManager(this, SPAN_COUNT);
        GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                Object item = items.get(position);
                if (item instanceof Bean01) {
                    return 2;
                }
                if (item instanceof Bean02) {
                    return 2;
                }
                if (item instanceof Bean03) {
                    return SPAN_COUNT;
                }
                if(item instanceof String){
                    return SPAN_COUNT;
                }
                return SPAN_COUNT;
            }
        };
        layoutManager.setSpanSizeLookup(spanSizeLookup);
        recyclerView.setLayoutManager(layoutManager);
    }
}
