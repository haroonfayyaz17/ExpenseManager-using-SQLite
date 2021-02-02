package com.myfirstapplication.expensemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FeedReaderDbHelper dbHelper;
    SQLiteDatabase db;
    DBController dbController;
    Intent intent;

    Button btnAdd, btnShow, btnAddExpense;
    ListView listOdd, listEven;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getIDs();
        dbController = new DBController(this);
        loadData();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, ShowAllActivity.class);
                startActivity(intent);
            }
        });
        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, AddExpenseActivity.class);
                startActivity(intent);
            }
        });

    }

    void loadData() {
        ArrayList<Expense> arrayList = dbController.getPercentages();

        ArrayList<Expense> evenList = new ArrayList<>();
        ArrayList<Expense> oddList = new ArrayList<>();

        for (int i = 0; i < arrayList.size(); i++) {
            if (i % 2 == 0)
                evenList.add(arrayList.get(i));
            else
                oddList.add(arrayList.get(i));
        }

        CustomAdapter customAdapter = new CustomAdapter(this, R.layout.xml_display_percentage, oddList);
        ListView listView = findViewById(R.id.lstListOdd);
        listView.setAdapter(customAdapter);

        CustomAdapter customAdapter1 = new CustomAdapter(this, R.layout.xml_display_percentage, evenList);
        ListView listView1 = findViewById(R.id.lstListEven);
        listView1.setAdapter(customAdapter1);

    }

    void getIDs() {
        btnAdd = findViewById(R.id.btnAdd);
        btnShow = findViewById(R.id.btnShowAll);
        listEven = findViewById(R.id.lstListEven);
        listOdd = findViewById(R.id.lstListOdd);
        btnAddExpense = findViewById(R.id.btnAddExpense);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }

    /* void loadCategories() {

        dbController.insertCategory("FUEL");
        dbController.insertCategory("Food");
        dbController.insertCategory("Education");
        dbController.insertCategory("MISC");
    }*/
}


