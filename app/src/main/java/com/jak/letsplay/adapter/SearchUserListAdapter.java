package com.jak.letsplay.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
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
import com.jak.letsplay.model.SearchUserListModel;
import com.jak.letsplay.util.API;
import com.jak.letsplay.util.Constants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchUserListAdapter extends RecyclerView.Adapter<SearchUserListAdapter.ViewHolder> {
    private ArrayList<SearchUserListModel> searchList, filterList;
    private Context context;
    private String username;

    public void search(String s) {
        if (s.isEmpty()) {
            filterList.clear();
            filterList.addAll(searchList);
        } else {
            filterList.clear();
            for (SearchUserListModel model : searchList) {
                if (model.getName().contains(s)) {
                    filterList.add(model);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvUsername, tvInGameName;
        ImageView ivIcon;
        Button btnAdd;

        ViewHolder(View row) {
            super(row);
            tvName = row.findViewById(R.id.tv_AddFriendsActivity_SearchList_Name);
            tvUsername = row.findViewById(R.id.tv_AddFriendsActivity_SearchList_Username);
            tvInGameName = row.findViewById(R.id.tv_AddFriendsActivity_SearchList_InGameName);
            ivIcon = row.findViewById(R.id.iv_AddFriendsActivity_SearchList_GameIcon);
            btnAdd = row.findViewById(R.id.btn_AddFriendsActivity_SearchList_Add);
        }
    }

    public SearchUserListAdapter(Context context, ArrayList<SearchUserListModel> sList, String username) {
        this.context = context;
        this.filterList = sList;
        this.searchList = new ArrayList<>();
        this.searchList.addAll(sList);
        this.username = username;
    }

    @NonNull
    @Override
    public SearchUserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_addfriends, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SearchUserListModel model = filterList.get(position);
        holder.tvName.setText(model.getName());
        holder.tvUsername.setText(model.getUsername());
        holder.tvInGameName.setText(model.getIn_game_name());
        holder.ivIcon.setImageResource(Constants.gameIconMap.get(model.getGame()));
        holder.btnAdd.setVisibility(model.isFriend() ? View.INVISIBLE : View.VISIBLE);
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialog);
                builder.setMessage("Are you sure you want to add this user as friend?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addUser(model.getGame(), model.getUsername());
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
     * Call the add friend API
     */
    private void addUser(String game, String f_username) {
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("game", game);
        params.put("f_username", f_username);
        API.call(Constants.addFriendURL, params, new Response.Listener<String>() {
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
        return filterList.size();
    }
}
