package com.example.android.inventoryapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * This is the screen when use clicks on the add product button (the pink plus button)
 * This screen's orientation is always in landscape mode for better user experience.
 * Created by Jae Hee on 7/4/2016.
 */
public class AddProductActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    DAOBrands brand_db_helper;
    DAOSuppliers supplier_db_helper;
    DAOInventoryItems items_db_helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        brand_db_helper = new DAOBrands(this);
        supplier_db_helper = new DAOSuppliers(this);
        items_db_helper = new DAOInventoryItems(this);

        addListeners();
    }

    //Creates and adds brand spinner
    public void addBrandSpinner(){

        Spinner spinner_brands = (Spinner) findViewById(R.id.spinner_brands);

        //Get brands from the DB
        Cursor results = brand_db_helper.getRecords();

        ArrayList<String> brandNames = new ArrayList<String>();

        while(results.moveToNext()){
            String brand_name = results.getString(results.getColumnIndex(DatabaseTables.Brands.BRAND_NAME));
            brandNames.add(brand_name);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, brandNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_brands.setAdapter(dataAdapter);
        spinner_brands.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // On selecting a spinner item
                String item = adapterView.getItemAtPosition(i).toString();
                TextView spinner_response = (TextView) findViewById(R.id.brand_spinner_response);
                spinner_response.setText(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    //Creates and adds supplier spinner
    public void addSupplierSpinner(){

        Spinner spinner_supplier = (Spinner) findViewById(R.id.spinner_supplier);

        //Get brands from the DB
        //Extract all the items from the DB
        Cursor results = supplier_db_helper.getRecords();

        ArrayList<String> supplierNames = new ArrayList<String>();

        while(results.moveToNext()){
            String supplier_name = results.getString(results.getColumnIndex(DatabaseTables.Suppliers.SUPPLIER_NAME));
            supplierNames.add(supplier_name);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, supplierNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_supplier.setAdapter(dataAdapter);
        spinner_supplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // On selecting a spinner item
                String item = adapterView.getItemAtPosition(i).toString();
                TextView spinner_response = (TextView) findViewById(R.id.supplier_spinner_response);
                spinner_response.setText(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    //Add listeners
    public void addListeners(){

        Button getImageButton = (Button) findViewById(R.id.get_image_button);
        getImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        addBrandSpinner();
        addSupplierSpinner();

        Button add_product_button = (Button) findViewById(R.id.add_product_button);
        add_product_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Before going further, we check whether all the information is provided or not
                EditText item_title_text = (EditText) findViewById(R.id.item_title);
                if(isEmpty(item_title_text)){
                    Toast.makeText(getApplicationContext(), "You did not enter an item name", Toast.LENGTH_SHORT).show();
                    return;
                }

                EditText item_description_text = (EditText) findViewById(R.id.item_description);
                if(isEmpty(item_description_text)){
                    Toast.makeText(getApplicationContext(), "You did not enter an item description", Toast.LENGTH_SHORT).show();
                    return;
                }
                EditText item_price_text = (EditText) findViewById(R.id.item_price);
                if(isEmpty(item_price_text)){
                    Toast.makeText(getApplicationContext(), "You did not enter an item price", Toast.LENGTH_SHORT).show();
                    return;
                }

                ImageView item_thumbnail = (ImageView) findViewById(R.id.item_thumbnail);
                if(item_thumbnail.getDrawable() == null){
                    Toast.makeText(getApplicationContext(), "You did not enter an image thumbnail", Toast.LENGTH_SHORT).show();
                    return;
                }

                //These will not be checked as there will be a response anyways.
                TextView brand_spinner_response = (TextView) findViewById(R.id.brand_spinner_response);
                TextView supplier_spinner_response = (TextView) findViewById(R.id.supplier_spinner_response);

                Item new_product = new Item();
                new_product.setItemTitle(item_title_text.getText().toString());
                new_product.setItemDescription(item_description_text.getText().toString());
                String item_price = item_price_text.getText().toString();
                double item_price_decimal;
                try{
                    //If double
                    item_price_decimal = Double.parseDouble(item_price);
                    new_product.setItemPrice(item_price_decimal);
                }catch(Exception e){
                    //Not double
                    e.printStackTrace();
                }

                new_product.setItemThumbnail(((BitmapDrawable) item_thumbnail.getDrawable()).getBitmap());
                String brand_name = brand_spinner_response.getText().toString();

                Cursor brand_results = brand_db_helper.getRecordWithBrandName(brand_name);
                brand_results.moveToFirst();
                int brandId = brand_results.getInt(brand_results.getColumnIndex(DatabaseTables.Brands.BRAND_ID));
                new_product.setBrandId(brandId);

                String supplier_name = supplier_spinner_response.getText().toString();

                Cursor supplier_results = supplier_db_helper.getRecordWithSupplierName(supplier_name);
                supplier_results.moveToFirst();
                int supplierId = supplier_results.getInt(supplier_results.getColumnIndex(DatabaseTables.Suppliers.SUPPLIER_ID));
                new_product.setSupplierId(supplierId);

                new_product.setQuantityInStock(1); //We added so basically one!

                items_db_helper.insert(new_product);

                //Inform the use that adding product has been completed.
                Toast.makeText(getApplicationContext(),
                        "The product has just been added.", Toast.LENGTH_LONG).show();

                //Going back to the main list screen.
                Intent main_intent = new Intent(AddProductActivity.this, MainActivity.class);
                startActivity(main_intent);

            }
        });
    }

    //Getting data from user selection and stores it in the ImageView
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ImageView imageView = (ImageView) findViewById(R.id.item_thumbnail);
            imageView.setImageBitmap(photo);
        }
    }

    /*
        CHECKERS
     */

    //Checking if editText is empty
    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }
}