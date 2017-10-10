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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenld on 2017/10/3.
 */

public class GroupWrapper extends RecyclerView.Adapter<ViewHolder> implements OnItemClickListener ,ICoustomAdapter {
    MultiTypeAdapter multiTypeAdapter = new MultiTypeAdapter();
    List<GroupStructure> groupList;
    List<GroupStructure> openedList = new ArrayList<>();

    private IExpandListener listener;
    private List<Object> expandList;
    OnItemClickListener onItemClickListener;
    RecyclerView recyclerView;
    public <T> GroupWrapper register(@NonNull Class<? extends T> clazz, @NonNull MultiItemView<T> multiItemView) {
        multiTypeAdapter.register(clazz, multiItemView);
        return this;
    }

    public GroupWrapper(RecyclerView recyclerView) {
        this.recyclerView=recyclerView;
        multiTypeAdapter.setOnItemClickListener(this);
    }

    @Override
    public int getItemViewType(int position) {
        return judgeType(position);
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

    public int judgeType(int position) {
        return multiTypeAdapter.getItemViewType(position);
    }


    public List<GroupStructure> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<GroupStructure> groupList) {
        this.groupList = groupList;
        calculateList();
        multiTypeAdapter.setItems(expandList);
    }

    private void calculateList() {
        if (expandList == null) {
            expandList = new ArrayList<>();
        }
        expandList.clear();
        GroupStructure objGroupStructure;
        boolean isEqual = false;
        lableBreak:
        for (int j = 0; j < groupList.size(); j++) {
            isEqual = false;
            objGroupStructure = groupList.get(j);
            for (int i = 0; i < openedList.size(); i++) {
                if (objGroupStructure.equalParent(openedList.get(i).parent)) {
                    isEqual = true;
                    break;
                }
            }

            if (objGroupStructure.hasHeader()) {
                expandList.add(objGroupStructure.parent);
            }

            if (isEqual) {
                if (objGroupStructure.getChildrenCount() > 0) {
                    expandList.addAll(objGroupStructure.children);
                }
            }
        }
    }

    public void setListener(IExpandListener listener) {
        this.listener = listener;
    }

    public List<GroupStructure> getOpenedList() {
        return openedList;
    }

    public List<Object> getExpandList() {
        return expandList;
    }

    /**
     * 判断是否是分组头部
     * @param position
     * @return
     */
    public boolean isGroupHeader(int position){
        Object currentObj=expandList.get(position);
        for(  GroupStructure group:groupList){
            if(group.equalParent(currentObj)){
                return true;
            }
        }
        return false;
    }

    public void expandOrShrikGroup(int groupPosition) {
        GroupStructure groupStructure = groupList.get(groupPosition);
        int position = -1;
        for (int i = 0; i < expandList.size(); i++) {
            Object group = expandList.get(i);
            if (group == groupStructure.parent) {
                position = i;
                break;
            }
        }

        expandOrShrikGroup(recyclerView.findViewHolderForPosition(position), groupStructure.parent, position);
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {
        expandOrShrikGroup( holder, o, position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void expandOrShrikGroup(RecyclerView.ViewHolder holder, Object o, int position) {
        boolean needopen = true;
        for (GroupStructure groupStructure : openedList) {
            if (groupStructure.equalParent(o)) {
                needopen = false;
                openedList.remove(groupStructure);
                if (listener != null) {
                    listener.onShrink( holder, o, position);
                }
                break;
            }
        }
        if (needopen) {
            for (GroupStructure needAddGroupStrure : groupList) {
                if (needAddGroupStrure.equalParent(o)) {
                    openedList.add(needAddGroupStrure);
                    if (listener != null) {
                        listener.onExpand(holder, o, position);
                    }
                    if (needAddGroupStrure.getChildrenCount() > 0) {
                        calculateList();
                        notifyDataSetChanged();
                    }
                    break;
                }
            }
        } else {
            calculateList();
            notifyDataSetChanged();
        }
        if(onItemClickListener!=null){
            onItemClickListener.onItemClick(holder.itemView,holder,o,position);
        }
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {
        if(onItemClickListener!=null)
            return onItemClickListener.onItemLongClick(view,holder,o,position);
        return false;
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder, int postion) {
        multiTypeAdapter.onViewAttachedToWindow(holder,postion);
    }

    public interface IExpandListener {
        void onExpand(RecyclerView.ViewHolder holder, Object o, int position);

        void onShrink(RecyclerView.ViewHolder holder, Object o, int position);
    }
}
