package com.neusoft.qiuxiangmei.iceboxtest;

import java.io.Serializable;

/*
邱祥梅版权所有
 */
public class FoodBean implements Serializable {
    public int id;
    public String name;
    public int amount;
    public String date;
    public int imgid;

    public FoodBean(int id ,String name, int amount, String date,int imgid) {
        super();
        this.id= id;
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.imgid = imgid;
    }

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getImgid() {
        return imgid;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }
}
