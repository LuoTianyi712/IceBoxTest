package com.neusoft.qiuxiangmei.iceboxtest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/*
邱祥梅版权所有
 */

public class Main4Activity extends AppCompatActivity {

    List<FoodBean> data;
    RecyclerView recyclerView;
    DBhelper dBhelper;
    FruitRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        recyclerView=findViewById(R.id.recyclerView);
        initData();
        adapter=new FruitRecyclerViewAdapter(this,data);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
    private void initData(){
        dBhelper =new DBhelper(this);
        dBhelper.open();
        //dBhelper.initData();//初始化
        data =dBhelper.getAllFoods();
       //data.clear();
    }

    public void onItemClick(View v){
        int postion=recyclerView.getChildAdapterPosition(v);
        Toast.makeText(this,"你点击了："+data.get(postion).name,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_add:
                makeAddFoodDialog();
                break;
            case R.id.menu_item_horizontal: {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                layoutManager.setOrientation(RecyclerView.HORIZONTAL);
            }
                break;
            case R.id.menu_item_vertical:{
                LinearLayoutManager layoutManager=(LinearLayoutManager)recyclerView.getLayoutManager();
                layoutManager.setOrientation(RecyclerView.VERTICAL);
            }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.context_menu_modify:
                Intent intent = new Intent();
                intent.setClass(Main4Activity.this,Main3Activity.class);
                intent.putExtra("id",adapter.getSelectedItemData().getId());
                intent.putExtra("name",adapter.getSelectedItemData().getName());
                intent.putExtra("amount",adapter.getSelectedItemData().getAmount());
                intent.putExtra("date",adapter.getSelectedItemData().getDate());
                intent.putExtra("imgid",adapter.getSelectedItemData().getImgid());
                startActivity(intent);
                Toast.makeText(this,"你选择了修改："+adapter.getSelectedItemData().id,Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.context_menu_delete:
                Toast.makeText(this,"你选择了删除："+adapter.getSelectedItemData().name,Toast.LENGTH_SHORT).show();
                dBhelper.deleteFoodById(adapter.getSelectedItemData().getId());
                adapter.removeSelectedItem();
                adapter.notifyDataSetChanged();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            FoodBean bean=(FoodBean)data.getSerializableExtra("food");
            dBhelper.inserfood(bean);
            adapter.addItem(bean);
            Toast.makeText(this,"成功添加食物："+bean.name,Toast.LENGTH_SHORT).show();
        }
    }

    //创建增加对话框
    private void makeAddFoodDialog(){
        final View view=View.inflate(this,R.layout.add_food_dialog,null);
        final Spinner spinner=view.findViewById(R.id.spinnerFoodName);
        final EditText etAmount=view.findViewById(R.id.editTextNumber);
        final EditText etDate=view.findViewById(R.id.editText_10);
        final ImageView imageView=view.findViewById(R.id.imageView);
        ArrayAdapter<String>spinnerAdapter=new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item,DBhelper.names);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                imageView.setImageResource(DBhelper.imgid[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //创建对话框
        AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle("添加食物")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        FoodBean bean=new FoodBean(1,"白菜",3,"1999/01/01",R.drawable.baicai);
                        int index = spinner.getSelectedItemPosition();
                        bean.name = DBhelper.names[index];
                        bean.amount = Integer.valueOf(etAmount.getText().toString());
                        bean.date = etDate.getText().toString();
                        bean.imgid = DBhelper.imgid[index];

                        dBhelper.inserfood(bean);
                        adapter.addItem(bean);
                    }
                })
                .setNegativeButton("取消",null)
                .create();
        dialog.show();
    }
}
