package com.jak.letsplay.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jak.letsplay.R;
import com.jak.letsplay.model.StatsModel;

import java.util.ArrayList;

public class StatsListAdapter extends RecyclerView.Adapter<StatsListAdapter.ViewHolder> {
    private ArrayList<StatsModel> statsList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatName, tvStatValue;

        ViewHolder(View row) {
            super(row);
            tvStatName = row.findViewById(R.id.tv_StatsActivity_StatsList_StatName);
            tvStatValue = row.findViewById(R.id.tv_StatsActivity_StatsList_StatValue);
        }
    }

    public StatsListAdapter(ArrayList<StatsModel> statsList) {
        this.statsList = statsList;
    }

    @NonNull
    @Override
    public StatsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_stats, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final StatsModel model = statsList.get(position);
        holder.tvStatName.setText(model.getStatName());
        holder.tvStatValue.setText(model.getStatValue());
    }

    @Override
    public int getItemCount() {
        return statsList.size();
    }
}
