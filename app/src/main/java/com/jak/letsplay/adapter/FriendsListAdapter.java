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
import com.jak.letsplay.R;
import com.jak.letsplay.StatsActivity;
import com.jak.letsplay.model.FriendsListModel;
import com.jak.letsplay.util.API;
import com.jak.letsplay.util.Constants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.ViewHolder> {
    private ArrayList<FriendsListModel> searchList, filterList;
    private Context context;
    private String username;

    public void search(String s) {
        if (s.isEmpty()) {
            filterList.clear();
            filterList.addAll(searchList);
        } else {
            filterList.clear();
            for (FriendsListModel model : searchList) {
                if (model.getName().contains(s)) {
                    filterList.add(model);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvUsername;
        ImageView ivIcon;
        Button btnDelete;
        ConstraintLayout cnslRow;

        ViewHolder(View row) {
            super(row);
            tvName = row.findViewById(R.id.tv_FriendsActivity_SearchList_Name);
            tvUsername = row.findViewById(R.id.tv_FriendsActivity_SearchList_Username);
            ivIcon = row.findViewById(R.id.iv_FriendsActivity_SearchList_GameIcon);
            btnDelete = row.findViewById(R.id.btn_FriendsActivity_SearchList_Delete);
            cnslRow = row.findViewById(R.id.cnsl_FriendsActivity_SearchList_Row);
        }
    }

    public FriendsListAdapter(Context context, ArrayList<FriendsListModel> searchList, String username) {
        this.context = context;
        this.searchList = searchList;
        this.filterList = searchList;
        this.username = username;
    }

    @NonNull
    @Override
    public FriendsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_friends, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int pos = position;
        final FriendsListModel model = filterList.get(position);
        holder.tvName.setText(model.getName());
        holder.tvUsername.setText(model.getUsername());
        holder.ivIcon.setImageResource(Constants.gameIconMap.get(model.getGame()));
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialog);
                builder.setMessage("Are you sure you want to remove this friend?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removeFriend(pos, model.getGame(), model.getUsername());
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
        holder.cnslRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StatsActivity.class);
                intent.putExtra("username", model.getUsername());
                intent.putExtra("game", model.getGame());
                intent.putExtra("platform", model.getPlatform());
                intent.putExtra("nickname", model.getNickname());
                intent.putExtra("in_game_name", model.getNickname());
                context.startActivity(intent);
            }
        });
    }

    /**
     * Call the remove friend API
     */
    private void removeFriend(final int pos, String game, String f_username) {
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("game", game);
        params.put("f_username", f_username);
        API.call(Constants.removeFriendURL, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.optString("status", "e");
                    String message = jsonObject.optString("message", "An error occurred. Please try again.");
                    Constants.toast(context, message);
                    if (status.equals("s")) {
                        filterList.remove(pos);
                        notifyItemRemoved(pos);
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

    @Override
    public int getItemCount() {
        return filterList.size();
    }
}
