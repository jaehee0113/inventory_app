package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Jae Hee on 7/4/2016.
 */

public class DAOSuppliers implements DAOInventory<Supplier> {

    DatabaseHelper helper;
    SQLiteDatabase db;

    public DAOSuppliers(Context context){
        helper = new DatabaseHelper(context);
    }

    @Override
    public void insert(Supplier supplier) {
        db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseTables.Suppliers.SUPPLIER_NAME, supplier.getSupplierName());
        values.put(DatabaseTables.Suppliers.SUPPLIER_EMAIL, supplier.getSupplierEmail());
        db.insert(DatabaseTables.Suppliers.TABLE_NAME, null, values);
    }

    @Override
    public Cursor getRecords() {
        db = helper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseTables.Suppliers.TABLE_NAME, null, null, null, null, null, null);

        return cursor;
    }

    public void traverseRecords(Cursor records){

        while(records.moveToNext()){
            int _id = records.getInt(records.getColumnIndex(DatabaseTables.Suppliers.SUPPLIER_ID));
            String name = records.getString(records.getColumnIndex(DatabaseTables.Suppliers.SUPPLIER_NAME));
            String email = records.getString(records.getColumnIndex(DatabaseTables.Suppliers.SUPPLIER_EMAIL));
            Log.i("db", "id: " + _id + ", name:" + name + ", email:" + email);
        }
    }

    @Override
    public void delete(Supplier supplier) {
        db = helper.getWritableDatabase();
        db.delete(DatabaseTables.Suppliers.TABLE_NAME, DatabaseTables.Suppliers.SUPPLIER_ID + "=?", new String[]{supplier.getSupplierId() + ""});
    }

    @Override
    public void deleteAll() {
        db = helper.getWritableDatabase();
        db.delete(DatabaseTables.Suppliers.TABLE_NAME, null, null);
    }

    public Cursor getRecordById(int supplier_id) {
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DatabaseTables.Suppliers.TABLE_NAME + " where " + DatabaseTables.Suppliers.SUPPLIER_ID + "=?", new String[]{supplier_id + ""});

        return cursor;
    }

    public Cursor getRecordWithSupplierName(String supplier_name){
        db = helper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseTables.Suppliers.TABLE_NAME, null, DatabaseTables.Suppliers.SUPPLIER_NAME + "=?", new String[]{String.valueOf(supplier_name)}, null, null, null);

        return cursor;
    }

}