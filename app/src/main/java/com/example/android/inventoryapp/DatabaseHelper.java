package com.example.android.inventoryapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jae Hee on 7/4/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "inventory_db";

    //DATABASE WILL BE CREATED UPON CALLING THIS CONSTRUCTOR
    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }


    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //Executes whatever query we pass!
        String createBrands = "create table " + DatabaseTables.Brands.TABLE_NAME +
                " (" + DatabaseTables.Brands.BRAND_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DatabaseTables.Brands.BRAND_NAME + " TEXT"
                + ")";

        String createSuppliers = "create table " + DatabaseTables.Suppliers.TABLE_NAME +
                " (" + DatabaseTables.Suppliers.SUPPLIER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DatabaseTables.Suppliers.SUPPLIER_NAME + " TEXT,"
                + DatabaseTables.Suppliers.SUPPLIER_EMAIL + " TEXT"
                + ")";

        String createInventoryItems = "create table " + DatabaseTables.InventoryItems.TABLE_NAME +
                " (" + DatabaseTables.InventoryItems.ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DatabaseTables.InventoryItems.ITEM_TITLE + " TEXT,"
                + DatabaseTables.InventoryItems.ITEM_DESCRIPTION + " TEXT,"
                + DatabaseTables.InventoryItems.SUPPLIER_ID + " INTEGER,"
                + DatabaseTables.InventoryItems.BRAND_ID + " INTEGER,"
                + DatabaseTables.InventoryItems.ITEM_PRICE + " REAL,"
                + DatabaseTables.InventoryItems.ITEM_THUMBNAIL + " BLOB,"
                + DatabaseTables.InventoryItems.QUANTITY_IN_STOCK + " INTEGER, "
                + " FOREIGN KEY (" + DatabaseTables.InventoryItems.SUPPLIER_ID
                + ") REFERENCES " + DatabaseTables.Suppliers.TABLE_NAME + "(" + DatabaseTables.Suppliers.SUPPLIER_ID + "),"
                + " FOREIGN KEY (" + DatabaseTables.InventoryItems.BRAND_ID
                + ") REFERENCES " + DatabaseTables.Brands.TABLE_NAME + "(" + DatabaseTables.Brands.BRAND_ID + ")"
                + ")";

        //Tables must be created in this order (due to foreign key constraints)
        db.execSQL(createBrands);
        db.execSQL(createSuppliers);
        db.execSQL(createInventoryItems);

        //Also suppliers and brands need to be populated first.
        db.execSQL("insert into " + DatabaseTables.Brands.TABLE_NAME + "(" + DatabaseTables.Brands.BRAND_ID + "," + DatabaseTables.Brands.BRAND_NAME
                + ") values(" + null + ",'Samsung')");
        db.execSQL("insert into " + DatabaseTables.Brands.TABLE_NAME + "(" + DatabaseTables.Brands.BRAND_ID + "," + DatabaseTables.Brands.BRAND_NAME
                + ") values(" + null + ",'Apple')");
        db.execSQL("insert into " + DatabaseTables.Brands.TABLE_NAME + "(" + DatabaseTables.Brands.BRAND_ID + "," + DatabaseTables.Brands.BRAND_NAME
                + ") values(" + null + ",'Google')");
        db.execSQL("insert into " + DatabaseTables.Brands.TABLE_NAME + "(" + DatabaseTables.Brands.BRAND_ID + "," + DatabaseTables.Brands.BRAND_NAME
                + ") values(" + null + ",'Poogle')");

        db.execSQL("insert into " + DatabaseTables.Suppliers.TABLE_NAME + "(" + DatabaseTables.Suppliers.SUPPLIER_ID + "," + DatabaseTables.Suppliers.SUPPLIER_NAME
                + "," + DatabaseTables.Suppliers.SUPPLIER_EMAIL + ") values(" + null + ",'John', 'john@gmail.com')");
        db.execSQL("insert into " + DatabaseTables.Suppliers.TABLE_NAME + "(" + DatabaseTables.Suppliers.SUPPLIER_ID + "," + DatabaseTables.Suppliers.SUPPLIER_NAME
                + "," + DatabaseTables.Suppliers.SUPPLIER_EMAIL + ") values(" + null + ",'Jane', 'jane@gmail.com')");
        db.execSQL("insert into " + DatabaseTables.Suppliers.TABLE_NAME + "(" + DatabaseTables.Suppliers.SUPPLIER_ID + "," + DatabaseTables.Suppliers.SUPPLIER_NAME
                + "," + DatabaseTables.Suppliers.SUPPLIER_EMAIL + ") values(" + null + ",'Amy', 'amy@gmail.com')");
        db.execSQL("insert into " + DatabaseTables.Suppliers.TABLE_NAME + "(" + DatabaseTables.Suppliers.SUPPLIER_ID + "," + DatabaseTables.Suppliers.SUPPLIER_NAME
                + "," + DatabaseTables.Suppliers.SUPPLIER_EMAIL + ") values(" + null + ",'Nicole', 'nicole@gmail.com')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseTables.Suppliers.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseTables.Brands.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseTables.InventoryItems.TABLE_NAME);

        onCreate(db);
    }

    //Deletes the database file.
    public void deleteDatabase(Context context){
        context.deleteDatabase(DATABASE_NAME);
    }

}
