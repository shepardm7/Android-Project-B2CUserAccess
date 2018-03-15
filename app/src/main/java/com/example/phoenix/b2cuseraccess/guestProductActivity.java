package com.example.phoenix.b2cuseraccess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phoenix.b2cuseraccess.adapters.GuestListAdapter;
import com.example.phoenix.b2cuseraccess.models.Product;
import com.example.phoenix.b2cuseraccess.models.ProductListSingleton;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class guestProductActivity extends AppCompatActivity {

    private ArrayList<Product> products = new ArrayList<>();
    private ListView productListView;
    private TextView amountText;
    private double amt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_product_list);

        productListView = (ListView) findViewById(R.id.guestProductListView);
        amountText = (TextView) findViewById(R.id.guestAmountTextView);
        products = ProductListSingleton.getProductList();
        GuestListAdapter listAdapter = new GuestListAdapter(getApplicationContext(), products);
        productListView.setAdapter(listAdapter);


        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBoxGuest);
                TextView priceText = (TextView) view.findViewById(R.id.priceTextView);
                double price = Double.parseDouble(priceText.getText().toString());
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                } else {
                    checkBox.setChecked(true);
                }
                getAmount();
//                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        getAmount();
//                    }
//                });
            }
        });

    }

    private void getAmount() {
        amt = 0;
        for (int i = 0; i < productListView.getCount(); i++) {
            View child = productListView.getChildAt(i);
            CheckBox checkBox = (CheckBox) child.findViewById(R.id.checkBoxGuest);
            if (checkBox.isChecked()) {
                TextView priceText = (TextView) child.findViewById(R.id.priceTextView);
                double price = Double.parseDouble(priceText.getText().toString());
                amt = amt + price;
            }
            amountText.setText("Amount: $" + amt);
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);

        if(hasFocus) {
            for (int i = 0; i < productListView.getCount(); i++) {
                productListView.performItemClick(
                        productListView.getAdapter().getView(i, null, null),
                        i,
                        productListView.getAdapter().getItemId(i));
            }
        }

    }
}
