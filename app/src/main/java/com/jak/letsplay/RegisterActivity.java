package com.jak.letsplay;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Spinner;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jak.letsplay.util.API;
import com.jak.letsplay.util.Constants;

import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etName, etEmail, etUsername, etPassword, etConfirmPassword;
    private Spinner spnGender;
    private String passwordFormat = "(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{6,15}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Register");

        // Initialize volley
        API.init(getApplicationContext());
        // Get reference to UI components. Set onclick listeners on buttons
        etName = findViewById(R.id.et_RegisterActivity_Name);
        etEmail = findViewById(R.id.et_RegisterActivity_Email);
        etUsername = findViewById(R.id.et_RegisterActivity_Username);
        etPassword = findViewById(R.id.et_RegisterActivity_Password);
        etConfirmPassword = findViewById(R.id.et_RegisterActivity_ConfirmPassword);
        spnGender = findViewById(R.id.spn_RegisterActivity_Gender);
        findViewById(R.id.btn_RegisterActivity_Register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();
                String gender = spnGender.getSelectedItem().toString();

                // Empty check
                if (name.isEmpty()) {
                    Constants.toast(getApplicationContext(), "Please enter name");
                    return;
                } else if (email.isEmpty()) {
                    Constants.toast(getApplicationContext(), "Please enter email");
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Constants.toast(getApplicationContext(), "Please enter valid email");
                    return;
                } else if (password.isEmpty()) {
                    Constants.toast(getApplicationContext(), "Please enter password");
                    return;
                } else if (password.length() < 6 || password.length() > 16) {
                    Constants.toast(getApplicationContext(), "Password must be between 6 to 15 characters long");
                    return;
                } else if (!password.matches(passwordFormat)) {
                    Constants.toast(getApplicationContext(), "Password should contain uppercase, lowercase, numbers and special symbols");
                    return;
                } else if (confirmPassword.isEmpty()) {
                    Constants.toast(getApplicationContext(), "Please enter confirm password");
                    return;
                } else if (!confirmPassword.equals(password)) {
                    Constants.toast(getApplicationContext(), "Please enter username");
                    return;
                } else if (gender.isEmpty()) {
                    Constants.toast(getApplicationContext(), "Please select gender");
                    return;
                }

                register(name, email, username, password, gender);
            }
        });
    }

    /**
     * Call the register API
     *
     * @param name     User's same
     * @param email    User's email
     * @param username User's username
     * @param password User's password
     * @param gender   User's gender
     */
    private void register(String name, String email, String username, String password, String gender) {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("email", email);
        params.put("username", username);
        params.put("password", password);
        params.put("gender", gender);
        params.put("device", "android");
        API.call(Constants.registerURL, params, responseListener, errorListener);
    }

    /**
     * Callback for Volley to return the response
     */
    Response.Listener<String> responseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String message = jsonObject.optString("message", "An error occurred. Please try again.");
                Constants.toast(getApplicationContext(), message);
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
