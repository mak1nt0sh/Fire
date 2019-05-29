package com.example.fire.assetslist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fire.R;
import com.example.fire.data.Asset;
import com.example.fire.databinding.AssetItemBinding;

public class AssetListAdapter extends ListAdapter<Asset, AssetListAdapter.AssetHolder> {

    private AssetClickCallback assetClickCallback;

    protected AssetListAdapter(AssetClickCallback listener) {
        super(DIFF_CALLBACK);
        assetClickCallback = listener;
    }

    private static final DiffUtil.ItemCallback<Asset> DIFF_CALLBACK = new DiffUtil.ItemCallback<Asset>() {
        @Override
        public boolean areItemsTheSame(@NonNull Asset oldItem, @NonNull Asset newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Asset oldItem, @NonNull Asset newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getCategory().equals(newItem.getCategory()) &&
                    oldItem.getBalance().equals(newItem.getBalance()) &&
                    oldItem.getAnnualReturn().equals(newItem.getAnnualReturn());
        }
    };

    @NonNull
    @Override
    public AssetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AssetItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.asset_item,
                parent, false);
        binding.setCallback(assetClickCallback);
        return new AssetHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AssetHolder holder, int position) {
        holder.binding.setAsset(getItem(position));

        // Set icons relatively to category
        switch (holder.binding.getAsset().getCategory()) {

            case "Cash":
                holder.binding.imageView.setImageResource(R.drawable.ic_cash);
                break;
            case "Bank Account":
                holder.binding.imageView.setImageResource(R.drawable.ic_bank);
                break;
            case "Investment":
                holder.binding.imageView.setImageResource(R.drawable.ic_invest);
                break;
            case "Salary":
                holder.binding.imageView.setImageResource(R.drawable.ic_job);
                break;
        }

        // Hide return value view if it is not an investment asset
        if (holder.binding.getAsset().getCategory().equals("Cash") ||
                holder.binding.getAsset().getCategory().equals("Bank Account") ||
                holder.binding.getAsset().getCategory().equals("Salary")) {
            holder.binding.textViewReturn.setVisibility(View.INVISIBLE);
        }

        holder.binding.executePendingBindings();
    }

    class AssetHolder extends RecyclerView.ViewHolder {

        final AssetItemBinding binding;

        public AssetHolder(AssetItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
