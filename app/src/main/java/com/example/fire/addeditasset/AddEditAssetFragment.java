package com.example.fire.addeditasset;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.fire.R;
import com.example.fire.databinding.AssetFragmentBinding;
import com.example.fire.utils.InjectorUtils;
import com.example.fire.utils.NumberDecimalFilter;

public class AddEditAssetFragment extends Fragment {

    private static final String ASSET_ID_ARGS = "ASSET_ID";

    private static boolean ADD_ASSET_STATE;

    private AssetFragmentBinding binding;

    private AddEditAssetViewModel addEditAssetViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate this data binding layout
        final View root = inflater.inflate(R.layout.asset_fragment, container, false);
        if (binding == null) {
            binding = AssetFragmentBinding.bind(root);
        }

        // Create viewmodel from factory
        AddEditAssetViewModelFactory factory = InjectorUtils.provideAddEditAssetViewModelFactory(getActivity().getApplicationContext());
        addEditAssetViewModel = ViewModelProviders.of(this, factory).get(AddEditAssetViewModel.class);

        binding.setViewmodel(addEditAssetViewModel);
        binding.setLifecycleOwner(this);

        setHasOptionsMenu(true);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupActionBar();

        loadAsset();

        observeSpinner();

        setupInputFilters();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save_asset_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        if(ADD_ASSET_STATE){
            MenuItem item = menu.findItem(R.id.delete_asset);
            item.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_asset:
                if (isInputCorrect()) {
                    addEditAssetViewModel.saveAsset();
                    getActivity().onBackPressed();
                }
                return true;

            case R.id.delete_asset:
                confirmDelete();
                return true;

            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return false;
    }


    private void setupActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

        actionBar.setHomeAsUpIndicator(R.drawable.ic_close);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (getArguments() != null) {
            actionBar.setTitle(R.string.edit_title);
            ADD_ASSET_STATE = false;
        } else {
            actionBar.setTitle(R.string.add_title);
            ADD_ASSET_STATE = true;
        }
    }

    private void loadAsset() {
        // Add or edit asset?
        if (getArguments() != null) {
            addEditAssetViewModel.start(getArguments().getInt(ASSET_ID_ARGS));
        } else {
            addEditAssetViewModel.start(null);
        }
    }

    private void observeSpinner() {
        addEditAssetViewModel.category.observe(this, s -> {

            if (s.equals("Investment")) {
                addEditAssetViewModel.isInvestment.setValue(true);
            } else {
                addEditAssetViewModel.isInvestment.setValue(false);
            }

            if (s.equals("Salary")) {
                addEditAssetViewModel.annualReturn.setValue("1200");
            }
            if (s.equals("Cash") || s.equals("Bank Account")) {
                addEditAssetViewModel.annualReturn.setValue("0");
            }

        });
    }

    private void setupInputFilters() {
        // Allow only two decimal places for number inputs.
        binding.editTextBalance.setFilters(new InputFilter[]{new NumberDecimalFilter()});
        binding.editTextReturn.setFilters(new InputFilter[]{new NumberDecimalFilter()});
    }

    private void confirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.alertDialog));
        builder.setCancelable(true);
        builder.setTitle(R.string.delete_asset_title);
        builder.setMessage(R.string.delete_asset_alert_text);
        builder.setPositiveButton(getString(R.string.delete),
                (dialog, which) -> {
                    addEditAssetViewModel.deleteAsset();
                    getActivity().onBackPressed();
                });
        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Check for empty fields
    private boolean isInputCorrect() {

        String name = addEditAssetViewModel.name.getValue();
        String balance = addEditAssetViewModel.balance.getValue();
        String annualReturn = addEditAssetViewModel.annualReturn.getValue();

        if (name == null || name.trim().isEmpty()) {
            binding.editTextName.setError(getString(R.string.name_error));
            return false;
        }
        if
        (balance == null || balance.trim().isEmpty()) {
            binding.editTextBalance.setError(getString(R.string.balance_error));
            return false;
        }
        if (annualReturn == null || annualReturn.trim().isEmpty()) {
            addEditAssetViewModel.annualReturn.setValue(getString(R.string.default_return_value));
            return true;
        }

        return true;
    }
}
