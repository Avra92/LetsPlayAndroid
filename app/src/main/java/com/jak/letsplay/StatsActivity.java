package com.jak.letsplay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jak.letsplay.adapter.StatsListAdapter;
import com.jak.letsplay.model.StatsModel;
import com.jak.letsplay.util.API;
import com.jak.letsplay.util.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class StatsActivity extends AppCompatActivity {
    private String username, nickname, game, platform, in_game_name;
    private ArrayList<StatsModel> statsList;
    private RecyclerView.Adapter statsListAdapter;
    private TextView tvInGameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        setTitle("Stats");
        // Initialize volley
        API.init(getApplicationContext());
        statsList = new ArrayList<>();

        Intent intent = getIntent();
        username = intent.hasExtra("username") ? intent.getStringExtra("username") : "";
        game = intent.hasExtra("game") ? intent.getStringExtra("game") : "";
        platform = intent.hasExtra("platform") ? intent.getStringExtra("platform") : "";
        nickname = intent.hasExtra("nickname") ? intent.getStringExtra("nickname") : "";
        in_game_name = intent.hasExtra("in_game_name") ? intent.getStringExtra("in_game_name") : "";

        // Get reference to UI components
        tvInGameName = findViewById(R.id.tv_StatsActivity_InGameName);
        tvInGameName.setText(in_game_name);
        ImageView ivPic = findViewById(R.id.iv_StatsActivity_GamePic);
        ivPic.setImageResource(Constants.gameBannerMap.get(game));
        RecyclerView rcvGamesList = findViewById(R.id.rcv_StatsActivity_StatsList);
        statsListAdapter = new StatsListAdapter(statsList);
        rcvGamesList.setHasFixedSize(true);
        rcvGamesList.setLayoutManager(new LinearLayoutManager(this));
        rcvGamesList.setAdapter(statsListAdapter);

        getStats();
    }

    /**
     * Call the get stats API
     */
    private void getStats() {
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("game", game);
        params.put("nickname", nickname);
        params.put("platform", platform);
        API.call(Constants.gameStatsURL, params, responseListener, errorListener);
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
                statsList.clear();
                in_game_name = jsonObject.optString("in_game_name");
                tvInGameName.setText(in_game_name);
                JSONArray gamesArray = jsonObject.getJSONArray("stats");
                for (int i = 0; i < gamesArray.length(); i++) {
                    JSONObject gameObject = gamesArray.getJSONObject(i);
                    StatsModel model = new StatsModel();
                    model.setStatName(gameObject.optString("key"));
                    model.setStatValue(gameObject.optString("value"));
                    statsList.add(model);
                }
                statsListAdapter.notifyDataSetChanged();
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
}
