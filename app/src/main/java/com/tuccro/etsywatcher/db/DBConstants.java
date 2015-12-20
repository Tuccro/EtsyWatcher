package com.tuccro.etsywatcher.db;

/**
 * Created by tuccro on 12/20/15.
 */
public interface DBConstants {

    String DB_NAME = "saved_items";
    int DB_VERSION = 1;

    String DB_ITEMS_TABLE_CREATE = "CREATE TABLE items" +
            "(id INTEGER PRIMARY KEY, title TEXT, description TEXT, price NUMERIC);";

    String DB_IMAGES_TABLE_CREATE = "CREATE TABLE images(item_id INTEGER, url TEXT);";

    String DB_ITEMS_TABLE_NAME = "items";

    String DB_ITEMS_TABLE_ID = "id";
    String DB_ITEMS_TABLE_TITLE = "title";
    String DB_ITEMS_TABLE_DESCRIPTION = "description";
    String DB_ITEMS_TABLE_PRICE = "price";


    String DB_IMAGES_TABLE_NAME = "images";

    String DB_IMAGES_TABLE_ID = "item_id";
    String DB_IMAGES_TABLE_URL = "url";
}
