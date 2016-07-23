package com.example.android.inventoryapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DAOBrands brand_db_helper;
    DAOSuppliers supplier_db_helper;
    DAOInventoryItems items_db_helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        brand_db_helper = new DAOBrands(this);
        supplier_db_helper = new DAOSuppliers(this);
        items_db_helper = new DAOInventoryItems(this);

        //brand_db_helper.traverseRecords(brand_db_helper.getRecords());
        //supplier_db_helper.traverseRecords(supplier_db_helper.getRecords());
        //items_db_helper.traverseRecords(items_db_helper.getRecords());
        //Cursor brands = brand_db_helper.getRecords();
        //Cursor suppliers = supplier_db_helper.getRecords();
        //brand_db_helper.traverseRecords(brands);
        //supplier_db_helper.traverseRecords(suppliers);

        addListeners();
        displayItems();
    }

    public void addListeners(){

        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.myFAB);
        myFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add_product_intent = new Intent(MainActivity.this, AddProductActivity.class);
                startActivity(add_product_intent);
            }
        });

    }

    //Displays items in ListView
    public void displayItems(){

        //Prepare Item ArrayList
        ArrayList<Item> itemList = new ArrayList<Item>();

        Cursor results = items_db_helper.getRecords();

        while(results.moveToNext()){
            int _id = results.getInt(results.getColumnIndex(DatabaseTables.InventoryItems.ITEM_ID));
            String item_title = results.getString(results.getColumnIndex(DatabaseTables.InventoryItems.ITEM_TITLE));
            int quantity_in_stock = results.getInt(results.getColumnIndex(DatabaseTables.InventoryItems.QUANTITY_IN_STOCK));
            double item_price = results.getDouble(results.getColumnIndex(DatabaseTables.InventoryItems.ITEM_PRICE));
            byte[] item_thumbnail_byte = results.getBlob(results.getColumnIndex(DatabaseTables.InventoryItems.ITEM_THUMBNAIL));
            Bitmap item_thumbnail = BitmapFactory.decodeByteArray(item_thumbnail_byte, 0, item_thumbnail_byte.length);

            //Create an Item object and store these data here (at this moment we will only extract data relevant to list item
            Item item = new Item();
            item.setItemId(_id);
            item.setItemTitle(item_title);
            item.setItemPrice(item_price);
            item.setItemThumbnail(item_thumbnail);
            item.setQuantityInStock(quantity_in_stock);

            itemList.add(item);
        }

        final ListView listView = (ListView) findViewById(R.id.item_list);
        listView.setItemsCanFocus(false);
        listView.setEmptyView(findViewById(R.id.empty_list_text));
        ItemAdapter itemAdapter = new ItemAdapter(MainActivity.this, itemList);
        listView.setAdapter(itemAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item curr_item = (Item) listView.getItemAtPosition(i);
                Intent details_intent = new Intent(MainActivity.this, DetailsActivity.class);
                details_intent.putExtra("item_id", curr_item.getItemId());
                startActivity(details_intent);
            }
        });

    }
}
