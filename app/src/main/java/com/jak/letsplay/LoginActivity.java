package com.jak.letsplay;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jak.letsplay.util.API;
import com.jak.letsplay.util.Constants;
import com.jak.letsplay.util.Prefs;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText etUsername, etPassword;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        // Initialize volley
        API.init(getApplicationContext());

        // Get reference to UI components. Set onclick listeners on buttons
        etUsername = findViewById(R.id.et_LoginActivity_Username);
        etPassword = findViewById(R.id.et_LoginActivity_Password);
        findViewById(R.id.tv_LoginActivity_ForgotPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open forgot password screen
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
        findViewById(R.id.btn_LoginActivity_Register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open register screen
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        findViewById(R.id.btn_LoginActivity_Login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                // Empty check
                if (username.isEmpty()) {
                    Constants.toast(getApplicationContext(), "Please enter username");
                    return;
                } else if (password.isEmpty()) {
                    Constants.toast(getApplicationContext(), "Please enter password");
                    return;
                }

                login(password);
            }
        });
    }

    /**
     * Call the login API
     * @param password
     */
    private void login(String password) {
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("device", "android");
        API.call(Constants.loginURL, params, responseListener, errorListener);
    }

    /**
     * Callback for Volley to return the response
     */
    Response.Listener<String> responseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.optString("status", "e");
                String message = jsonObject.optString("message", "An error occurred. Please try again.");
                if (status.equals("s")) {
                    // Store username in shared preferences and open home screen
                    Prefs.getInstance(getApplicationContext()).put("username", username);
                    LoginActivity.this.startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                } else {
                    Constants.toast(getApplicationContext(), message);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Constants.errorToast(getApplicationContext());
            }
        }
    };

    /**
     * Callback for Volley to return errors
     */
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Constants.errorToast(getApplicationContext());
        }
    };
}
