package com.neusoft.qiuxiangmei.iceboxtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

/*
邱祥梅版权所有
 */

public class Main3Activity extends AppCompatActivity {

    DBhelper dbHelper=new DBhelper(this);
    List<FoodBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        initData();
        //接收intent数据
        Intent intent = getIntent();

        //拆包，获取数据
        final int id = intent.getIntExtra("id",0);
        final String name = intent.getStringExtra("name");
        int amount = intent.getIntExtra("amount",10);
        String date = intent.getStringExtra("date");
        int imgid = intent.getIntExtra("imgid",R.drawable.baicai);

        //显示到控件
        ImageView imageView = findViewById(R.id.imageView4);
        imageView.setImageResource(imgid);
        TextView textView5 = findViewById(R.id.textView5);
        textView5.setText(name);
        final EditText editText = findViewById(R.id.editText_alteramount);
        editText.setText(String.valueOf(amount));
        final EditText editText1 = findViewById(R.id.editText_alterdate);
        editText1.setText(String.valueOf(date));

        //返回页面
        findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodBean bean = new FoodBean(1,"白菜",7,"1998/01/01",R.drawable.baicai);
                bean.date = editText1.getText().toString();
                bean.amount = Integer.parseInt(editText.getText().toString());
                dbHelper.updatefoodById(bean,id);
                Toast.makeText(Main3Activity.this,"数据更新完成："+name+" "+editText.getText().toString()+" "+editText1.getText().toString(), LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent();
                intent.setClass(Main3Activity.this,Main4Activity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.button8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initData() {
        dbHelper = new DBhelper(this);
        dbHelper.open();
//        dbHelper.initData();//这个方法只在投一次运行时调用，生成数据库中初始数据，之后就可以注释掉
        data = dbHelper.getAllFoods();
    }
}
