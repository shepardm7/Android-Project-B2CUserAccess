package com.example.phoenix.b2cuseraccess;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.phoenix.b2cuseraccess.models.UserDbHelper;

import java.util.regex.Pattern;

public class registerActivity extends AppCompatActivity {

    EditText emailText, passText, phoneText;
    Button signUpBtn;
    private AlertDialog.Builder alertDialogBuilder;
    private String email, password, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailText = (EditText) findViewById(R.id.registerEmailText);
        passText = (EditText) findViewById(R.id.registerPassText);
        phoneText = (EditText) findViewById(R.id.registerPhoneText);
        signUpBtn = (Button) findViewById(R.id.signUpButton);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDbHelper db = new UserDbHelper(registerActivity.this);

                email = emailText.getText().toString();
                password = passText.getText().toString();
                phone = phoneText.getText().toString();

                if (!emptyFieldsPresent()) {
                    if (isValidEmailAddress(email)) {
                        if (db.insertUser(email, password, phone)) {
                            Toast.makeText(registerActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            createAlert("E-mail address already registered!", "If you would like to log in please go back or use a different e-mail address.");
                        }
                    } else {
                        createAlert("Invalid E-mail address!", "Please enter a valid E-mail.");
                    }
                } else {
                    createAlert("All fields are required!", "");
                }
            }
        });
    }

    private void createAlert(String title, String message) {
        alertDialogBuilder = new AlertDialog.Builder(registerActivity.this);
        alertDialogBuilder.setTitle(title);
        if (!message.isEmpty()){
            alertDialogBuilder.setMessage(message);
        }
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public boolean emptyFieldsPresent() {
        if (email.trim().isEmpty() || password.isEmpty() || phone.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}
