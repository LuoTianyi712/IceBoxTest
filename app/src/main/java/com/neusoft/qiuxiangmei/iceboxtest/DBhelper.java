package com.neusoft.qiuxiangmei.iceboxtest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/*
邱祥梅版权所有
 */
//数据库访问封装
public class DBhelper {
    Context context;
    SQLiteDatabase db;
    static final String DB_NAME = "foods.db";
    static final String TABLE_NAME = "foods_list";

    public static final String[] names = {
            "白菜", "冬笋", "花菜", "黄瓜", "鸡翅",
            "卷心菜", "苦瓜", "莲藕", "蘑菇", "牛肉",
            "排骨", "茄子", "芹菜", "秋葵", "生菜",
            "土豆", "西红柿", "猪肉",
    };
    public static final int[] imgid = {
            R.drawable.baicai,
            R.drawable.dongsun,
            R.drawable.huacai,
            R.drawable.huanggua,
            R.drawable.jichi,
            R.drawable.juanxincai,
            R.drawable.kugua,
            R.drawable.lianou,
            R.drawable.mogu,
            R.drawable.niurou,
            R.drawable.paigu,
            R.drawable.qiezi,
            R.drawable.qincai,
            R.drawable.qiukui,
            R.drawable.shengcai,
            R.drawable.tudou,
            R.drawable.xihongshi,
            R.drawable.zhurou,
    };

    /**
     * 数据库构造方法
     */
    DBhelper(Context c) {
        this.context = c;
    }

    /**
     * 打开数据方法
     */
    public boolean open() {
        String path = context.getFilesDir() + "/" + DB_NAME;
        db = SQLiteDatabase.openOrCreateDatabase(path, null);
        String sql="create table if not exists "+TABLE_NAME+"(" +
                "id INTEGER primary key autoincrement, " +
                "name varchar(30), " +
                "amount int," +
                "date date, " +
                "imgid int)";
        try{
            db.execSQL(sql);
            return true;
        }catch (Exception e){
            Log.e(TABLE_NAME,"open_db:err"+e.toString());
            return false;
        }
    }

    /**
     * 关闭数据库
     */
    public void close() {
        db.close();
    }
    /***
     * 初始化数据库的数据
     * 这个方法只在投一次运行时调用，生成数据库中初始数据，之后就可以注释掉
     */
    public void initData() {
       deleteAllfoods();
        for (int i = 0; i < 18;i++) {
            FoodBean bean = new FoodBean(i, names[i],3,"2020/06/16",imgid[i]);
            inserfood(bean);
        }
    }
    /*
     * 插入食物信息
     */
    public boolean inserfood(FoodBean bean) {
        ContentValues values = new ContentValues();
        values.put("name", bean.getName());
        values.put("amount", bean.getAmount());
        values.put("date",bean.getDate());
        values.put("imgid", bean.getImgid());
        long i = db.insert(TABLE_NAME, null, values);
        if (i > 0) return true;
        else return false;
    }
    /**
     * 获取所有食物信息列表
     */
    public List<FoodBean> getAllFoods() {
        List<FoodBean> data = new ArrayList<>();
        Cursor c = db.query(TABLE_NAME, null, null,
                null, null, null, null);
        while (c.moveToNext()) {
            int id=c.getInt(0);
            String name = c.getString(1);
            int amount = c.getInt(2);
            String date = c.getString(3);
            int imgid = c.getInt(4);
            FoodBean foodBean = new FoodBean(id,name,amount,date,imgid);
            data.add(foodBean);
        }
        return data;
    }

    public int deleteFoodById(int id) {
        return db.delete(TABLE_NAME, "id=?", new String[]{String.valueOf(id)});
    }

    public void deleteAllfoods(){
        db.delete(TABLE_NAME,null,null);
    }

    public boolean updatefoodById(FoodBean bean,int id){
        ContentValues values = new ContentValues();
        /*在values中添加内容*/
        values.put("amount", bean.getAmount());
        values.put("date", bean.getDate());
        long i = db.update(TABLE_NAME,values,"id=?",new String[]{String.valueOf(id)});
        if (i > 0) return true;
        else return false;
    }
}