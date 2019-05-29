package com.example.fire.assetslist;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.example.fire.R;
import com.example.fire.data.Asset;
import com.example.fire.databinding.DetailsFragmentBinding;
import com.example.fire.utils.InjectorUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.List;

public class AssetListFragment extends Fragment {

    private AssetListAdapter adapter;

    private DetailsFragmentBinding binding;

    private AssetListViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.details_fragment, container, false);

        AssetListViewModelFactory factory = InjectorUtils.provideAssetListViewModelFactory(getActivity().getApplicationContext());
        viewModel = ViewModelProviders.of(this, factory).get(AssetListViewModel.class);

        adapter = new AssetListAdapter(assetClickCallback);

        binding.assetList.assetRecyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupActionBar();

        setupFab();

        subscribeUi(viewModel.getAssets());

    }

    /**
     * Observe livedata, change ui values on data change.
     */
    private void subscribeUi(LiveData<List<Asset>> liveData) {
        liveData.observe(this, assets -> {

            // Set empty view when there are no assets
            if (assets.isEmpty()) {
                binding.emptyView.setVisibility(View.VISIBLE);
            } else {
                binding.emptyView.setVisibility(View.GONE);
            }

            // Update list items
            adapter.submitList(assets);
            // Update summary
            double totalBalance = 0;
            double annualIncome = 0;

            for (Asset asset : assets) {

                double balance = Double.parseDouble(asset.getBalance());
                double returnValue = Double.parseDouble(asset.getAnnualReturn());

                if (asset.getCategory().equals("Salary")) {
                    annualIncome += balance * returnValue / 100;
                    continue;
                }

                totalBalance += balance;
                annualIncome += balance * returnValue / 100;

            }

            // Setup summary layout ui components
            binding.summary.totalBalance.setText(new DecimalFormat("#.##").format(totalBalance) + "€");
            binding.summary.annualIncome.setText("+" + new DecimalFormat("#.##").format(annualIncome) + "€/year");
            binding.summary.monthlyIncome.setText("+" + new DecimalFormat("#.##").format(annualIncome / 12) + "€/month");
            binding.summary.dailyIncome.setText("+" + new DecimalFormat("#.##").format(annualIncome / 365) + "€/day");

            binding.executePendingBindings();
        });
    }

    private void setupActionBar() {

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("Home");
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

    }

    private void setupFab() {
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(view1 -> {
            ((MainActivity) getActivity()).addAssetFragment();
        });
    }


    /**
     * Callback for click on items in list
     */

    private AssetClickCallback assetClickCallback = asset -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            ((MainActivity) AssetListFragment.this.getActivity()).editAssetFragment(asset);
        }
    };
}
