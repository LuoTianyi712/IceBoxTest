package com.neusoft.qiuxiangmei.iceboxtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
邱祥梅版权所有
 */

public class Main2Activity extends AppCompatActivity {
    ListView listView;
    List<FoodBean> foodBeans = new ArrayList<FoodBean>();
    MyAdapter adapter;

    private  DBhelper dbHelper = new DBhelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        listView=findViewById(R.id.listView);

        initData();

        foodBeans = dbHelper.getAllFoods();
        adapter=new MyAdapter(this,foodBeans);

        listView=findViewById(R.id.listView);
        listView.setAdapter(adapter);

     //事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return true;
            }
        });
    }

    private void initData() {
        dbHelper = new DBhelper(this);
        dbHelper.open();
        //dbHelper.initData();//这个方法只在投一次运行时调用，生成数据库中初始数据，之后就可以注释掉
        foodBeans = dbHelper.getAllFoods();
    }

    static class ViewHolder {
        public TextView txt_name;
        public TextView txt_amount;
        public TextView txt_date;
        public ImageView imageView;
    }
}
