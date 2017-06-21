package com.wenld.multitypeadapter.sticky;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wenld.multitypeadapter.base.ViewHolder;


public abstract class StickyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        StickyHeaderAdapter<ViewHolder> {

    protected LayoutInflater mInflater;
    private Context mContext;

    protected RecyclerView.Adapter mAdapter;

    @Override
    public int getItemViewType(int position) {
        return mAdapter.getItemViewType(position);
    }

    public StickyAdapter(Context context, RecyclerView.Adapter adapter) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mAdapter = adapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return mAdapter.onCreateViewHolder(viewGroup, i);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount();
    }


    @Override
    public ViewHolder onCreateHeaderViewHolder(ViewGroup parent,int postion) {
        final View view = mInflater.inflate(getLayoutId(), parent, false);
        return new ViewHolder(mInflater.getContext(), view);
    }

    protected abstract @LayoutRes
    int getLayoutId();

}
