package com.neusoft.qiuxiangmei.iceboxtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;;

/*
邱祥梅版权所有
 */

public class MainActivity extends AppCompatActivity {

    String ur1="http://wthrcdn.etouch.cn/weather_mini?city=";
    TextView textView3;
    TextView textView4;
    TextView textView8;

    DBhelper dbHelper;
    List<FoodBean> data;
    static final String TABLE_NAME = "foods_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView3=findViewById(R.id.textView3);
        textView4=findViewById(R.id.textView4);
        textView8=findViewById(R.id.textView8);
        initData();

        SimpleDateFormat formatter=new SimpleDateFormat("yyyy年MM月dd日");
        Date curDate=new Date(System.currentTimeMillis());
        String string=formatter.format(curDate);

        TextView textView=findViewById(R.id.textView6);
        textView.setText(string);

        findViewById(R.id.button10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeatherMsg(ur1+"大方");
            }
        });

    findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i=new Intent(MainActivity.this, Main2Activity.class);
            startActivity(i);
        }
    });
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this, Main4Activity.class);
                startActivity(i);
            }
        });

        comparetime();
        stockout();
    }
    private void initData() {
        dbHelper = new DBhelper(this);
        dbHelper.open();
       // dbHelper.initData();//这个方法只在投一次运行时调用，生成数据库中初始数据，之后就可以注释掉
        data = dbHelper.getAllFoods();
    }
    /**
     * 向天气查询API发送GET请求
     * @param path
     */
    public void getWeatherMsg(final String path){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //线程执行的内容
                try {
                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5 * 1000);
                    connection.setReadTimeout(5 * 1000);
                    connection.connect();
                    //获得服务器的响应码
                    int response = connection.getResponseCode();
                    if(response == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        String html = dealResponseResult(inputStream);
                        //处理服务器相应结果
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = html;
                        mHandler.sendMessage(msg);
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    //错误处理
                    Message msg = new Message();
                    msg.what = 2;
                    msg.obj = e.toString();
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }
    private String dealResponseResult(InputStream inputStream) {
        StringBuilder html = new StringBuilder();      //存储处理结果
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String s;
            while ((s = reader.readLine()) != null) {
                html.append(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html.toString();
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    //成功返回结果
                    //   textView.setText(msg.obj.toString());
                    Gson gson=new Gson();
                    WeatherBean bean=gson.fromJson(msg.obj.toString(),WeatherBean.class);
                    textView3.setText(bean.getData().getCity()
                            +"\n当前温度"+bean.getData().getWendu()
                            +"\n感冒指数"+bean.getData().getGanmao()
                            +"\n今天最高温度"+bean.getData().getForecast().get(0).getHigh()

                    );
                    break;

            }
            return false;
        }
    });

       public void comparetime (){
        StringBuilder stringBuilder = new StringBuilder();
        Cursor cursor = dbHelper.db.query(TABLE_NAME,new String[]{"name","date"},null,null,null,null,null);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            Date date1 = null;
            Date date2 = null;
            try {
                date1 = dateFormat.parse(date);
                date2 = new Date(System.currentTimeMillis());

            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date1.getTime() < date2.getTime()) {
                stringBuilder.append(name + " ");
            }
        }textView4.setText("过期提示："+stringBuilder.toString());
        cursor.close();
    }

    public void stockout(){
        StringBuilder stringBuilder = new StringBuilder();
        Cursor cursor = dbHelper.db.query(TABLE_NAME,new String[]{"name","amount"},null,null,null,null,null);
        while (cursor.moveToNext())
        {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String amount = cursor.getString(cursor.getColumnIndex("amount"));
            int n = Integer.parseInt(amount);
            if(n==0){
                stringBuilder.append(name+" ");
            }
        }textView8.setText("缺货提示："+stringBuilder.toString());
        cursor.close();
    }
}
