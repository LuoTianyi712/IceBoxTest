package com.neusoft.qiuxiangmei.iceboxtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/*
邱祥梅版权所有
 */

class MyAdapter extends BaseAdapter {

    private Context context;
    private List<FoodBean> foodBeans;
    private LayoutInflater inflater;
    FoodBean selectedItemData;


    public FoodBean getSelectedItemData() {
        return selectedItemData;
    }

    //
    public void addItem(FoodBean bean){
        foodBeans.add(bean);
        notifyDataSetChanged();
    }


    //
    public MyAdapter(Context context, List<FoodBean> foodBeans){
        super();
        this.context = context;
        this.foodBeans = foodBeans;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return foodBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FoodBean f = foodBeans.get(position);
        Main2Activity.ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new Main2Activity.ViewHolder();
            convertView = inflater.inflate(R.layout.listitem, null);
            viewHolder.txt_name = (TextView) convertView
                    .findViewById(R.id.textView11);
            viewHolder.txt_amount = (TextView) convertView
                    .findViewById(R.id.textView15);
            viewHolder.txt_date = (TextView) convertView
                    .findViewById(R.id.textView20);
            viewHolder.imageView = (ImageView) convertView
                    .findViewById(R.id.imageView8);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (Main2Activity.ViewHolder) convertView.getTag();
        }
        //插入数据
        viewHolder.txt_name.setText(f.getName());
        viewHolder.txt_amount.setText(String.valueOf(f.getAmount()));
        viewHolder.txt_date.setText(String.valueOf(f.getDate()));
        viewHolder.imageView.setImageResource(f.getImgid());
        return convertView;
    }
}
