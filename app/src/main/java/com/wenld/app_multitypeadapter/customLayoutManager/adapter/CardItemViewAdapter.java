package com.wenld.app_multitypeadapter.customLayoutManager.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.wenld.app_multitypeadapter.R;
import com.wenld.app_multitypeadapter.customLayoutManager.layoutManager.CardItemView;
import com.wenld.multitypeadapter.base.MultiItemView;
import com.wenld.multitypeadapter.base.ViewHolder;

import java.util.Random;

/**
 * <p/>
 * Author: 温利东 on 2017/6/14 11:52.
 * http://www.jianshu.com/u/99f514ea81b3
 * github: https://github.com/LidongWen
 */

public class CardItemViewAdapter extends MultiItemView<String> {
    private static final int[] COLORS = {0xff00FFFF, 0xffDEB887, 0xff5F9EA0,
            0xff7FFF00, 0xff6495ED, 0xffDC143C,
            0xff008B8B, 0xff006400, 0xff2F4F4F,
            0xffFF69B4, 0xffFF00FF, 0xffCD5C5C,
            0xff90EE90, 0xff87CEFA, 0xff800000};
    @NonNull
    @Override
    public int getLayoutId() {
        return R.layout.item_card;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final @NonNull  String item, int position) {
        holder.setText(R.id.text, item);
        CardItemView cardItemView=holder.getView(R.id.item);
        cardItemView.setCardColor(randomColor());
        cardItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), item, Toast.LENGTH_SHORT).show();
            }
        });

    }
    private int randomColor() {
        return COLORS[new Random().nextInt(COLORS.length)];
    }

}
