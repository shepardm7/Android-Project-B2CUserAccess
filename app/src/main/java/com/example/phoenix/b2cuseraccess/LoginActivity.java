package com.example.phoenix.b2cuseraccess;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.phoenix.b2cuseraccess.models.UserDbHelper;
import com.example.phoenix.b2cuseraccess.models.UserSession;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn, registerBtn, guestBtn;
    EditText emailText, passwordText;
    AlertDialog.Builder alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = (Button) findViewById(R.id.loginButton);
        registerBtn = (Button) findViewById(R.id.registerButton);
        guestBtn = (Button) findViewById(R.id.guestButton);

        emailText = (EditText) findViewById(R.id.loginEmailText);
        passwordText = (EditText) findViewById(R.id.loginPasswordText);

        guestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, guestProductActivity.class);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), registerActivity.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDbHelper db = new UserDbHelper(LoginActivity.this);
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                if (db.checkUserCredentials(email, password)) {
                    UserSession.setSession(email);
                    Intent intent = new Intent(LoginActivity.this, registeredProductActivity.class);
//                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                } else {
                    alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                    alertDialogBuilder.setTitle("E-mail or password is invalid");
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
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UserSession.getSession() != null){
            Intent intent = new Intent(LoginActivity.this, registeredProductActivity.class);
//                    intent.putExtra("email", email);
            startActivity(intent);
            finish();
        }
    }
}
