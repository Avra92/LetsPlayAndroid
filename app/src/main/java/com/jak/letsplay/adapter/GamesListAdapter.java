package com.jak.letsplay.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jak.letsplay.AddFriendsActivity;
import com.jak.letsplay.R;
import com.jak.letsplay.StatsActivity;
import com.jak.letsplay.model.GamesListModel;
import com.jak.letsplay.util.API;
import com.jak.letsplay.util.Constants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GamesListAdapter extends RecyclerView.Adapter<GamesListAdapter.ViewHolder> {
    private ArrayList<GamesListModel> gamesList;
    private Context context;
    private String username;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvGame;
        ImageView ivPic;
        Button btnAddFriends, btnLetsPlay, btnDeleteGame;
        ConstraintLayout cnslRow;

        ViewHolder(View row) {
            super(row);
            tvName = row.findViewById(R.id.tv_HomeActivity_GamesList_Nickname);
            tvGame = row.findViewById(R.id.tv_HomeActivity_GamesList_GameName);
            ivPic = row.findViewById(R.id.iv_HomeActivity_GamesList_GamePic);
            btnAddFriends = row.findViewById(R.id.btn_HomeActivity_GamesList_AddFriends);
            btnLetsPlay = row.findViewById(R.id.btn_HomeActivity_GamesList_LetsPlay);
            btnDeleteGame = row.findViewById(R.id.btn_HomeActivity_GamesList_DeleteGame);
            cnslRow = row.findViewById(R.id.cnsl_HomeActivity_GamesList_Row);
        }
    }

    public GamesListAdapter(Context context, ArrayList<GamesListModel> gamesList, String username) {
        this.context = context;
        this.gamesList = gamesList;
        this.username = username;
    }

    @NonNull
    @Override
    public GamesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_home, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int pos = position;
        final GamesListModel model = gamesList.get(position);
        holder.tvName.setText(model.getIn_game_name());
        holder.tvGame.setText(Constants.gameFullNameMap.get(model.getGame()));
        holder.ivPic.setImageResource(Constants.gameBannerMap.get(model.getGame()));
        holder.btnLetsPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                letsPlay(model.getGame());
            }
        });
        holder.btnAddFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddFriendsActivity.class);
                intent.putExtra("game", model.getGame());
                intent.putExtra("username", username);
                context.startActivity(intent);
            }
        });
        holder.cnslRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StatsActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("game", model.getGame());
                intent.putExtra("platform", model.getPlatform());
                intent.putExtra("nickname", model.getNickname());
                intent.putExtra("in_game_name", model.getIn_game_name());
                context.startActivity(intent);
            }
        });
        holder.btnDeleteGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialog);
                builder.setMessage("Are you sure you want to remove this game?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteGame(pos, model.getGame(), model.getNickname(), model.getPlatform());
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }

    /**
     * Call the delete game API
     */
    private void deleteGame(final int pos, String game, String nickname, String platform) {
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("game", game);
        params.put("nickname", nickname);
        params.put("platform", platform);
        API.call(Constants.removeGameURL, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.optString("status", "e");
                    String message = jsonObject.optString("message", "An error occurred. Please try again.");
                    if (status.equals("s")) {
                        gamesList.remove(pos);
                        Constants.toast(context, message);
                        notifyItemRemoved(pos);
                    } else {
                        Constants.toast(context, message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Constants.errorToast(context);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Constants.errorToast(context);
            }
        });
    }

    /**
     * Call the lets play API
     */
    private void letsPlay(String game) {
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("game", game);
        API.call(Constants.letsPlayURL, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message", "An error occurred. Please try again.");
                    Constants.toast(context, message);
                } catch (Exception e) {
                    e.printStackTrace();
                    Constants.errorToast(context);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Constants.errorToast(context);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gamesList.size();
    }
}
