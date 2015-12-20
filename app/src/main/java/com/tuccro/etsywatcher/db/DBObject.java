package com.tuccro.etsywatcher.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tuccro.etsywatcher.model.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tuccro on 12/20/15.
 */
public class DBObject {

    Context mContext;
    SQLiteDatabase database;

    public DBObject(Context mContext) {
        this.mContext = mContext;
    }

    public void addItemToDB(Item item) {

        try {
            openDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBConstants.DB_ITEMS_TABLE_ID, item.getId());
            contentValues.put(DBConstants.DB_ITEMS_TABLE_TITLE, item.getTitle());
            contentValues.put(DBConstants.DB_ITEMS_TABLE_DESCRIPTION, item.getDescription());
            contentValues.put(DBConstants.DB_ITEMS_TABLE_PRICE, item.getPrice());

            database.insert(DBConstants.DB_ITEMS_TABLE_NAME, null, contentValues);

            if (!item.getImagesUrls().isEmpty()) {
                for (String url : item.getImagesUrls()) {
                    contentValues = new ContentValues();
                    contentValues.put(DBConstants.DB_IMAGES_TABLE_ID, item.getId());
                    contentValues.put(DBConstants.DB_IMAGES_TABLE_URL, url);
                    database.insert(DBConstants.DB_IMAGES_TABLE_NAME, null, contentValues);
                }
            }
            closeDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Item> getItemsListFromDB() {

        List<Item> itemsList = new ArrayList<>();

        try {
            openDatabase();
            Cursor cursor = database.query(DBConstants.DB_ITEMS_TABLE_NAME, null, null, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();

                do {
                    long id = cursor.getLong(cursor.getColumnIndex(DBConstants.DB_ITEMS_TABLE_ID));
                    String title = cursor.getString(cursor.getColumnIndex(DBConstants.DB_ITEMS_TABLE_TITLE));
                    String description = cursor.getString(cursor.getColumnIndex(DBConstants.DB_ITEMS_TABLE_DESCRIPTION));
                    double price = cursor.getDouble(cursor.getColumnIndex(DBConstants.DB_ITEMS_TABLE_PRICE));

                    itemsList.add(new Item(id, title, description, price));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }

        for (Item item : itemsList) {
            item.addImageUrls(getImagesUrlsListByItemId(item.getId()));
        }

        return itemsList;
    }

    public List<String> getImagesUrlsListByItemId(long id) {
        List<String> imagesUrls = new ArrayList<>();

        try {
            openDatabase();

            Cursor cursor = database.query(DBConstants.DB_IMAGES_TABLE_NAME
                    , DBConstants.DB_IMAGES_TABLE_URL.split(" ") // small hack for representing String object as String array
                    , DBConstants.DB_IMAGES_TABLE_ID + "=" + String.valueOf(id)
                    , null, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                do {
                    imagesUrls.add(cursor.getString(cursor
                            .getColumnIndex(DBConstants.DB_IMAGES_TABLE_URL)));
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return imagesUrls;
    }

    public void openDatabase() {
        database = new DBHelper(mContext).getReadableDatabase();
    }

    public void closeDatabase() {
        database.close();
    }

    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DBConstants.DB_NAME, null, DBConstants.DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DBConstants.DB_ITEMS_TABLE_CREATE);
            db.execSQL(DBConstants.DB_IMAGES_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
