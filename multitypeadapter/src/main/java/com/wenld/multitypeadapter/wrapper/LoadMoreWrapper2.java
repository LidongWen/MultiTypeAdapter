package com.wenld.multitypeadapter.wrapper;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.wenld.multitypeadapter.base.ICoustomAdapter;
import com.wenld.multitypeadapter.base.ViewHolder;
import com.wenld.multitypeadapter.utils.WrapperUtils;


/**
 * 增加开关控制
 * Created by zhy on 16/6/23.
 */
public class LoadMoreWrapper2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ICoustomAdapter {
    public static final int ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 2;

    private RecyclerView.Adapter mInnerAdapter;
    private View mLoadMoreView;
    private int mLoadMoreLayoutId;

    private boolean hasLoadMore = true;
    private boolean isLoading = false;

    public LoadMoreWrapper2(RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;
    }

    private boolean hasLoadMore() {
        return hasLoadMore && (mLoadMoreView != null || mLoadMoreLayoutId != 0);
    }


    private boolean isShowLoadMore(int position) {
        return hasLoadMore() && (position >= mInnerAdapter.getItemCount());
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowLoadMore(position)) {
            return ITEM_TYPE_LOAD_MORE;
        }
        return mInnerAdapter.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_LOAD_MORE) {
            ViewHolder holder;
            if (mLoadMoreView != null) {
                holder = ViewHolder.createViewHolder(parent.getContext(), mLoadMoreView);
            } else {
                holder = ViewHolder.createViewHolder(parent.getContext(), parent, mLoadMoreLayoutId);
            }
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isShowLoadMore(position)) {
            if (mOnLoadMoreListener != null && !isLoading) {
                isLoading = true;
                Log.e("onBindViewHolder",isLoading+" "+position);
                mOnLoadMoreListener.onLoadMoreRequested();
            }
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {

                if (isShowLoadMore(position)) {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null) {
                    return oldLookup.getSpanSize(position);
                }
                return 1;
            }
        });
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (isShowLoadMore(holder.getLayoutPosition())) {
            setFullSpan(holder);
        } else {
            onViewAttachedToWindow(holder, holder.getLayoutPosition());
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder, int postion) {
        if (isShowLoadMore(holder.getLayoutPosition())) {
            setFullSpan(holder);
            return;
        }
        if (mInnerAdapter instanceof ICoustomAdapter) {
            ((ICoustomAdapter) mInnerAdapter).onViewAttachedToWindow(holder, postion);
            return;
        }
        mInnerAdapter.onViewAttachedToWindow(holder);

    }

    private void setFullSpan(RecyclerView.ViewHolder holder) {
        WrapperUtils.setFullSpan(holder);
    }

    @Override
    public int getItemCount() {
        return mInnerAdapter.getItemCount() + (hasLoadMore() ? 1 : 0);
    }


    public interface OnLoadMoreListener {
        void onLoadMoreRequested();
    }

    private OnLoadMoreListener mOnLoadMoreListener;

    public LoadMoreWrapper2 setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        if (loadMoreListener != null) {
            mOnLoadMoreListener = loadMoreListener;
        }
        return this;
    }

    public LoadMoreWrapper2 setLoadMoreView(View loadMoreView) {
        mLoadMoreView = loadMoreView;
        return this;
    }

    public LoadMoreWrapper2 setLoadMoreView(int layoutId) {
        mLoadMoreLayoutId = layoutId;
        return this;
    }

    public void loadingComplete() {
        isLoading = false;
    }

    public void setLoadMore(boolean b) {
        hasLoadMore = b;
        if (hasLoadMore) {
            notifyItemInserted(getItemCount());
        } else {
            notifyItemRemoved(getItemCount());
        }
    }

    public boolean isLoading() {
        return isLoading;
    }
}
