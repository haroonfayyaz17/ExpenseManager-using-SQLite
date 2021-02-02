package com.myfirstapplication.expensemanager;

import android.provider.BaseColumns;

public final class FeedReaderContract {

    private FeedReaderContract() {
    }

    public static class FeedCategory implements BaseColumns {
        public static final String TABLE_NAME = "Categories";
        public static final String COLUMN_NAME_TITLE = "Category";
        public static final String COLUMN_NAME_IMAGE = "Image1";
    }

    public static class FeedExpense implements BaseColumns {
        public static final String TABLE_NAME = "Expense";
        public static final String COLUMN_NAME_AMOUNT = "Amount";
        public static final String COLUMN_NAME_DATE = "DATE";
        public static final String COLUMN_NAME_CatID = "CatID";
    }

}
