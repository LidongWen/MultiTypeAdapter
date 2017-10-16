package com.wenld.multitypeadapter.wrapper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.wenld.multitypeadapter.MultiTypeAdapter;
import com.wenld.multitypeadapter.base.ICoustomAdapter;
import com.wenld.multitypeadapter.base.MultiItemView;
import com.wenld.multitypeadapter.base.OnItemClickListener;
import com.wenld.multitypeadapter.base.ViewHolder;
import com.wenld.multitypeadapter.bean.GroupStructure;
import com.wenld.multitypeadapter.sticky.StickyHeaderAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenld on 2017/10/3.
 */

public class GroupWrapper extends RecyclerView.Adapter<ViewHolder> implements OnItemClickListener , ICoustomAdapter,StickyHeaderAdapter {
    MultiTypeAdapter multiTypeAdapter = new MultiTypeAdapter();

    private IExpandListener listener;
    OnItemClickListener<Object> onItemClickListener;
    RecyclerView recyclerView;

    GroupWrapperDateHelper helper = new GroupWrapperDateHelper();

    public <T> GroupWrapper register(@NonNull Class<? extends T> clazz, @NonNull MultiItemView<T> multiItemView) {
        multiTypeAdapter.register(clazz, multiItemView);
        return this;
    }

    public GroupWrapper(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        multiTypeAdapter.setOnItemClickListener(this);
    }

    @Override
    public int getItemViewType(int position) {
        return multiTypeAdapter.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return multiTypeAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        multiTypeAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return multiTypeAdapter.getItemCount();
    }


    public GroupWrapperDateHelper getHelper() {
        return helper;
    }

    public void setGroupList(List<GroupStructure> groupList) {
        helper.sourceList = groupList;
        helper.calculateList();
        multiTypeAdapter.setItems(helper.adapterList);
    }


    public void setListener(IExpandListener listener) {
        this.listener = listener;
    }


    public void expandOrShrikGroup(int groupPosition) {
        int position = helper.getAdapterPositionForGroupHeaderPostition(groupPosition);

        expandOrShrikGroup(recyclerView.findViewHolderForAdapterPosition(position), helper.sourceList.get(groupPosition).parent, position);
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {
        expandOrShrikGroup(holder, o, position);
    }

    public void setOnItemClickListener(OnItemClickListener<Object> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void expandOrShrikGroup(RecyclerView.ViewHolder holder, Object o, int position) {
        boolean needopen = true;
        if (helper.isGroupHeader(position)) {
            for (GroupStructure groupStructure : helper.openGroupList) {
                if (groupStructure.equalParent(o)) {
                    needopen = false;
                    notifyDataForShrik(groupStructure, position);
                    if (listener != null) {
                        listener.onShrink(holder, o, position);
                    }
                    break;
                }
            }
            if (needopen) {
                for (GroupStructure needAddGroupStrure : helper.sourceList) {
                    if (needAddGroupStrure.equalParent(o)) {
                        notifyDataForExpand(needAddGroupStrure, position);
                        if (listener != null) {
                            listener.onExpand(holder, o, position);
                        }
                        break;
                    }
                }
            }
        }

        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(holder.itemView, holder, o, position);
        }
    }

    void notifyDataForExpand(GroupStructure expandObj, int postiton) {
        helper.notifyDataForExpand(expandObj,postiton);

        if (expandObj.getChildrenCount() > 0) {
            if (helper.isAnimtor) {
                notifyItemRangeInserted(postiton + 1, expandObj.getChildrenCount());
            } else {
                notifyDataSetChanged();
            }
        }
    }

    void notifyDataForShrik(GroupStructure shrikObj, int postiton) {
        helper.notifyDataForShrik(shrikObj);
        if (shrikObj.getChildrenCount() > 0) {
            if (helper.isAnimtor) {
                notifyItemRangeRemoved(postiton + 1, shrikObj.getChildrenCount());
            } else {
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {
        if (onItemClickListener != null)
            return onItemClickListener.onItemLongClick(view, holder, o, position);
        return false;
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder, int postion) {
        multiTypeAdapter.onViewAttachedToWindow(holder, postion);
    }

    @Override
    public boolean isHeader(int position) {
        return helper.isGroupHeader(position);
    }

    public interface IExpandListener {
        void onExpand(RecyclerView.ViewHolder holder, Object o, int position);

        void onShrink(RecyclerView.ViewHolder holder, Object o, int position);
    }

    public static class GroupWrapperDateHelper {
        public boolean isAnimtor = false;
        // 分组源数据
        public List<GroupStructure> sourceList;
        // 打开的分组列表
        public List<GroupStructure> openGroupList = new ArrayList<>();
        // 展示的数据集合
        public List<Object> adapterList;

        /**
         * 判断是否是分组头部
         *
         * @param position
         * @return
         */
        public boolean isGroupHeader(int position) {
            //越界
            if(adapterList.size()<position){
                return false;
            }
            Object currentObj = adapterList.get(position);
            for (GroupStructure group : sourceList) {
                if (group.equalParent(currentObj)) {
                    return true;
                }
            }
            return false;
        }
        // 转换数据
        private void calculateList() {
            if (adapterList == null) {
                adapterList = new ArrayList<>();
            }
            adapterList.clear();
            GroupStructure objGroupStructure;
            boolean isEqual = false;
            lableBreak:
            for (int j = 0; j < sourceList.size(); j++) {
                isEqual = false;
                objGroupStructure = sourceList.get(j);
                for (int i = 0; i < openGroupList.size(); i++) {
                    if (objGroupStructure.equalParent(openGroupList.get(i).parent)) {
                        isEqual = true;
                        break;
                    }
                }

                if (objGroupStructure.hasHeader()) {
                    adapterList.add(objGroupStructure.parent);
                }

                if (isEqual) {
                    if (objGroupStructure.getChildrenCount() > 0) {
                        adapterList.addAll(objGroupStructure.children);
                    }
                }
            }
        }
        //
        public int getAdapterPositionForGroupHeaderPostition(int groupPosition) {
            GroupStructure groupStructure = sourceList.get(groupPosition);
            int position = -1;
            for (int i = 0; i < adapterList.size(); i++) {
                Object group = adapterList.get(i);
                if (group == groupStructure.parent) {
                    position = i;
                    break;
                }
            }
            return position;
        }
        void notifyDataForExpand(GroupStructure expandObj,int adapterPostiton) {
            openGroupList.add(expandObj);
            if (expandObj.getChildrenCount() > 0) {
                adapterList.addAll(adapterPostiton + 1, expandObj.children);
            }
        }
        void notifyDataForShrik(GroupStructure shrikObj) {
            openGroupList.remove(shrikObj);
            if (shrikObj.getChildrenCount() > 0) {
                adapterList.removeAll(shrikObj.children);
            }
        }
    }
}
