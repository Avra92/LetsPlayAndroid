package com.jak.letsplay;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        etUsername = findViewById(R.id.et_LoginActivity_Username);
        etPassword = findViewById(R.id.et_LoginActivity_Password);
        findViewById(R.id.tv_LoginActivity_ForgotPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
        findViewById(R.id.btn_LoginActivity_Register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        findViewById(R.id.btn_LoginActivity_Login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
