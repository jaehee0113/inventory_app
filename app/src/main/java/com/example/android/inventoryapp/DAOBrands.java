package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Jae Hee on 7/4/2016.
 */
public class DAOBrands implements DAOInventory<Brand> {

    DatabaseHelper helper;
    SQLiteDatabase db;

    public DAOBrands(Context context){
        helper = new DatabaseHelper(context);
    }

    @Override
    public void insert(Brand brand){
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseTables.Brands.BRAND_NAME, brand.getBrandName());
        db.insert(DatabaseTables.Brands.TABLE_NAME, null, values);
    }

    public Cursor getRecordWithBrandName(String brand_name){
        db = helper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseTables.Brands.TABLE_NAME, null, DatabaseTables.Brands.BRAND_NAME + "=?", new String[]{String.valueOf(brand_name)}, null, null, null);

        return cursor;
    }

    @Override
    public void delete(Brand brand) {
        db = helper.getWritableDatabase();
        db.delete(DatabaseTables.Brands.TABLE_NAME, DatabaseTables.Brands.BRAND_ID + "=?", new String[]{brand.getBrandId() + ""});
    }

    public void traverseRecords(Cursor records){

        while(records.moveToNext()){
            int _id = records.getInt(records.getColumnIndex(DatabaseTables.Brands.BRAND_ID));
            String name = records.getString(records.getColumnIndex(DatabaseTables.Brands.BRAND_NAME));
            Log.i("db", "id: " + _id + ", name:" + name);
        }
    }

    @Override
    public void deleteAll() {
        db = helper.getWritableDatabase();
        db.delete(DatabaseTables.Brands.TABLE_NAME, null, null);
    }

    @Override
    public Cursor getRecords() {
        db = helper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseTables.Brands.TABLE_NAME, null, null, null, null, null, null);

        return cursor;
    }

    public Cursor getRecordById(int brand_id) {
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DatabaseTables.Brands.TABLE_NAME + " where " + DatabaseTables.Brands.BRAND_ID + "=?", new String[]{brand_id + ""});

        return cursor;
    }

}
