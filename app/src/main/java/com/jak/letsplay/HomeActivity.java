package com.jak.letsplay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jak.letsplay.adapter.GamesListAdapter;
import com.jak.letsplay.model.GamesListModel;
import com.jak.letsplay.util.API;
import com.jak.letsplay.util.Constants;
import com.jak.letsplay.util.Prefs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {
    private String username;
    private ArrayList<GamesListModel> gamesList;
    private RecyclerView.Adapter gamesListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Home");
        // Initialize volley
        API.init(getApplicationContext());
        username = Prefs.getInstance(getApplicationContext()).get("username");
        updateFCMToken();

        gamesList = new ArrayList<>();
        // Get reference to UI components. Set onclick listeners on buttons
        RecyclerView rcvGamesList = findViewById(R.id.rcv_HomeActivity_GamesList);
        gamesListAdapter = new GamesListAdapter(HomeActivity.this, gamesList, username);
        rcvGamesList.setHasFixedSize(true);
        rcvGamesList.setLayoutManager(new LinearLayoutManager(this));
        rcvGamesList.setAdapter(gamesListAdapter);
        findViewById(R.id.btn_HomeActivity_AddGame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open add game screen
                HomeActivity.this.startActivity(new Intent(HomeActivity.this, AddGameActivity.class));
            }
        });
        findViewById(R.id.btn_HomeActivity_Friends).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open friends screen
                HomeActivity.this.startActivity(new Intent(HomeActivity.this, FriendsActivity.class));
            }
        });
    }

    /**
     * Fetch games list every time this screen resumes
     */
    @Override
    protected void onResume() {
        super.onResume();
        getGamesList();
    }

    /**
     * Call the games list API
     */
    private void getGamesList() {
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        API.call(Constants.gamesListURL, params, responseListener, errorListener);
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
                gamesList.clear();
                JSONArray gamesArray = jsonObject.getJSONArray("games");
                for (int i = 0; i < gamesArray.length(); i++) {
                    JSONObject gameObject = gamesArray.getJSONObject(i);
                    GamesListModel model = new GamesListModel();
                    model.setGame(gameObject.optString("game"));
                    model.setPlatform(gameObject.optString("platform"));
                    model.setNickname(gameObject.optString("nickname"));
                    model.setNickname2(gameObject.optString("nickname2"));
                    model.setIn_game_name(gameObject.optString("in_game_name"));
                    gamesList.add(model);
                }
                gamesListAdapter.notifyDataSetChanged();
            } else {
                Constants.toast(getApplicationContext(), message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Constants.errorToast(getApplicationContext());
        }
    }

    /**
     * Callback for Volley to return errors
     */
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Constants.errorToast(getApplicationContext());
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_home_logout) {
            Prefs.getInstance(getApplicationContext()).remove(Constants.PREF_USERNAME);
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Call the fcm token API
     */
    private void updateFCMToken() {
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("token", FirebaseInstanceId.getInstance().getToken());
        API.call(Constants.fcmTokenURL, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
}
