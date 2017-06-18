package com.wenld.app_multitypeadapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wenld.app_multitypeadapter.decoration.ItemDecoration;
import com.wenld.app_multitypeadapter.empty.EmptyActivity;
import com.wenld.app_multitypeadapter.itemTouch.ItemTouchActivity;
import com.wenld.app_multitypeadapter.manyData.MultiDataActivity;
import com.wenld.app_multitypeadapter.mix.MixActivity;
import com.wenld.app_multitypeadapter.mix.WaterFallActivity;
import com.wenld.app_multitypeadapter.one2many.One2ManyActivity;
import com.wenld.app_multitypeadapter.pull_load.PullLoadActivity;
import com.wenld.app_multitypeadapter.stickheader.StickHeaderActivity;
import com.wenld.app_multitypeadapter.stickheader.StickHeaderSingleActivity;
import com.wenld.multitypeadapter.CommonAdapter;
import com.wenld.multitypeadapter.base.OnItemClickListener;
import com.wenld.multitypeadapter.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private CommonAdapter adapter;
    List<ItemClass> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new CommonAdapter<ItemClass>(this, ItemClass.class, R.layout.item_one) {
            @Override
            protected void convert(ViewHolder holder, final ItemClass item, int position) {
                holder.setText(R.id.tv_item01, item.name);
            }
        };
        adapter.setOnItemClickListener(new OnItemClickListener<ItemClass>() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, ItemClass itemClass, int position) {
                Intent intent = new Intent(MainActivity.this, itemClass.classUri);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, ItemClass itemClass, int position) {
                return false;
            }

        });
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rlv_multiType);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        int space = getResources().getDimensionPixelSize(R.dimen.btn_space);
        recyclerView.addItemDecoration(new ItemDecoration(space));
        recyclerView.setAdapter(adapter);

        list.add(new ItemClass("MultiDataActivity", MultiDataActivity.class));
        list.add(new ItemClass("One2ManyActivity", One2ManyActivity.class));
        list.add(new ItemClass("MixActivity", MixActivity.class));
        list.add(new ItemClass("WaterFallActivity", WaterFallActivity.class));
        list.add(new ItemClass("pull-load", PullLoadActivity.class));
        list.add(new ItemClass("empty", EmptyActivity.class));
        list.add(new ItemClass("itemTouchHepler", ItemTouchActivity.class));
        list.add(new ItemClass("StickHeader", StickHeaderActivity.class));
        list.add(new ItemClass("StickHeaderSingle",StickHeaderSingleActivity.class));

        adapter.setItems(list);
        adapter.notifyDataSetChanged();
    }

    public class ItemClass {
        public String name;
        public Class classUri;

        public ItemClass(String name, Class classUri) {
            this.name = name;
            this.classUri = classUri;
        }
    }
}
