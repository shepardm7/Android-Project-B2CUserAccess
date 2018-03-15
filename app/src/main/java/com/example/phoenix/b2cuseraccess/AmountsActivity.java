package com.example.phoenix.b2cuseraccess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.phoenix.b2cuseraccess.models.AmountDbHelper;
import com.example.phoenix.b2cuseraccess.models.UserDbHelper;

import java.util.ArrayList;
import java.util.List;

public class AmountsActivity extends AppCompatActivity {

    ListView amountsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amounts);

        UserDbHelper dbHelper = new UserDbHelper(AmountsActivity.this);
        amountsList = (ListView) findViewById(R.id.amountsListView);
        ArrayList<String> amounts = dbHelper.getAmounts();
        dbHelper.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, amounts);
        amountsList.setAdapter(adapter);
    }
}
