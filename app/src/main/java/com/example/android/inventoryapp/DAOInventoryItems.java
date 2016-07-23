package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Created by Jae Hee on 7/4/2016.
 */
public class DAOInventoryItems implements DAOInventory<Item>{

    DatabaseHelper helper;
    SQLiteDatabase db;

    public DAOInventoryItems(Context context){
        helper = new DatabaseHelper(context);
    }

    @Override
    public void insert(Item item) {
        db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseTables.InventoryItems.ITEM_TITLE, item.getItemTitle());
        values.put(DatabaseTables.InventoryItems.ITEM_DESCRIPTION, item.getItemDescription());
        values.put(DatabaseTables.InventoryItems.ITEM_PRICE, item.getItemPrice());
        values.put(DatabaseTables.InventoryItems.ITEM_THUMBNAIL, getBitmapAsByteArray(item.getItemThumbnail()));
        values.put(DatabaseTables.InventoryItems.BRAND_ID, item.getBrandId());
        values.put(DatabaseTables.InventoryItems.SUPPLIER_ID, item.getSupplierId());
        values.put(DatabaseTables.InventoryItems.QUANTITY_IN_STOCK, item.getItemQuantityInStock());
        db.insert(DatabaseTables.InventoryItems.TABLE_NAME, null, values);
    }

    public Cursor getRecordById(int item_id) {
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DatabaseTables.InventoryItems.TABLE_NAME + " where " + DatabaseTables.InventoryItems.ITEM_ID + "=?", new String[]{item_id + ""});

        return cursor;
    }

    @Override
    //This must be used to parse data into Item objects before using the setAdapter!
    public Cursor getRecords() {
        db = helper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseTables.InventoryItems.TABLE_NAME, null, null, null, null, null, null);

        return cursor;
    }

    @Override
    public void delete(Item item) {
        db = helper.getWritableDatabase();
        db.delete(DatabaseTables.InventoryItems.TABLE_NAME, DatabaseTables.InventoryItems.ITEM_ID + "=?", new String[]{item.getItemId() + ""});
    }

    public void deleteById(int item_id){
        db = helper.getWritableDatabase();
        db.delete(DatabaseTables.InventoryItems.TABLE_NAME, DatabaseTables.InventoryItems.ITEM_ID + "=?", new String[]{item_id + ""});
    }

    public void updateQuantityById(int item_id, int quantity) {
        db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseTables.InventoryItems.QUANTITY_IN_STOCK, quantity); //This should change the value
        db.update(DatabaseTables.InventoryItems.TABLE_NAME, values, DatabaseTables.InventoryItems.ITEM_ID + "=?", new String[]{item_id + ""});
    }

    @Override
    public void deleteAll() {
        db = helper.getWritableDatabase();
        db.delete(DatabaseTables.InventoryItems.TABLE_NAME, null, null);
    }

    public void traverseRecords(Cursor records){

        while(records.moveToNext()){
            int _id = records.getInt(records.getColumnIndex(DatabaseTables.InventoryItems.ITEM_ID));
            String name = records.getString(records.getColumnIndex(DatabaseTables.InventoryItems.ITEM_TITLE));
            String description = records.getString(records.getColumnIndex(DatabaseTables.InventoryItems.ITEM_DESCRIPTION));
            int qty = records.getInt(records.getColumnIndex(DatabaseTables.InventoryItems.QUANTITY_IN_STOCK));
            Log.i("db", "id: " + _id + ", name:" + name + ", description:" + description + ", quantity:" + qty);
        }
    }

    //Before storing the thumbnail, it is important to convert it to bitearray to store it in DB.
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}
