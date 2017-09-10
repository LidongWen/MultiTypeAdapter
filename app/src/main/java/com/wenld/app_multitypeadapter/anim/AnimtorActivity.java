package com.wenld.app_multitypeadapter.anim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.wenld.app_multitypeadapter.R;
import com.wenld.app_multitypeadapter.decoration.ItemBottomDecoration;
import com.wenld.multitypeadapter.CommonAdapter;
import com.wenld.multitypeadapter.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class AnimtorActivity extends AppCompatActivity  implements View.OnClickListener{
    private RecyclerView mRv;
    private Button btnAdd,btnRemove,btnUpdate;
    private List<String> mData=new ArrayList<>();
    private boolean isFirst=true;
    private Spinner mSpinner;
    private CommonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animtor);
        mRv= (RecyclerView) this.findViewById(R.id.rv_activity_animtor);
        mSpinner= (Spinner) this.findViewById(R.id.spinner);
        btnAdd= (Button) this.findViewById(R.id.btn_add);
        btnRemove= (Button) this.findViewById(R.id.btn_remove);
        btnUpdate= (Button) this.findViewById(R.id.btn_update);

        mRv.addItemDecoration(new ItemBottomDecoration(15));

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mRv.setItemAnimator(new FadeItemAnimator());
                        break;
                    case 1:
                        mRv.setItemAnimator(new SlideItemAnimator());
                        break;
                    case 2:
                        mRv.setItemAnimator(new RotateItemAnimator());
                        break;
                    case 3:
                        mRv.setItemAnimator(new ScaleItemAnimator());
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAdd.setOnClickListener(this);
        btnRemove.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        adapter=new CommonAdapter<String>(this,String.class,R.layout.header_one) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.tv_header,s);
            }
        };
        mRv.setAdapter(adapter);
        for(int i=0;i<50;i++){
            mData.add("我是"+i);
        }
        adapter.setItems(mData);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add:
                if (isFirst){
                    mData.add(1,"我是添加的");
                    adapter.notifyItemInserted(1);
                }else{
                    mData.add(1,"我是添加的");
                    mData.add(2,"i am add");
                    adapter.notifyItemRangeInserted(1,2);
                }
                break;
            case R.id.btn_remove:
                if (isFirst){
                    mData.remove(1);
                    adapter.notifyItemRemoved(1);
                }else{
                    mData.remove(1);
                    mData.remove(1);
                    adapter.notifyItemRangeRemoved(1,2);
                }
                break;
            case R.id.btn_update:
                if (isFirst) {
                    mData.set(1, "我是更新的");
                    adapter.notifyItemChanged(1);
                }else{
                    mData.set(1, "我是更新的");
                    mData.set(2, "我是更新的2");
                    adapter.notifyItemRangeChanged(1,2);
                }
                break;
            default:
                break;
        }
    }
}
