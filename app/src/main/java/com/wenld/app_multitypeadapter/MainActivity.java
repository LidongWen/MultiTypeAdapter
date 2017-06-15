package com.wenld.app_multitypeadapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wenld.app_multitypeadapter.decoration.ItemDecoration;
import com.wenld.app_multitypeadapter.manyData.MultiDataActivity;
import com.wenld.app_multitypeadapter.mix.MixActivity;
import com.wenld.app_multitypeadapter.mix.WaterFallActivity;
import com.wenld.app_multitypeadapter.one2many.One2ManyActivity;
import com.wenld.multitypeadapter.MultiItemView;
import com.wenld.multitypeadapter.MultiTypeAdapter;
import com.wenld.multitypeadapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int SPAN_COUNT = 4;
    private MultiTypeAdapter adapter;
    List<ItemClass> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new MultiTypeAdapter();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rlv_multiType);
        adapter.register(ItemClass.class, new ItemVIew());


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        int space = getResources().getDimensionPixelSize(R.dimen.btn_space);
        recyclerView.addItemDecoration(new ItemDecoration(space));
        recyclerView.setAdapter(adapter);

        list.add(new ItemClass("MultiDataActivity", MultiDataActivity.class));
        list.add(new ItemClass("One2ManyActivity", One2ManyActivity.class));
        list.add(new ItemClass("MixActivity", MixActivity.class));
        list.add(new ItemClass("WaterFallActivity", WaterFallActivity.class));



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

    public class ItemVIew extends MultiItemView<ItemClass, ViewHolder> {
        @NonNull
        @Override
        protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
            View view = inflater.inflate(R.layout.item_one, parent, false);
            return new ViewHolder(inflater.getContext(), view);
        }

        @Override
        protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final ItemClass item, int position) {
            holder.setText(R.id.tv_item01, item.name);
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, item.classUri);
                    startActivity(intent);
                }
            });
        }
    }
}
