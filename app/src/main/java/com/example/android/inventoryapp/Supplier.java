package com.example.android.inventoryapp;

/**
 * Each item must be supplied by a supplier.
 * Created by Jae Hee on 7/4/2016.
 */
public class Supplier {

    private int supplier_id;
    private String supplier_name;
    private String supplier_email;

    public Supplier(){}

    public Supplier(String supplier_name, String supplier_email){
        this.supplier_name = supplier_name;
        this.supplier_email = supplier_email;
    }

    /*
    * SETTERS
    * */

    public void setSupplierName(String supplier_name){
        this.supplier_name = supplier_name;
    }

    public void setSupplier_email(String supplier_email){
        this.supplier_email = supplier_email;
    }

    /*
    * GETTERS
    * */

    public int getSupplierId(){
        return this.supplier_id;
    }

    public String getSupplierName(){
        return this.supplier_name;
    }

    public String getSupplierEmail(){
        return this.supplier_email;
    }

}
