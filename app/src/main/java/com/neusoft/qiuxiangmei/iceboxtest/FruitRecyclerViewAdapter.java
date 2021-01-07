package com.neusoft.qiuxiangmei.iceboxtest;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/*
邱祥梅版权所有
 */
/**
 * 自定义RecyclerView的适配器（难点！）
 */
public class FruitRecyclerViewAdapter extends RecyclerView.Adapter<FruitRecyclerViewAdapter.MyViewHolder> {

    List<FoodBean> mDatas;
    Context context;
    FoodBean selectedItemData;

    public FoodBean getSelectedItemData() {
        return selectedItemData;
    }
    /**
     * 自定义方法，为了获取适配器里的数据
     * @return
     */
    public List<FoodBean> getDatas() {
        return mDatas;
    }
    /**
     * 自定义方法，为了设置适配器里的数据
     * @param mDatas
     */
    public void setDatas(List<FoodBean> mDatas) {
        this.mDatas = mDatas;
    }
    /**
     * 适配器的构造方法，在activity中创建适配器对象的时候使用。传如上下文和数据
     * @param context
     * @param data
     */
    public FruitRecyclerViewAdapter(Context context, List<FoodBean> data) {
        this.context = context;
        mDatas = data;
    }
    /**
     * 覆盖父类中方法：创建item视图的holder。这个holder也需要根据item视图自定义。
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = View.inflate(parent.getContext(),R.layout.listitem,null);//用这个方法有个缺陷，item不能适配到屏幕宽度
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem,parent,false);
        return new MyViewHolder(view);
    }
    /**
     * 覆盖父类方法：给holder绑定数据，根据列表当前滚动位置，给进入显示状态的item绑定数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.bind(mDatas.get(position));
    }
   /**
     * 覆盖父类方法：获取item数目，就是数据的个数
     * @return
     */
    @Override
    public int getItemCount() {
        if(mDatas!=null)return mDatas.size();
        return 0;
    }
    /**
     * 自定义删除选定item方法，为了方便对列表的操作
     */
    public void removeSelectedItem(){
        mDatas.remove(selectedItemData);
        notifyDataSetChanged();
    }
    /**
     * 自定义增加item条目的方法，为了方便对列表的操作
     */
    public void addItem(FoodBean bean){
        mDatas.add(bean);
        notifyDataSetChanged();
    }
    /**
     * 自定义ViewHolder类，继承RecyclerView自带的ViewHolder类。
     * 定义holder类的目的，是为了提高列表控件的效率，少占用内存，避免反复创建释放item视图
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvName;
        TextView tvAmount;
        TextView tvDate;
        FoodBean bean;
        /**
         * 构造方法：绑定item里各个控件，给控件设置事件监听器
         * @param itemView
         */
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.imageView8);
            tvName = itemView.findViewById(R.id.textView11);
            tvAmount = itemView.findViewById(R.id.textView15);
            tvDate = itemView.findViewById(R.id.textView20);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedItemData=bean;
                    Toast.makeText(context,"你点击了："+bean.name,Toast.LENGTH_SHORT).show();

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    selectedItemData=bean;
                    return false;
                }
            });
            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    MenuInflater inflater=new MenuInflater(context);
                    inflater.inflate(R.menu.context_menu,menu);
                }
            });
        }
        /**
         * 给item里控件绑定数据
         * @param bean
         */
        public void bind(FoodBean bean){
            this.bean = bean;
            ivIcon.setImageResource(bean.getImgid());
            tvName.setText(bean.getName());
            tvAmount.setText(String.valueOf(bean.getAmount()));
            tvDate.setText(String.valueOf(bean.getDate()));
        }
    }
}
