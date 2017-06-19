package com.wenld.app_multitypeadapter.stickheader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.wenld.app_multitypeadapter.R;
import com.wenld.app_multitypeadapter.stickheader.bean.GroupSingleBean;
import com.wenld.multitypeadapter.base.ViewHolder;
import com.wenld.multitypeadapter.sticky.StickyAdapter;

import java.util.List;


public class StickySigleAdapter extends StickyAdapter {
    private List<Integer> groupPositions;
    List<GroupSingleBean> groupBeans;
    private boolean type_one = true;

    public StickySigleAdapter(Context context, RecyclerView.Adapter mAdapter) {
        super(context, mAdapter);
    }

    public void setGroupPositions(List<Integer> groupPositions) {
        this.groupPositions = groupPositions;
    }

    public void setGroupBeans(List<GroupSingleBean> groupBeans) {
        this.groupBeans = groupBeans;
        type_one = !type_one;
    }

    @Override
    public boolean isHeader(int position) {
        if (groupPositions == null || groupPositions.isEmpty() || !groupPositions.contains(position)) {
            return false;
        } else
            return true;
    }

    @Override
    public void onBindHeaderViewHolder(final ViewHolder viewholder, final int position) {
        if (!type_one) {
            for (int i = groupPositions.size() - 1; i >= 0; i--) {
                if (position >= groupPositions.get(i)) {
                    viewholder.setText(R.id.tv_header, groupBeans.get(i).getTitle());
                    viewholder.setOnClickListener(R.id.tv_header, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("header onclick", " "+position);
                        }
                    });

                    break;
                }
            }
        }
    }

    @Override
    protected int getLayoutId() {
        if (!type_one)
            return R.layout.header_one;
        else return R.layout.header_two;
    }
}
