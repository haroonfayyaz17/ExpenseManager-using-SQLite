package com.myfirstapplication.expensemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddActivity extends AppCompatActivity {

    FeedReaderDbHelper dbHelper;
    SQLiteDatabase db;
    DBController dbController;
    TextView txtDate1;
    EditText txtAmount;
    Spinner spinner;
    DatePicker datePicker;
    Button button;
    ArrayAdapter<String> adapter;
    List itemsID;
    List itemsString;
    String category;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        dbController = new DBController(this);
        getIDs();
        LoadSpinnerData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, monthOfYear, dayOfMonth);

                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    date = format.format(calendar.getTime());
                    txtDate1.setText(date);
                }
            });
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Amount = Integer.parseInt(String.valueOf(txtAmount.getText()));
                int catID = (int) dbController.getCategoryID(category);
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);

                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                date = format.format(calendar.getTime());
                Long id = dbController.insertExpense(Amount, catID, date);
                if (id == -1) {
                    showMessage("Error while inserting data");
                } else {
                    showMessage("Data inserted with this ID: " + id);
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            }
        });

    }

    void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    void LoadSpinnerData() {
        Cursor cursor = dbController.loadCategories();
        itemsID = new ArrayList<>();
        itemsString = new ArrayList<>();
        while (cursor.moveToNext()) {
            Long id = cursor.getLong(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedCategory._ID));
            String string = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedCategory.COLUMN_NAME_TITLE));
            itemsID.add(id);
            itemsString.add(string);
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemsString);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        cursor.close();
    }

    void getIDs() {
        txtAmount = findViewById(R.id.txtAmount);
        spinner = findViewById(R.id.spinnerCategory);
        datePicker = findViewById(R.id.datePickDate);
        button = findViewById(R.id.btnSave1);
        txtDate1 = findViewById(R.id.txtDate1);
    }

    void updateExpenseData(Expense expense) {

    }
}
