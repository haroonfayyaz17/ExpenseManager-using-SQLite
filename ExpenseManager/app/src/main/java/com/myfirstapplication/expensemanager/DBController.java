package com.myfirstapplication.expensemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBController {

    FeedReaderDbHelper dbHelper;
    SQLiteDatabase db;

    DBController(Context context) {
        dbHelper = new FeedReaderDbHelper(context);
    }

    long insertCategory(String Category, byte[] bytes) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedCategory.COLUMN_NAME_TITLE, Category);
        values.put(FeedReaderContract.FeedCategory.COLUMN_NAME_IMAGE, bytes);
        long newRowId = db.insert(FeedReaderContract.FeedCategory.TABLE_NAME, null, values);
        db.close();
        return newRowId;

    }

    Cursor loadCategories() {
        db = dbHelper.getReadableDatabase();
        String[] projection = {FeedReaderContract.FeedCategory._ID,
                FeedReaderContract.FeedCategory.COLUMN_NAME_TITLE};
        Cursor cursor = db.query(
                FeedReaderContract.FeedCategory.TABLE_NAME,
                projection, null, null, null, null, null, null);
        return cursor;
    }

    long insertExpense(int Amount, int category, String date) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedExpense.COLUMN_NAME_AMOUNT, Amount);
        values.put(FeedReaderContract.FeedExpense.COLUMN_NAME_CatID, category);
        values.put(FeedReaderContract.FeedExpense.COLUMN_NAME_DATE, date);

        long newRowId = db.insert(FeedReaderContract.FeedExpense.TABLE_NAME, null, values);

        return newRowId;

    }

    long getCategoryID(String category) {
        db = dbHelper.getReadableDatabase();
        String[] projection = {FeedReaderContract.FeedCategory._ID};
        String selection = FeedReaderContract.FeedCategory.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = {category};

        Cursor cursor = db.query(
                FeedReaderContract.FeedCategory.TABLE_NAME,
                projection, selection, selectionArgs, null, null, null, null);
        Long id = null;
        while (cursor.moveToNext()) {
            id = cursor.getLong(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedCategory._ID));
        }
        cursor.close();
        return id;
    }

    ArrayList<Expense> loadExpenseData() {
        db = dbHelper.getReadableDatabase();
        ArrayList<Expense> expenses = new ArrayList<>();

        String query = "SELECT e." + FeedReaderContract.FeedExpense._ID + "," +
                FeedReaderContract.FeedExpense.COLUMN_NAME_AMOUNT + "," +
                FeedReaderContract.FeedExpense.COLUMN_NAME_DATE + "," +
                FeedReaderContract.FeedCategory.COLUMN_NAME_TITLE +
                " from " + FeedReaderContract.FeedExpense.TABLE_NAME + " e," + FeedReaderContract.FeedCategory.TABLE_NAME +
                " c where c." + FeedReaderContract.FeedCategory._ID + " = e." + FeedReaderContract.FeedExpense.COLUMN_NAME_CatID + ";";

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedExpense._ID));
            String Amount = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedExpense.COLUMN_NAME_AMOUNT));
            String Date = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedExpense.COLUMN_NAME_DATE));
            String Category = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedCategory.COLUMN_NAME_TITLE));
            expenses.add(new Expense(id, Amount, Date, Category));

        }
        cursor.close();
        return expenses;
    }

    boolean deleteExpense(String id) {
        db = dbHelper.getReadableDatabase();
        String selection = FeedReaderContract.FeedExpense._ID + "= ?";
        String[] selectionArgs = {id};
        int cursor = db.delete(
                FeedReaderContract.FeedExpense.TABLE_NAME, selection, selectionArgs);
        if (cursor == 0)
            return false;
        else
            return true;
    }

    ArrayList<Expense> getExpenseData(String id) {
        db = dbHelper.getReadableDatabase();
        ArrayList<Expense> expenses = new ArrayList<>();
        String query = "SELECT e." + FeedReaderContract.FeedExpense._ID + "," +
                FeedReaderContract.FeedExpense.COLUMN_NAME_AMOUNT + "," +
                FeedReaderContract.FeedExpense.COLUMN_NAME_DATE + "," +
                FeedReaderContract.FeedCategory.COLUMN_NAME_TITLE +
                " from " + FeedReaderContract.FeedExpense.TABLE_NAME + " e," + FeedReaderContract.FeedCategory.TABLE_NAME +
                " c where c." + FeedReaderContract.FeedCategory._ID + " = e." + FeedReaderContract.FeedExpense.COLUMN_NAME_CatID +
                " and e." + FeedReaderContract.FeedExpense._ID + "= " + id + ";";

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            String id1 = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedExpense._ID));
            String Amount = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedExpense.COLUMN_NAME_AMOUNT));
            String Date = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedExpense.COLUMN_NAME_DATE));
            String Category = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedCategory.COLUMN_NAME_TITLE));
            expenses.add(new Expense(id1, Amount, Date, Category));

        }
        cursor.close();
        return expenses;
    }

    int updateExpenseData(Expense expense) {
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedExpense.COLUMN_NAME_AMOUNT, expense.getAmount());
        values.put(FeedReaderContract.FeedExpense.COLUMN_NAME_DATE, expense.getDate());
        values.put(FeedReaderContract.FeedExpense.COLUMN_NAME_CatID, expense.getType());
        db = dbHelper.getWritableDatabase();
        int id = db.update(FeedReaderContract.FeedExpense.TABLE_NAME, values, FeedReaderContract.FeedExpense._ID + "=" + expense.getID(), null);
        return id;
    }

    ArrayList<Expense> getPercentages() {
        db = dbHelper.getReadableDatabase();
        ArrayList<Expense> percentages = new ArrayList<>();
        String query = "select " + FeedReaderContract.FeedCategory.COLUMN_NAME_IMAGE + ", c." +
                FeedReaderContract.FeedCategory.COLUMN_NAME_TITLE +
                ",round((sum(" + FeedReaderContract.FeedExpense.COLUMN_NAME_AMOUNT +
                ")/(SELECT CAST(SUM(" + FeedReaderContract.FeedExpense.COLUMN_NAME_AMOUNT +
                ") As REAL)  from Expense))*100,1) As 'per' from " + FeedReaderContract.FeedExpense.TABLE_NAME +
                " e," + FeedReaderContract.FeedCategory.TABLE_NAME + " c " +
                "where e." + FeedReaderContract.FeedExpense.COLUMN_NAME_CatID + "= c." + FeedReaderContract.FeedCategory._ID +
                " GROUP by " + FeedReaderContract.FeedExpense.COLUMN_NAME_CatID + ";";

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            String category = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedCategory.COLUMN_NAME_TITLE));
            String percentage = cursor.getString(cursor.getColumnIndexOrThrow("per"));
            byte[] bytes = cursor.getBlob(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedCategory.COLUMN_NAME_IMAGE));
            Expense percentage1 = new Expense(category, percentage, "", "");
            percentage1.setBytes(bytes);
            percentages.add(percentage1);
        }
        return percentages;
    }

}
