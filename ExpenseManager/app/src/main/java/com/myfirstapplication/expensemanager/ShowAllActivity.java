package com.myfirstapplication.expensemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ShowAllActivity extends AppCompatActivity {

    private static final int LAUNCH_SECOND_ACTIVITY = 1;
    String result = "";
    private static ShowAllActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all);
        instance = this;

        DBController dbController = new DBController(this);
        ArrayList<Expense> expenseArrayAdapter = dbController.loadExpenseData();

        CustomAdapter customAdapter = new CustomAdapter(this, R.layout.xml_show_data, expenseArrayAdapter);

        ListView listView = findViewById(R.id.lstShow);
        listView.setAdapter(customAdapter);
    }

    public void editData(String id) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("id", id);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);
    }

    public static ShowAllActivity getInstance() {
        return instance;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                result = data.getStringExtra("result");
                if (result.equals("updated"))
                    recreate();
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }
}
