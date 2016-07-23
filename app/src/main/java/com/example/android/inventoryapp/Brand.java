package com.example.android.inventoryapp;

/**
 * Each item should have a brand.
 * Created by Jae Hee on 7/4/2016.
 */
public class Brand {

    private int brand_id;
    private String brand_name;

    public Brand(){

    }

    public Brand(String brand_name){
        this.brand_name = brand_name;
    }

    /*
    * GETTERS
    * */

    public int getBrandId(){
        return brand_id;
    }

    public String getBrandName(){
        return brand_name;
    }

    /*
    * SETTERS
    * */
    public void setBrandName(){
        this.brand_name = brand_name;
    }

}
