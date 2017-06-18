package com.wenld.app_multitypeadapter.stickheader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.wenld.app_multitypeadapter.R;
import com.wenld.multitypeadapter.sticky.StickyAdapter;
import com.wenld.multitypeadapter.base.ViewHolder;


public class StickySigleTwoAdapter extends StickyAdapter {


    public StickySigleTwoAdapter(Context context, RecyclerView.Adapter mAdapter) {
        super(context, mAdapter);
    }

    @Override
    public boolean isHeader(int position) {
        if (position % 10 == 0) {
            return true;
        } else
            return false;
    }

    @Override
    public void onBindHeaderViewHolder(final ViewHolder viewholder, final int position) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.header_two;
    }
}
