package com.myfirstapplication.expensemanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class FeedReaderDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ExpenseManager.db";
    private static final String CREATE_CATEGORY_TABLE_QUERY =
            "CREATE TABLE " + FeedReaderContract.FeedCategory.TABLE_NAME + " ("
                    + FeedReaderContract.FeedCategory._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + FeedReaderContract.FeedCategory.COLUMN_NAME_TITLE + " TEXT," + FeedReaderContract.FeedCategory.COLUMN_NAME_IMAGE + " BLOB NOT NULL);";
    private static final String CREATE_EXPENSE_TABLE_QUERY =
            "CREATE TABLE " + FeedReaderContract.FeedExpense.TABLE_NAME + " ("
                    + FeedReaderContract.FeedExpense._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + FeedReaderContract.FeedExpense.COLUMN_NAME_AMOUNT + " INT,"
                    + FeedReaderContract.FeedExpense.COLUMN_NAME_DATE + " DATE,"
                    + FeedReaderContract.FeedExpense.COLUMN_NAME_CatID + " INT);";

    private static final String DELETE_CATEGORY_TABLE_QUERY =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedCategory.TABLE_NAME + ";";
    private static final String DELETE_EXPENSE_TABLE_QUERY =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedExpense.TABLE_NAME + ";";

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CATEGORY_TABLE_QUERY);
        db.execSQL(CREATE_EXPENSE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_CATEGORY_TABLE_QUERY);
        db.execSQL(DELETE_EXPENSE_TABLE_QUERY);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
