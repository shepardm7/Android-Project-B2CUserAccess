package com.example.phoenix.b2cuseraccess;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phoenix.b2cuseraccess.adapters.PostLoginListAdapter;
import com.example.phoenix.b2cuseraccess.models.AmountDbHelper;
import com.example.phoenix.b2cuseraccess.models.Product;
import com.example.phoenix.b2cuseraccess.models.ProductListSingleton;
import com.example.phoenix.b2cuseraccess.models.UserDbHelper;
import com.example.phoenix.b2cuseraccess.models.UserSession;

import java.util.ArrayList;
import java.util.Formatter;

import static java.lang.System.exit;

public class registeredProductActivity extends AppCompatActivity {

    private ArrayList<Product> products = new ArrayList<>();
    private ListView productListView;
    private TextView amountText, titleText;
    private Button smsBtn;
    private String smsMessage;
    private EditText priceEditText, discountEditText;
    private ArrayList<View> listRowViews = new ArrayList<>();
    private double amt;

    private AlertDialog.Builder alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_product);

        productListView = (ListView) findViewById(R.id.registeredProductListView);
        products = ProductListSingleton.getProductList();
        amountText = (TextView) findViewById(R.id.registeredProductAmountText);
        titleText = (TextView) findViewById(R.id.welcomeTitle);
        smsBtn = (Button) findViewById(R.id.smsButton);

        titleText.setText("Welcome, " + UserSession.getSession() + "!");
        PostLoginListAdapter listAdapter = new PostLoginListAdapter(getApplicationContext(), products);
        productListView.setAdapter(listAdapter);
        registerForContextMenu(productListView);
        smsBtn.setEnabled(false);

        smsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(Intent.ACTION_VIEW);
                n.setType("vnd.android-dir/mms-sms");
                n.putExtra("sms_body",smsMessage);
                startActivity(n);
            }
        });

        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBoxPLogin);
                priceEditText = (EditText) view.findViewById(R.id.priceEditText);
                discountEditText = (EditText) view.findViewById(R.id.discountEditText);
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                } else {
                    checkBox.setChecked(true);
                }

                getAmount();
                setListViews();
                if (checkIfChecked()) {
                    setSmsMessage();
                    smsBtn.setEnabled(true);
                } else {
                    smsBtn.setEnabled(false);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.registered_product_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_save_amount:
                alertDialogBuilder = new AlertDialog.Builder(registeredProductActivity.this);
                alertDialogBuilder.setTitle("Save amount?");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        insertAmounts();
                        Toast.makeText(registeredProductActivity.this, "Amount saved", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;

            case R.id.menu_show_amount:
                Intent goToAmountsActivity = new Intent(registeredProductActivity.this, AmountsActivity.class);
                startActivity(goToAmountsActivity);
                break;

            case R.id.menu_logout:
                alertDialogBuilder = new AlertDialog.Builder(registeredProductActivity.this);
                alertDialogBuilder.setTitle("Are you sure you want to log out?");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(registeredProductActivity.this, LoginActivity.class);
                        startActivity(intent);
                        UserSession.setSession(null);
                        finish();
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            default:
                alertDialogBuilder = new AlertDialog.Builder(registeredProductActivity.this);
                alertDialogBuilder.setTitle("Exit app?");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        exit(0);
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertAmounts() {
        if (checkIfChecked()) {
            UserDbHelper dbHelper = new UserDbHelper(registeredProductActivity.this);
            dbHelper.insertAmount(amt);
            dbHelper.close();
        } else {
            Toast.makeText(this, "No product selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void setListViews() {
        listRowViews.clear();

        for (int i = 0; i < productListView.getCount(); i++) {
            View child = productListView.getChildAt(i);
            CheckBox checkBox = (CheckBox) child.findViewById(R.id.checkBoxPLogin);
            if (checkBox.isChecked()) {
                listRowViews.add(child);
            }
        }
    }

    private Boolean checkIfChecked() {
        if (!listRowViews.isEmpty())
            return true;
        return false;
    }

    private void setSmsMessage() {
        smsMessage = "";
        String amount = amountText.getText().toString();
        for (int i = 0; i < listRowViews.size(); i++) {
            View child = listRowViews.get(i);
            TextView titleText = (TextView) child.findViewById(R.id.titlePLoginItem);
            EditText priceText = (EditText) child.findViewById(R.id.priceEditText);
            EditText discountText = (EditText) child.findViewById(R.id.discountEditText);
            String title = titleText.getText().toString();
            String price = priceText.getText().toString();
            String discount = discountText.getText().toString();
            CheckBox checkBox = (CheckBox) child.findViewById(R.id.checkBoxPLogin);
            if (checkBox.isChecked()) {
                if (smsMessage.isEmpty()){
                    if (discount.isEmpty()) {
                        smsMessage = smsMessage + "Great sales promotion here:\n";
                        smsMessage = smsMessage + title + ":\nPrice = $" + price;
                    } else {
                        smsMessage = smsMessage + "Great sales promotion here:\n";
                        smsMessage = smsMessage + title + ":\nPrice = $" + price + "  Discount = " + discount + "%";
                    }
                } else {
                    if (discount.isEmpty()) {
                        smsMessage = smsMessage + "\n\n" + title + ":\nPrice = $" + price;
                    } else {
                        smsMessage = smsMessage + "\n\n" + title + ":\nPrice = $" + price + "  Discount = " + discount + "%";
                    }
                }
            }
        }

        smsMessage = smsMessage + "\n\n" + amount;
    }

    private void getAmount() {
        amt = 0;
        for (int i = 0; i < productListView.getCount(); i++) {
            View child = productListView.getChildAt(i);
            CheckBox checkBox = (CheckBox) child.findViewById(R.id.checkBoxPLogin);
            if (checkBox.isChecked()) {
                EditText priceText = (EditText) child.findViewById(R.id.priceEditText);
                EditText discountText = (EditText) child.findViewById(R.id.discountEditText);
                double price = Double.parseDouble(priceText.getText().toString());
                double discount = getDiscountValue(discountText);
                price = price - (price * discount / 100);
                amt = amt + price;
            }

        }
        amt = (double) Math.round(amt * 100) / 100;
        amountText.setText("Amount: $" + amt);
    }

    private void updateAmount(double initialValue, double newValue) {
        amt = amt - initialValue;
        amt = amt + newValue;
        amt = (double) Math.round(amt * 100) / 100;
        amountText.setText("Amount: $" + amt);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem editPrice = menu.add("Edit Price");
        MenuItem editDiscount = menu.add("Edit Discount");

        editPrice.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                View child = productListView.getChildAt(info.position);
                EditText priceEditText = (EditText) child.findViewById(R.id.priceEditText);
                EditText discountEditText = (EditText) child.findViewById(R.id.discountEditText); 
                CheckBox checkBox = (CheckBox) child.findViewById(R.id.checkBoxPLogin);
                getAlert("Enter new price", priceEditText, priceEditText, discountEditText, checkBox.isChecked());
                return false;
            }
        });

        editDiscount.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                View child = productListView.getChildAt(info.position);
                EditText priceEditText = (EditText) child.findViewById(R.id.priceEditText);
                EditText discountEditText = (EditText) child.findViewById(R.id.discountEditText);
                CheckBox checkBox = (CheckBox) child.findViewById(R.id.checkBoxPLogin);
                getAlert("Enter new discount", discountEditText, priceEditText, discountEditText, checkBox.isChecked());
                return false;
            }
        });
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    private double getDiscountValue(EditText discountText) {
        double value;
        try {
            value = Double.parseDouble(discountText.getText().toString());
        } catch (NumberFormatException e) {
            value = 0;
        }
        return value;
    }

    private void setDiscountValue(double value, EditText discountText) {
        discountText.setText(String.valueOf(value));
    }

    private void getAlert(final String title, final EditText fieldToEdit, final EditText priceField, final EditText discountField, final Boolean checked) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(registeredProductActivity.this);
        final EditText editText = new EditText(registeredProductActivity.this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        alert.setTitle(title);
        alert.setView(editText);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                double initialPrice = Double.parseDouble(priceField.getText().toString());
                double initialDiscount = getDiscountValue(discountField);
                if (fieldToEdit != discountField) {
                    if (!editText.getText().toString().isEmpty()) {
                        fieldToEdit.setText(editText.getText().toString());
                    } else {
                        Toast.makeText(registeredProductActivity.this, "Price field cannot remain empty", Toast.LENGTH_SHORT).show();
                        getAlert(title, fieldToEdit, priceField, discountField, checked);
                        return;
                    }
                } else {
                    if (getDiscountValue(editText) >= 0.0 && getDiscountValue(editText) <= 100.0){
//                        fieldToEdit.setText(editText.getText().toString());
                        setDiscountValue(getDiscountValue(editText), fieldToEdit);
                    } else {
                        Toast.makeText(registeredProductActivity.this, "Discount must be between 0 and 100", Toast.LENGTH_SHORT).show();
                        getAlert(title, fieldToEdit, priceField, discountField, checked);
                        return;
                    }
                }
                double newPrice = Double.parseDouble(priceField.getText().toString());
                double newDiscount = getDiscountValue(discountField);
                if (checked) {
                    double initialTotalPrice = initialPrice - (initialPrice * initialDiscount / 100);
                    double newTotalPrice = newPrice - (newPrice * newDiscount / 100);
                    updateAmount(initialTotalPrice, newTotalPrice);
                }
                setSmsMessage();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }
}
