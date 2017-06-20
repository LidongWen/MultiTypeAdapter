package com.wenld.app_multitypeadapter.stickheader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.wenld.app_multitypeadapter.R;
import com.wenld.multitypeadapter.base.ViewHolder;
import com.wenld.multitypeadapter.sticky.StickyAdapter;


public class StickySigleTwoAdapter extends StickyAdapter {


    public StickySigleTwoAdapter(Context context, RecyclerView.Adapter mAdapter) {
        super(context, mAdapter);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    public boolean isHeader(int position) {
        if (position == 0 || position == 8 || position == 11) {
            return true;
        } else
            return false;
    }

    @Override
    public ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int position) {
        final View view;
        if (position == 0) {
            view = mInflater.inflate(R.layout.header_one, parent, false);
        } else {
            view = mInflater.inflate(R.layout.header_two, parent, false);
        }
        return new ViewHolder(mInflater.getContext(), view);
    }

    @Override
    public void onBindHeaderViewHolder(ViewHolder viewholder, int position) {

    }
}
