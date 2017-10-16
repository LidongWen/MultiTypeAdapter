package com.wenld.app_multitypeadapter.stickheader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.wenld.multitypeadapter.MultiTypeAdapter;
import com.wenld.multitypeadapter.sticky.StickyHeaderAdapter;


public class StickySigleTwoAdapter extends MultiTypeAdapter implements StickyHeaderAdapter {

    @Override
    public boolean isHeader(int position) {

        if (position  == 0 || position == 8 || position == 15|| position == 21|| position == 28) {
            return true;
        } else
            return false;
    }
}
