package com.example.android.inventoryapp;

import android.database.Cursor;

/**
 * DAO Architecture (However, Update function was not included.
 * Created by Jae Hee on 7/4/2016.
 */
public interface DAOInventory<T> {

    //method used to insert a record to a table
    void insert(T record);

    //method used to retrieve records from a table
    Cursor getRecords();

    //method used to delete records from a table
    void delete(T record);

    //method used to delete entire records from a table
    void deleteAll();
}
