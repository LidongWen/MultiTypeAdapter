package com.wenld.app_multitypeadapter.stickheader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.wenld.multitypeadapter.sticky.StickyAnyAdapter;


public class StickySigleTwoAdapter extends StickyAnyAdapter {
    public StickySigleTwoAdapter(Context context, RecyclerView.Adapter mAdapter) {
        super(context, mAdapter);
    }

    @Override
    public boolean isHeader(int position) {

        if (position  == 0 || position == 8 || position == 15|| position == 21|| position == 28) {
            return true;
        } else
            return false;
    }
}
