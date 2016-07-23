package com.example.android.inventoryapp;

import android.provider.BaseColumns;

/**
 * Created by Jae Hee on 7/4/2016.
 */
public class DatabaseTables {


    //Preventing accidental creation of this object
    public DatabaseTables(){

    }

    //Suppliers Table
    //A Supplier can supply zero, one or many items.
    public static abstract class Suppliers implements BaseColumns {

        public static final String TABLE_NAME = "suppliers";
        public static final String SUPPLIER_ID = "supplier_id"; //PK
        public static final String SUPPLIER_NAME = "supplier_name";
        public static final String SUPPLIER_EMAIL = "supplier_email";
        public static final String[] ALL_COLUMNS = {SUPPLIER_ID, SUPPLIER_NAME, SUPPLIER_EMAIL};

    }

    //InventoryItems Table
    public static abstract class InventoryItems implements BaseColumns {

        public static final String TABLE_NAME = "inventory_items";
        public static final String ITEM_ID = "item_id"; //PK
        public static final String ITEM_TITLE = "item_title";
        public static final String ITEM_DESCRIPTION = "item_description";
        public static final String ITEM_PRICE = "item_price";
        public static final String ITEM_THUMBNAIL = "item_thumbnail";
        public static final String QUANTITY_IN_STOCK = "quantity_in_stock";
        public static final String BRAND_ID = "brand_id"; //FK
        public static final String SUPPLIER_ID = "supplier_id"; //FK
        public static final String[] ALL_COLUMNS =
                {ITEM_ID, ITEM_TITLE, ITEM_DESCRIPTION, ITEM_PRICE, ITEM_THUMBNAIL, QUANTITY_IN_STOCK, BRAND_ID, SUPPLIER_ID};

    }

    //Brands Table
    public static abstract class Brands implements BaseColumns {

        public static final String TABLE_NAME = "brands";
        public static final String BRAND_ID = "brand_id"; //PK
        public static final String BRAND_NAME = "brand_name";
        public static final String[] ALL_COLUMNS = {BRAND_ID, BRAND_NAME};

    }

}
