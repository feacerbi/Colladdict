package br.com.felipeacerbi.colladdict.dbs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.felipeacerbi.colladdict.models.Category;
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
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_PHOTO_PATH = "photopath";
        public static final String COLUMN_NAME_CATEGORY_ID = "categoryId";
    }

    public static abstract class CollectionItems implements BaseColumns {
        public static final String TABLE_NAME = "collectionitems";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_PHOTO_PATH = "photopath";
        public static final String COLUMN_NAME_RARITY = "rarity";
        public static final String COLUMN_NAME_VALUE = "value";
        public static final String COLUMN_NAME_STORAGE_ID = "storageid";
    }

    public static abstract class Categories implements BaseColumns {
        public static final String TABLE_NAME = "categories";
        public static final String COLUMN_NAME_TITLE = "title";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_STORAGES_TABLE =
            "CREATE TABLE IF NOT EXISTS " + CollectionStorages.TABLE_NAME + " (" +
                    CollectionStorages._ID + INTEGER_TYPE + " PRIMARY KEY," +
                    CollectionStorages.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    CollectionStorages.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    CollectionStorages.COLUMN_NAME_PHOTO_PATH + TEXT_TYPE + COMMA_SEP +
                    CollectionStorages.COLUMN_NAME_CATEGORY_ID + INTEGER_TYPE + " )";
    private static final String SQL_DELETE_STORAGES_TABLE =
            "DROP TABLE IF EXISTS " + CollectionStorages.TABLE_NAME;

    private static final String SQL_CREATE_ITEMS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + CollectionItems.TABLE_NAME + " (" +
                    CollectionItems._ID + INTEGER_TYPE + " PRIMARY KEY," +
                    CollectionItems.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    CollectionItems.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    CollectionItems.COLUMN_NAME_PHOTO_PATH + TEXT_TYPE + COMMA_SEP +
                    CollectionItems.COLUMN_NAME_RARITY + INTEGER_TYPE + COMMA_SEP +
                    CollectionItems.COLUMN_NAME_VALUE + INTEGER_TYPE + COMMA_SEP +
                    CollectionItems.COLUMN_NAME_STORAGE_ID + INTEGER_TYPE + " )";
    private static final String SQL_DELETE_ITEMS_TABLE =
            "DROP TABLE IF EXISTS " + CollectionItems.TABLE_NAME;

    private static final String SQL_CREATE_CATEGORIES_TABLE =
            "CREATE TABLE IF NOT EXISTS " + Categories.TABLE_NAME + " (" +
                    Categories._ID + INTEGER_TYPE + " PRIMARY KEY," +
                    Categories.COLUMN_NAME_TITLE + TEXT_TYPE + " )";
    private static final String SQL_DELETE_CATEGORIES_TABLE =
            "DROP TABLE IF EXISTS " + Categories.TABLE_NAME;

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
            db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_STORAGES_TABLE);
            db.execSQL(SQL_DELETE_ITEMS_TABLE);
            db.execSQL(SQL_DELETE_CATEGORIES_TABLE);
            onCreate(db);
        }
    }

    public long insertCollectionStorage(CollectionStorage storage) {
        CollectionsDbHelper csDbHelper = new CollectionsDbHelper(context);
        SQLiteDatabase db = csDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CollectionStorages.COLUMN_NAME_TITLE, storage.getTitle());
        values.put(CollectionStorages.COLUMN_NAME_DESCRIPTION, storage.getDescription());
        values.put(CollectionStorages.COLUMN_NAME_PHOTO_PATH, storage.getPhotoPath());
        if(storage.getCategory() != null) {
            values.put(CollectionStorages.COLUMN_NAME_CATEGORY_ID, storage.getCategory().getId());
        }

        return db.insert(
                CollectionStorages.TABLE_NAME,
                null,
                values);
    }

    public long insertCollectionItem(CollectionItem item) {
        CollectionsDbHelper csDbHelper = new CollectionsDbHelper(context);
        SQLiteDatabase db = csDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CollectionItems.COLUMN_NAME_TITLE, item.getTitle());
        values.put(CollectionItems.COLUMN_NAME_DESCRIPTION, item.getDescription());
        values.put(CollectionItems.COLUMN_NAME_PHOTO_PATH, item.getPhotoPath());
        values.put(CollectionItems.COLUMN_NAME_RARITY, item.getRarity());
        values.put(CollectionItems.COLUMN_NAME_VALUE, item.getValue());
        values.put(CollectionItems.COLUMN_NAME_STORAGE_ID, item.getStorageId());

        return db.insert(
                CollectionItems.TABLE_NAME,
                null,
                values);
    }

    public long insertCategory(Category category) {
        CollectionsDbHelper csDbHelper = new CollectionsDbHelper(context);
        SQLiteDatabase db = csDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Categories.COLUMN_NAME_TITLE, category.getTitle());

        return db.insert(
                Categories.TABLE_NAME,
                null,
                values);
    }

    public int deleteCollectionStorage(long id) {
        CollectionsDbHelper csDbHelper = new CollectionsDbHelper(context);
        SQLiteDatabase db = csDbHelper.getWritableDatabase();

        String selection = CollectionStorages._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };

        List<CollectionItem> items = getCollectionItems(id);
        for(CollectionItem item : items) {
            deleteCollectionItem(item.getId());
        }

        return db.delete(
                CollectionStorages.TABLE_NAME,
                selection,
                selectionArgs);
    }

    public int deleteCategory(long id) {
        CollectionsDbHelper csDbHelper = new CollectionsDbHelper(context);
        SQLiteDatabase db = csDbHelper.getWritableDatabase();

        String selection = Categories._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };
        return db.delete(
                Categories.TABLE_NAME,
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
        values.put(CollectionStorages.COLUMN_NAME_DESCRIPTION, storage.getDescription());
        values.put(CollectionStorages.COLUMN_NAME_PHOTO_PATH, storage.getPhotoPath());
        values.put(CollectionStorages.COLUMN_NAME_CATEGORY_ID, storage.getCategory().getId());

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
        values.put(CollectionItems.COLUMN_NAME_RARITY, item.getRarity());
        values.put(CollectionItems.COLUMN_NAME_VALUE, item.getValue());
        values.put(CollectionItems.COLUMN_NAME_STORAGE_ID, item.getStorageId());

        String selection = CollectionItems._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(item.getId()) };

        return db.update(
                CollectionItems.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public int updateCategory(Category category) {
        CollectionsDbHelper csDbHelper = new CollectionsDbHelper(context);
        SQLiteDatabase db = csDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Categories.COLUMN_NAME_TITLE, category.getTitle());

        String selection = Categories._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(category.getId()) };

        return db.update(
                Categories.TABLE_NAME,
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
                CollectionStorages.COLUMN_NAME_DESCRIPTION,
                CollectionStorages.COLUMN_NAME_PHOTO_PATH,
                CollectionStorages.COLUMN_NAME_CATEGORY_ID,};

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
                storage.setDescription(c.getString(c.getColumnIndex(CollectionStorages.COLUMN_NAME_DESCRIPTION)));
                storage.setPhotoPath(c.getString(c.getColumnIndex(CollectionStorages.COLUMN_NAME_PHOTO_PATH)));

                Category category = getCategoryById(c.getInt(c.getColumnIndex(CollectionStorages.COLUMN_NAME_CATEGORY_ID)));
                if(category != null) {
                    storage.setCategory(category);
                } else {
                    storage.setCategory(getCategories().get(0));
                }

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
                CollectionItems.COLUMN_NAME_RARITY,
                CollectionItems.COLUMN_NAME_VALUE,
                CollectionItems.COLUMN_NAME_STORAGE_ID};

        String selection = null;
        String[] selectionArgs = { String.valueOf(storageId) };

        if(storageId != -1) {
            selection = CollectionItems.COLUMN_NAME_STORAGE_ID + " LIKE ?";
        }

        if(selectionArgs.length != 0) {
            Cursor c = db.query(
                    CollectionItems.TABLE_NAME,  // The table to query
                    projection,                  // The columns to return
                    selection,                   // The columns for the WHERE clause
                    selectionArgs,               // The values for the WHERE clause
                    null,                        // don't group the rows
                    null,                        // don't filter by row groups
                    null                         // The sort order
            );

            if (c.moveToFirst()) {
                do {
                    CollectionItem item = new CollectionItem();

                    item.setId(c.getLong(c.getColumnIndex(CollectionItems._ID)));
                    item.setTitle(c.getString(c.getColumnIndex(CollectionItems.COLUMN_NAME_TITLE)));
                    item.setDescription(c.getString(c.getColumnIndex(CollectionItems.COLUMN_NAME_DESCRIPTION)));
                    item.setPhotoPath(c.getString(c.getColumnIndex(CollectionItems.COLUMN_NAME_PHOTO_PATH)));
                    item.setRarity(c.getInt(c.getColumnIndex(CollectionItems.COLUMN_NAME_RARITY)));
                    item.setValue(c.getInt(c.getColumnIndex(CollectionItems.COLUMN_NAME_VALUE)));
                    item.setStorageId(c.getInt(c.getColumnIndex(CollectionItems.COLUMN_NAME_STORAGE_ID)));

                    items.add(item);
                } while (c.moveToNext());
            }
        }

        return items;
    }

    public List<Category> getCategories() {

        List<Category> categories = new ArrayList<>();
        Cursor c = getCategoriesCursor();

        if(c.moveToFirst()) {
            do {
                Category category = new Category();

                category.setId(c.getLong(c.getColumnIndex(Categories._ID)));
                category.setTitle(c.getString(c.getColumnIndex(Categories.COLUMN_NAME_TITLE)));
                categories.add(category);
            } while(c.moveToNext());
        }

        if(categories.isEmpty()) {
            Category defaultCategory = new Category("Default");
            defaultCategory.setId(insertCategory(defaultCategory));
            categories.add(defaultCategory);
        }

        return categories;
    }

    public Cursor getCategoriesCursor() {
        CollectionsDbHelper csDbHelper = new CollectionsDbHelper(context);
        SQLiteDatabase db = csDbHelper.getWritableDatabase();

        String[] projection = {
                Categories._ID,
                Categories.COLUMN_NAME_TITLE};

        Cursor cursor = db.query(
                Categories.TABLE_NAME,  // The table to query
                projection,                     // The columns to return
                null,                           // The columns for the WHERE clause
                null,                           // The values for the WHERE clause
                null,                           // don't group the rows
                null,                           // don't filter by row groups
                null                            // The sort order
        );

        if(!cursor.moveToFirst()) {
            Category defaultCategory = new Category("Default");
            insertCategory(defaultCategory);

            return db.query(
                    Categories.TABLE_NAME,  // The table to query
                    projection,                     // The columns to return
                    null,                           // The columns for the WHERE clause
                    null,                           // The values for the WHERE clause
                    null,                           // don't group the rows
                    null,                           // don't filter by row groups
                    null                            // The sort order
            );
        } else {
            return cursor;
        }
    }

    public Category getCategory(Cursor cursor, int position) {
        Category category = new Category();

        cursor.moveToPosition(position);

        category.setId(cursor.getLong(cursor.getColumnIndex(CollectionsContract.Categories._ID)));
        category.setTitle(cursor.getString(cursor.getColumnIndex(CollectionsContract.Categories.COLUMN_NAME_TITLE)));

        return category;
    }

    public Category getCategoryById(long id) {
        Cursor c = getCategoriesCursor();
        Category category = new Category();

        if(c.moveToFirst()) {
            do {

                long newId = (c.getLong(c.getColumnIndex(Categories._ID)));

                if(id == newId) {
                    category.setId(newId);
                    category.setTitle(c.getString(c.getColumnIndex(CollectionsContract.Categories.COLUMN_NAME_TITLE)));
                    return category;
                }

            } while(c.moveToNext());
        }

        return null;
    }
}
