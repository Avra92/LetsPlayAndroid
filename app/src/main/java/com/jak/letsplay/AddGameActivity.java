package com.jak.letsplay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jak.letsplay.util.API;
import com.jak.letsplay.util.Constants;
import com.jak.letsplay.util.Prefs;

import org.json.JSONObject;

import java.util.HashMap;

public class AddGameActivity extends AppCompatActivity {
    private String username;
    private Spinner spnGames, spnPlatform;
    private EditText etID;
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);
        setTitle("Add Friends");
        // Initialize volley
        API.init(getApplicationContext());
        username = Prefs.getInstance(getApplicationContext()).get("username");

        // Get reference to UI components. Set onclick listeners on buttons
        spnGames = findViewById(R.id.spn_AddGameActivity_Game);
        spnPlatform = findViewById(R.id.spn_AddGameActivity_Platform);
        etID = findViewById(R.id.et_AddGameActivity_ID);
        tvInfo = findViewById(R.id.tv_AddGameActivity_Info);
        findViewById(R.id.btn_AddGameActivity_Add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String game = spnGames.getSelectedItem().toString();
                String ID = etID.getText().toString().trim();
                String platform = spnPlatform.getSelectedItem().toString();

                if (game.isEmpty()) {
                    Constants.toast(getApplicationContext(), "Please select game");
                    return;
                } else if (ID.isEmpty()) {
                    Constants.toast(getApplicationContext(), "Please enter game ID or tag");
                    return;
                } else if (platform.isEmpty()) {
                    Constants.toast(getApplicationContext(), "Please select platform");
                    return;
                }

                addGame(game, ID, platform);
            }
        });
        spnGames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String game = Constants.gameShortNameMap.get(adapterView.getItemAtPosition(i).toString());
                ArrayAdapter adapter = null;
                if (game.equals("csgo")) {
                    adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.addgame_csgo, R.layout.spinneritem);
                    tvInfo.setText("Please note for CSGO, please check your steam profile URL.\n\nSelect Steam 64 ID if your profile URL looks like https://steamcommunity.com/profiles/<steam64id>.\n\nSelect Custom URL ID if your profile URL looks like https://steamcommunity.com/id/<id>.");
                } else if (game.equals("fort")) {
                    adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.addgame_fort, R.layout.spinneritem);
                    tvInfo.setText("");
                } else if (game.equals("cr") || game.equals("coc")) {
                    adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.addgame_coccr, R.layout.spinneritem);
                    tvInfo.setText("Please note for Clash of Clans and Clash Royale provide your tag as game ID and not the game name.");
                }
                spnPlatform.setAdapter(adapter);
                spnPlatform.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * Call the add game API
     */
    public void addGame(String game, String nickname, String platform) {
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("game", Constants.gameShortNameMap.get(game));
        params.put("nickname", nickname);
        params.put("platform", Constants.gamePlatformMap.get(platform));
        API.call(Constants.addGameURL, params, responseListener, errorListener);
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
            String message = jsonObject.optString("message", "An error occurred. Please try again.");
            Constants.toast(getApplicationContext(), message);
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