package com.jak.letsplay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jak.letsplay.adapter.FriendsListAdapter;
import com.jak.letsplay.model.FriendsListModel;
import com.jak.letsplay.util.API;
import com.jak.letsplay.util.Constants;
import com.jak.letsplay.util.Prefs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FriendsActivity extends AppCompatActivity {
    private String username;
    private ArrayList<FriendsListModel> searchList;
    private FriendsListAdapter searchListAdapter;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        setTitle("Friends");
        // Initialize volley
        API.init(getApplicationContext());
        searchList = new ArrayList<>();

        username = Prefs.getInstance(getApplicationContext()).get("username");

        // Get reference to UI components. Set onclick listeners on buttons
        RecyclerView rcvGamesList = findViewById(R.id.rcv_FriendsActivity_ResultList);
        searchListAdapter = new FriendsListAdapter(FriendsActivity.this, searchList, username);
        rcvGamesList.setHasFixedSize(true);
        rcvGamesList.setLayoutManager(new LinearLayoutManager(this));
        rcvGamesList.setAdapter(searchListAdapter);

        etSearch = findViewById(R.id.et_FriendsActivity_Search);

        getFriendsList();
    }

    /**
     * Call the friends list API
     */
    private void getFriendsList() {
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        API.call(Constants.friendsListUrl, params, responseListener, errorListener);
    }

    /**
     * Callback for Volley to return the response
     */
    Response.Listener<String> responseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            parseJSON(response);
        }
    };

    private void parseJSON(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            String status = jsonObject.optString("status", "e");
            String message = jsonObject.optString("message", "An error occurred. Please try again.");
            if (status.equals("s")) {
                // Parse JSON and populate recyclerview
                searchList.clear();
                JSONArray gamesArray = jsonObject.getJSONArray("friends");
                for (int i = 0; i < gamesArray.length(); i++) {
                    JSONObject gameObject = gamesArray.getJSONObject(i);
                    FriendsListModel model = new FriendsListModel();
                    model.setGame(gameObject.optString("game"));
                    model.setName(gameObject.optString("name"));
                    model.setUsername(gameObject.optString("username"));
                    model.setNickname(gameObject.optString("nickname"));
                    model.setPlatform(gameObject.optString("platform"));
                    searchList.add(model);
                }
                searchListAdapter.notifyDataSetChanged();
                etSearch.addTextChangedListener(watcher);
            } else {
                Constants.toast(getApplicationContext(), message);
                etSearch.removeTextChangedListener(watcher);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Constants.errorToast(getApplicationContext());
            etSearch.removeTextChangedListener(watcher);
        }
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            searchListAdapter.search(charSequence.toString().trim());
        }

        @Override
        public void afterTextChanged(Editable editable) {

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
