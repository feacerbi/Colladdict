package br.com.felipeacerbi.colladdict.dbs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import br.com.felipeacerbi.colladdict.models.CollectionItem;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;

/**
 * Created by felipe.acerbi on 29/10/2015.
 */
public final class CollectionsContract {

    private Context context;

    public CollectionsContract(Context context) {
        this.context = context;
    }

    public static abstract class CollectionStorages implements BaseColumns {
        public static final String TABLE_NAME = "collectionstorages";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_PHOTO_PATH = "photopath";
    }

    public static abstract class CollectionItems implements BaseColumns {
        public static final String TABLE_NAME = "collectionitems";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_PHOTO_PATH = "photopath";
        public static final String COLUMN_NAME_STORAGE_ID = "storageid";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_STORAGES_TABLE =
            "CREATE TABLE IF NOT EXISTS " + CollectionStorages.TABLE_NAME + " (" +
                    CollectionStorages._ID + INTEGER_TYPE + " PRIMARY KEY," +
                    CollectionStorages.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    CollectionStorages.COLUMN_NAME_CATEGORY + INTEGER_TYPE + COMMA_SEP +
                    CollectionStorages.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    CollectionStorages.COLUMN_NAME_PHOTO_PATH + TEXT_TYPE + " )";
    private static final String SQL_DELETE_STORAGES_TABLE =
            "DROP TABLE IF EXISTS " + CollectionStorages.TABLE_NAME;

    private static final String SQL_CREATE_ITEMS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + CollectionStorages.TABLE_NAME + " (" +
                    CollectionItems._ID + INTEGER_TYPE + " PRIMARY KEY," +
                    CollectionItems.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    CollectionItems.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    CollectionItems.COLUMN_NAME_PHOTO_PATH + TEXT_TYPE + COMMA_SEP +
                    CollectionItems.COLUMN_NAME_STORAGE_ID + INTEGER_TYPE + " )";
    private static final String SQL_DELETE_ITEMS_TABLE =
            "DROP TABLE IF EXISTS " + CollectionItems.TABLE_NAME;

    public class CollectionsDbHelper extends SQLiteOpenHelper {

        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "Collections.db";

        public CollectionsDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_STORAGES_TABLE);
            db.execSQL(SQL_CREATE_ITEMS_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_STORAGES_TABLE);
            db.execSQL(SQL_DELETE_ITEMS_TABLE);
            onCreate(db);
        }
    }

    public long insertCollectionStorage(CollectionStorage storage) {
        CollectionsDbHelper csDbHelper = new CollectionsDbHelper(context);
        SQLiteDatabase db = csDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CollectionStorages.COLUMN_NAME_TITLE, storage.getTitle());
        values.put(CollectionStorages.COLUMN_NAME_CATEGORY, storage.getCategory());
        values.put(CollectionStorages.COLUMN_NAME_DESCRIPTION, storage.getDescription());
        values.put(CollectionStorages.COLUMN_NAME_PHOTO_PATH, storage.getPhotoPath());

        return db.insert(
                CollectionStorages.TABLE_NAME,
                null,
                values);
    }

    public long insertCollectionStorageItem(CollectionItem item) {
        CollectionsDbHelper csDbHelper = new CollectionsDbHelper(context);
        SQLiteDatabase db = csDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CollectionItems.COLUMN_NAME_TITLE, item.getTitle());
        values.put(CollectionItems.COLUMN_NAME_DESCRIPTION, item.getDescription());
        values.put(CollectionItems.COLUMN_NAME_PHOTO_PATH, item.getPhotoPath());
        values.put(CollectionItems.COLUMN_NAME_STORAGE_ID, item.getStorageId());

        return db.insert(
                CollectionItems.TABLE_NAME,
                null,
                values);
    }

    public int deleteCollectionStorage(long id) {
        CollectionsDbHelper csDbHelper = new CollectionsDbHelper(context);
        SQLiteDatabase db = csDbHelper.getWritableDatabase();

        String selection = CollectionStorages._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };
        return db.delete(
                CollectionStorages.TABLE_NAME,
                selection,
                selectionArgs);
    }

    public int deleteCollectionItem(long id) {
        CollectionsDbHelper csDbHelper = new CollectionsDbHelper(context);
        SQLiteDatabase db = csDbHelper.getWritableDatabase();

        String selection = CollectionItems._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };
        return db.delete(
                CollectionItems.TABLE_NAME,
                selection,
                selectionArgs);
    }

    public int updateCollectionStorage(CollectionStorage storage) {
        CollectionsDbHelper csDbHelper = new CollectionsDbHelper(context);
        SQLiteDatabase db = csDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CollectionStorages.COLUMN_NAME_TITLE, storage.getTitle());
        values.put(CollectionStorages.COLUMN_NAME_CATEGORY, storage.getCategory());
        values.put(CollectionStorages.COLUMN_NAME_DESCRIPTION, storage.getDescription());
        values.put(CollectionStorages.COLUMN_NAME_PHOTO_PATH, storage.getPhotoPath());

        String selection = CollectionStorages._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(storage.getId()) };

        return db.update(
                CollectionStorages.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public int updateCollectionItem(CollectionItem item) {
        CollectionsDbHelper csDbHelper = new CollectionsDbHelper(context);
        SQLiteDatabase db = csDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CollectionItems.COLUMN_NAME_TITLE, item.getTitle());
        values.put(CollectionItems.COLUMN_NAME_DESCRIPTION, item.getDescription());
        values.put(CollectionItems.COLUMN_NAME_PHOTO_PATH, item.getPhotoPath());
        values.put(CollectionItems.COLUMN_NAME_STORAGE_ID, item.getStorageId());

        String selection = CollectionItems._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(item.getId()) };

        return db.update(
                CollectionItems.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public List<CollectionStorage> getCollectionStorages() {
        CollectionsDbHelper csDbHelper = new CollectionsDbHelper(context);
        SQLiteDatabase db = csDbHelper.getWritableDatabase();

        List<CollectionStorage> storages = new ArrayList<>();

        String[] projection = {
                CollectionStorages._ID,
                CollectionStorages.COLUMN_NAME_TITLE,
                CollectionStorages.COLUMN_NAME_CATEGORY,
                CollectionStorages.COLUMN_NAME_DESCRIPTION,
                CollectionStorages.COLUMN_NAME_PHOTO_PATH};

        Cursor c = db.query(
                CollectionStorages.TABLE_NAME,  // The table to query
                projection,                     // The columns to return
                null,                           // The columns for the WHERE clause
                null,                           // The values for the WHERE clause
                null,                           // don't group the rows
                null,                           // don't filter by row groups
                null                            // The sort order
        );

        if(c.moveToFirst()) {
            do {
                CollectionStorage storage = new CollectionStorage();

                storage.setId(c.getLong(c.getColumnIndex(CollectionStorages._ID)));
                storage.setTitle(c.getString(c.getColumnIndex(CollectionStorages.COLUMN_NAME_TITLE)));
                storage.setCategory(c.getInt(c.getColumnIndex(CollectionStorages.COLUMN_NAME_CATEGORY)));
                storage.setDescription(c.getString(c.getColumnIndex(CollectionStorages.COLUMN_NAME_DESCRIPTION)));
                storage.setPhotoPath(c.getString(c.getColumnIndex(CollectionStorages.COLUMN_NAME_PHOTO_PATH)));

                storages.add(storage);
            } while(c.moveToNext());
        }

        return storages;
    }

    public List<CollectionItem> getCollectionItems(long storageId) {
        CollectionsDbHelper csDbHelper = new CollectionsDbHelper(context);
        SQLiteDatabase db = csDbHelper.getWritableDatabase();

        List<CollectionItem> items = new ArrayList<>();

        String[] projection = {
                CollectionItems._ID,
                CollectionItems.COLUMN_NAME_TITLE,
                CollectionItems.COLUMN_NAME_DESCRIPTION,
                CollectionItems.COLUMN_NAME_PHOTO_PATH,
                CollectionItems.COLUMN_NAME_STORAGE_ID};

        String selection = null;
        String[] selectionArgs = { String.valueOf(storageId) };

        if(storageId != 0) {
            selection = CollectionItems.COLUMN_NAME_STORAGE_ID + " LIKE ?";
        }

        Cursor c = db.query(
                CollectionItems.TABLE_NAME,  // The table to query
                projection,                  // The columns to return
                selection,                   // The columns for the WHERE clause
                selectionArgs,               // The values for the WHERE clause
                null,                        // don't group the rows
                null,                        // don't filter by row groups
                null                         // The sort order
        );

        if(c.moveToFirst()) {
            do {
                CollectionItem item = new CollectionItem();

                item.setId(c.getLong(c.getColumnIndex(CollectionItems._ID)));
                item.setTitle(c.getString(c.getColumnIndex(CollectionItems.COLUMN_NAME_TITLE)));
                item.setDescription(c.getString(c.getColumnIndex(CollectionItems.COLUMN_NAME_DESCRIPTION)));
                item.setPhotoPath(c.getString(c.getColumnIndex(CollectionItems.COLUMN_NAME_PHOTO_PATH)));
                item.setStorageId(c.getInt(c.getColumnIndex(CollectionItems.COLUMN_NAME_STORAGE_ID)));

                items.add(item);
            } while(c.moveToNext());
        }

        return items;
    }
}
