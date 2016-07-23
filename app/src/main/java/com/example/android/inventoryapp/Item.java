package com.example.android.inventoryapp;

import android.graphics.Bitmap;

/**
 *
 * Created by Jae Hee on 7/4/2016.
 */
public class Item {

    private int item_id; //AUTOINCREMENT
    private String item_title;
    private String item_description;
    private Bitmap item_thumbnail = null;
    private int brand_id;
    private int supplier_id;
    private int quantity_in_stock;
    private double item_price;

    public Item(){}

    public Item(String item_title){
        this.item_title = item_title;
    }

    /*
        SETTERS
     */

    public void setItemId(int item_id){
        this.item_id = item_id;
    }

    public void setItemTitle(String item_title){
        this.item_title = item_title;
    }

    public void setItemDescription(String item_description){
        this.item_description = item_description;
    }

    public void setSupplierId(int supplier_id){
        this.supplier_id = supplier_id;
    }

    public void setItemPrice(double item_price){
        this.item_price = item_price;
    }

    public void setBrandId(int brand_id){
        this.brand_id = brand_id;
    }

    public void setItemThumbnail(Bitmap item_thumbnail){
        this.item_thumbnail = item_thumbnail;
    }

    //Should be careful about using this method (usually increase and decrease)
    public void setQuantityInStock(int quantity_in_stock){
        this.quantity_in_stock = quantity_in_stock;
    }

    public void increaseQuantityBy(int amount){
        this.quantity_in_stock += amount;
    }

    public void decreaseQuantityBy(int amount){
        this.quantity_in_stock -= amount;
    }

    /*
        GETTERS
     */

    public int getBrandId(){
        return this.brand_id;
    }

    public int getSupplierId(){
        return this.supplier_id;
    }

    public int getItemId(){
        return this.item_id;
    }

    public String getItemTitle(){
        return this.item_title;
    }

    public String getItemDescription(){
        return this.item_description;
    }

    public int getItemQuantityInStock(){
        return this.quantity_in_stock;
    }

    public double getItemPrice(){
        return this.item_price;
    }

    public Bitmap getItemThumbnail(){
        return this.item_thumbnail;
    }

    /*
    * CHECKERS
    * */

    public boolean hasThumbnail(){
        return this.item_thumbnail != null;
    }

}
