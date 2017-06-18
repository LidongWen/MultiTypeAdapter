package com.wenld.app_multitypeadapter.stickheader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wenld.app_multitypeadapter.R;
import com.wenld.app_multitypeadapter.decoration.ItemBottomDecoration;
import com.wenld.app_multitypeadapter.stickheader.bean.GroupSingleBean;
import com.wenld.multitypeadapter.sticky.StickyAdapter;
import com.wenld.multitypeadapter.sticky.StickyControl;
import com.wenld.multitypeadapter.CommonAdapter;
import com.wenld.multitypeadapter.base.OnItemClickListener;
import com.wenld.multitypeadapter.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class StickHeaderSingleActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CommonAdapter adapter;
    private List<Object> items;
    private List<Integer> groupPositions;
    private StickySigleAdapter stickyTestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multidata);
        recyclerView = (RecyclerView) findViewById(R.id.rlv_multidata);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new ItemBottomDecoration(getResources().getDimensionPixelSize(R.dimen.normal_space)));


        adapter = new CommonAdapter<String>(this, String.class, R.layout.item_one) {
            @Override
            protected void convert(ViewHolder holder, String o, int position) {
                holder.setText(R.id.tv_item01, o);
            }
        };

        initData();

        stickyTestAdapter = new StickySigleAdapter(this, adapter);
        stickyTestAdapter.setGroupPositions(groupPositions);
        stickyTestAdapter.setGroupBeans(groupBeans);
        StickyControl.single()
                .adapter(stickyTestAdapter)
                .setRecyclerView(recyclerView)
                .togo();
        recyclerView.setAdapter(stickyTestAdapter);


        adapter.setOnItemClickListener(new OnItemClickListener<String>() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, String o, int position) {
                changeData();

                StickyControl.single()
                        .adapter(stickyTestAdapter)
                        .setRecyclerView(recyclerView)
                        .immersion()
                        .togo();
                stickyTestAdapter.setGroupPositions(groupPositions);
                stickyTestAdapter.setGroupBeans(groupBeans);
                stickyTestAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, String o, int position) {
                return false;
            }
        });
    }

    private void initData() {

        netWork();

        items = new ArrayList<>();
        groupPositions = new ArrayList<>();

        calculationData();

        adapter.setItems(items);
    }

    private void changeData() {
        AddData4netWork();

        items.clear();
        groupPositions.clear();

        calculationData();
    }

    private void calculationData() {

        groupPositions.add(0);
        for (GroupSingleBean groupBean : groupBeans) {
            for (String s : groupBean.getChilds()) {
                items.add(s);
            }
            groupPositions.add(items.size());
        }
        groupPositions.remove(groupPositions.size() - 1);
    }


    public List<GroupSingleBean> groupBeans;

    private void netWork() {
        groupBeans = new ArrayList<>();
        for (int j = 0; j < 10; j++) {
            List<String> childs = new ArrayList<>();
            childs.add("click me change the header");
            for (int i = 0; i < 5; i++) {
                childs.add("click me !  childe_" + j + " _" + i);
            }
            GroupSingleBean groupBean = new GroupSingleBean(" groupTitle_" + j, childs);
            groupBeans.add(groupBean);
        }
    }

    private void AddData4netWork() {
        groupBeans = new ArrayList<>();
        for (int j = 0; j < 10; j++) {
            List<String> childs = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                childs.add("click me !   the changed Data_" + j + "_" + i);
            }
            GroupSingleBean groupBean = new GroupSingleBean(" new header Title_" + j, childs);
            groupBeans.add(groupBean);
        }
    }
}
