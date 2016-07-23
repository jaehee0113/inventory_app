package com.example.android.inventoryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is the screen when user clicks each item.
 * Created by Jae Hee on 7/4/2016.
 */
public class DetailsActivity extends AppCompatActivity {

    SQLiteDatabase db;
    DAOBrands brand_db_helper;
    DAOSuppliers supplier_db_helper;
    DAOInventoryItems items_db_helper;
    private int item_id;
    private int supplier_id;
    private String supplier_email;
    private int brand_id;
    private String supplier_name;
    private String brand_name;
    private String item_title;
    private int quantity_in_stock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        brand_db_helper = new DAOBrands(this);
        supplier_db_helper = new DAOSuppliers(this);
        items_db_helper = new DAOInventoryItems(this);

        //We need to get the corresponding item_id in order show details of a particular item!
        Bundle extras = getIntent().getExtras();
        item_id = extras.getInt("item_id");
        populateLayout();
        addListeners();
    }

    public void populateLayout(){
        Cursor results = items_db_helper.getRecordById(item_id);
        //NOW WE HAVE TO POPULATE THIS (We should have only one row)
        results.moveToFirst();
            supplier_id = results.getInt(results.getColumnIndex(DatabaseTables.InventoryItems.SUPPLIER_ID));
            item_title = results.getString(results.getColumnIndex(DatabaseTables.InventoryItems.ITEM_TITLE));
            String itemDescription = results.getString(results.getColumnIndex(DatabaseTables.InventoryItems.ITEM_DESCRIPTION));
            brand_id = results.getInt(results.getColumnIndex(DatabaseTables.InventoryItems.BRAND_ID));
            quantity_in_stock = results.getInt(results.getColumnIndex(DatabaseTables.InventoryItems.QUANTITY_IN_STOCK));
            double item_price = results.getDouble(results.getColumnIndex(DatabaseTables.InventoryItems.ITEM_PRICE));
            byte[] item_thumbnail_byte = results.getBlob(results.getColumnIndex(DatabaseTables.InventoryItems.ITEM_THUMBNAIL));
            Bitmap item_thumbnail = BitmapFactory.decodeByteArray(item_thumbnail_byte, 0, item_thumbnail_byte.length);

        Cursor supplier_results = supplier_db_helper.getRecordById(supplier_id);
        supplier_results.moveToFirst();
        supplier_email = supplier_results.getString(supplier_results.getColumnIndex(DatabaseTables.Suppliers.SUPPLIER_EMAIL));
        supplier_name = supplier_results.getString(supplier_results.getColumnIndex(DatabaseTables.Suppliers.SUPPLIER_NAME));

        Cursor brand_results = brand_db_helper.getRecordById(brand_id);
        brand_results.moveToFirst();
        brand_name = brand_results.getString(brand_results.getColumnIndex(DatabaseTables.Brands.BRAND_NAME));

        //NOW WE CAN POPULATE THE LAYOUT!
        TextView itemTitleTextView = (TextView) findViewById(R.id.item_title_text);
        itemTitleTextView.setText(item_title);

        TextView itemDescriptionTextView = (TextView) findViewById(R.id.item_description_text);
        itemDescriptionTextView.setText(itemDescription);

        ImageView itemThumbnailView = (ImageView) findViewById(R.id.item_thumbnail_view);
        itemThumbnailView.setImageBitmap(item_thumbnail);

        TextView itemBrandTextView = (TextView) findViewById(R.id.brand_name_text);
        itemBrandTextView.setText(brand_name);

        TextView itemSuplierTextView = (TextView) findViewById(R.id.supplier_name_text);
        itemSuplierTextView.setText(supplier_name);

        TextView itemPriceTextView = (TextView) findViewById(R.id.item_price);
        itemPriceTextView.setText(Double.toString(item_price));
    }

    public void addListeners(){
        Button detailDeleteButton = (Button) findViewById(R.id.detail_delete_button);
        detailDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(DetailsActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Closing Activity")
                        .setMessage("Are you sure you want to delete this product?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Deletes the item
                                items_db_helper.deleteById(item_id);
                                //Going back to the main list screen.
                                Intent main_intent = new Intent(DetailsActivity.this, MainActivity.class);
                                startActivity(main_intent);
                                //Finishes
                                finish();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });

        Button detailOrderButton = (Button) findViewById(R.id.detail_order_button);
        detailOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Use implicit intent to allow to use Email app!

                Intent email_intent = new Intent(Intent.ACTION_SENDTO);
                email_intent.setData(Uri.parse("mailto:"));
                email_intent.putExtra(Intent.EXTRA_EMAIL, new String[]{supplier_email});
                email_intent.putExtra(Intent.EXTRA_SUBJECT, "Order for the item: " + item_title);
                email_intent.putExtra(Intent.EXTRA_TEXT, "Please process this item as soon as possible.");

                // Verify that the intent will resolve properly
                if(email_intent.resolveActivity(getPackageManager()) != null)
                    startActivity(email_intent);
            }
        });

        Button modifyButton = (Button) findViewById(R.id.modify_button);
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Spinner quantity_option_spinner = (Spinner) findViewById(R.id.quantity_option_spinner);
                String choice = quantity_option_spinner.getSelectedItem().toString();

                EditText quantity_amount = (EditText) findViewById(R.id.quantity_choice_text);
                String quantity_amount_string = quantity_amount.getText().toString();
                int quantity_amount_int = 0;
                try{
                    quantity_amount_int = Integer.parseInt(quantity_amount_string);
                }catch(Exception e){
                    e.printStackTrace();
                }

                //Modify quantity variable according to the spinner input
                if(choice.equals("increase")){
                    quantity_in_stock += quantity_amount_int;
                    items_db_helper.updateQuantityById(item_id, quantity_in_stock);
                }else if(choice.equals("decrease")){

                    if( (quantity_in_stock - quantity_amount_int) < 0){
                        Toast.makeText(getApplicationContext(), "Quantity cannot be modified as it will return minus value.", Toast.LENGTH_LONG).show();
                        return;
                    }else{
                        quantity_in_stock -= quantity_amount_int;
                        items_db_helper.updateQuantityById(item_id, quantity_in_stock);
                    }
                }

                Toast.makeText(getApplicationContext(), "Quantity has been modified.", Toast.LENGTH_LONG).show();

                Intent main_intent = new Intent(DetailsActivity.this, MainActivity.class);
                startActivity(main_intent);

            }
        });
    }

}