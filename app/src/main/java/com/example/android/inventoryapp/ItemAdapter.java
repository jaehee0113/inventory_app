package com.example.android.inventoryapp;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Jae Hee on 7/4/2016.
 */
public class ItemAdapter extends ArrayAdapter<Item> {

    SQLiteDatabase db;
    DAOBrands brand_db_helper;
    DAOSuppliers supplier_db_helper;
    DAOInventoryItems items_db_helper;
    private Context mContext;

    public ItemAdapter(Activity context, ArrayList<Item> items){

        super(context, 0, items);
        this.mContext = context;
        items_db_helper = new DAOInventoryItems(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View itemView = convertView;

        if(itemView == null)
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

        final Item currentItem = getItem(position);

        /*
            ADDING DATA TO THE VIEW
         */

        TextView itemTitleTextView = (TextView) itemView.findViewById(R.id.name_text_view);
        itemTitleTextView.setText(currentItem.getItemTitle());

        final TextView itemQuantityTextView = (TextView) itemView.findViewById(R.id.quantity_text_view);
        itemQuantityTextView.setText("Qty." + ": " + Integer.toString(currentItem.getItemQuantityInStock()));

        TextView itemPriceTextView = (TextView) itemView.findViewById(R.id.price_text_view);
        itemPriceTextView.setText("$" + ": " + Double.toString(currentItem.getItemPrice()));

        ImageView itemThumbnailView = (ImageView) itemView.findViewById(R.id.item_image);
        itemThumbnailView.setImageBitmap(currentItem.getItemThumbnail());

        Button saleButtonView = (Button) itemView.findViewById(R.id.sale_button);
        saleButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int current_quantity = currentItem.getItemQuantityInStock();
                if(current_quantity == 0)
                    Toast.makeText(mContext.getApplicationContext(), "The quantity is zero.", Toast.LENGTH_LONG).show();
                else {
                    currentItem.decreaseQuantityBy(1);
                    items_db_helper.updateQuantityById(currentItem.getItemId(), currentItem.getItemQuantityInStock());
                }
                //Set it to a newly reflected quantity
                itemQuantityTextView.setText("Qty." + ": " + Integer.toString(currentItem.getItemQuantityInStock()));

            }
        });

        return itemView;

    }
}
