package com.wenld.app_multitypeadapter.pull_load;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.wenld.app_multitypeadapter.R;
import com.wenld.app_multitypeadapter.decoration.ItemDecoration;
import com.wenld.app_multitypeadapter.manyData.adapter.ItemVIew01;
import com.wenld.app_multitypeadapter.manyData.adapter.ItemVIew02;
import com.wenld.app_multitypeadapter.manyData.adapter.ItemVIew03;
import com.wenld.app_multitypeadapter.manyData.adapter.ItemVIewNormal;
import com.wenld.app_multitypeadapter.manyData.bean.Bean01;
import com.wenld.app_multitypeadapter.manyData.bean.Bean02;
import com.wenld.app_multitypeadapter.manyData.bean.Bean03;
import com.wenld.app_multitypeadapter.utils.MainThreadExecutor;
import com.wenld.multitypeadapter.MultiTypeAdapter;
import com.wenld.multitypeadapter.wrapper.LoadMoreWrapper2;

import java.util.ArrayList;
import java.util.List;

public class PullLoadActivity extends AppCompatActivity {
    private static final int SPAN_COUNT = 4;
    LoadMoreWrapper2 loadMoreWrapper2;
    private MultiTypeAdapter adapter;
    List<Object> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multidata);
        adapter = new MultiTypeAdapter();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rlv_multidata);
        adapter.register(String.class, new ItemVIewNormal());
        adapter.register(Bean01.class, new ItemVIew01());
        adapter.register(Bean02.class, new ItemVIew02());
        adapter.register(Bean03.class, new ItemVIew03());

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
                return SPAN_COUNT;
            }
        };
        layoutManager.setSpanSizeLookup(spanSizeLookup);
        recyclerView.setLayoutManager(layoutManager);
        int space = getResources().getDimensionPixelSize(R.dimen.normal_space);
        recyclerView.addItemDecoration(new ItemDecoration(space));

        loadMoreWrapper2 = new LoadMoreWrapper2(adapter);
        loadMoreWrapper2.setLoadMoreView(LayoutInflater.from(this).inflate(R.layout.default_loading, recyclerView, false));
        recyclerView.setAdapter(loadMoreWrapper2);

        initData();

        loadMoreWrapper2.setOnLoadMoreListener(new LoadMoreWrapper2.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                addPage();
            }
        });
    }

    private void initData() {
        items = new ArrayList<>();
        items.add(" 加载三次数据 关闭加载功能 ");
        for (int i = 0; i < 8; i++) {
            items.add(new Bean01("bean01_" + i));
        }
        for (int i = 0; i < 6; i++) {
            items.add(new Bean02("bean02_" + i));
        }
        for (int i = 0; i < 1; i++) {
            items.add(new Bean03("bean03_" + i));
        }
        adapter.setItems(items);
//        adapter.notifyDataSetChanged();
        loadMoreWrapper2.notifyDataSetChanged();
    }

    int count = 0;

    private void addPage() {
        if (count > 1) {
            new MainThreadExecutor(2000).execute(
                    new Runnable() {
                        @Override
                        public void run() {
                            noMore();
                        }
                    }
            );
            return;
        }
        count++;

        new MainThreadExecutor(1500).execute(new Runnable() {
            @Override
            public void run() {
                items.add(" 加载第 " + count + "次");
                for (int i = 0; i < 8; i++) {
                    items.add(new Bean01("bean01_" + i));
                }
                for (int i = 0; i < 6; i++) {
                    items.add(new Bean02("bean02_" + i));
                }
                for (int i = 0; i < 1; i++) {
                    items.add(new Bean03("bean03_" + i));
                }
                loadMoreWrapper2.loadingComplete();
                loadMoreWrapper2.notifyDataSetChanged();
            }
        });
    }

    public void noMore() {
        loadMoreWrapper2.setLoadMore(false);
    }
}
