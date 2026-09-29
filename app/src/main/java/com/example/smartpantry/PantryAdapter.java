package com.example.smartpantry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ItemHolder> {

    private ArrayList<PantryItem> items;
    private MainActivity activity;

    public PantryAdapter(MainActivity activity, ArrayList<PantryItem> items) {
        this.activity = activity;
        this.items = items;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        final PantryItem item = items.get(position);

        holder.name.setText(item.getName());

        String amount;
        if (item.getQuantity() == (int) item.getQuantity()) {
            amount = String.valueOf((int) item.getQuantity());
        } else {
            amount = String.valueOf(item.getQuantity());
        }
        holder.amount.setText(amount + " " + item.getUnit());

        if (item.getExpiry() == null || item.getExpiry().isEmpty()) {
            holder.expiry.setVisibility(View.GONE);
        } else {
            holder.expiry.setVisibility(View.VISIBLE);
            holder.expiry.setText("Expires " + item.getExpiry());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.editItem(item.getId());
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.deleteItem(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        TextView name, amount, expiry;
        Button delete;

        ItemHolder(View view) {
            super(view);
            name = view.findViewById(R.id.nameText);
            amount = view.findViewById(R.id.amountText);
            expiry = view.findViewById(R.id.expiryText);
            delete = view.findViewById(R.id.deleteButton);
        }
    }
}
