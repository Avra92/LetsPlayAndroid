package com.jak.letsplay;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jak.letsplay.util.API;
import com.jak.letsplay.util.Constants;

import org.json.JSONObject;

import java.util.HashMap;

public class ForgotPasswordActivity extends AppCompatActivity {
    private TextInputEditText etUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setTitle("Forgot Password");
        // Initialize volley
        API.init(getApplicationContext());
        // Get reference to UI components. Set onclick listeners on buttons
        etUsername = findViewById(R.id.et_ForgotPasswordActivity_Username);
        findViewById(R.id.btn_ForgotPasswordActivity_Submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString().trim();

                // Empty check
                if (username.isEmpty()) {
                    Constants.toast(getApplicationContext(), "Please enter username");
                    return;
                }

                forgotPassword(username);
            }
        });
    }

    /**
     * Call the forgot password API
     *
     * @param username The user's username
     */
    private void forgotPassword(String username) {
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        API.call(Constants.forgotPasswordURL, params, responseListener, errorListener);
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
