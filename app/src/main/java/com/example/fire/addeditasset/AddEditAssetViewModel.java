package com.example.fire.addeditasset;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fire.data.Asset;
import com.example.fire.data.DataRepository;
import com.example.fire.data.LoadAssetCallback;

public class AddEditAssetViewModel extends ViewModel implements LoadAssetCallback {

    private final DataRepository repository;

    // Two-way databinding, exposing MutableLiveData
    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> category = new MutableLiveData<>();
    public MutableLiveData<String> balance = new MutableLiveData<>();
    public MutableLiveData<String> annualReturn = new MutableLiveData<>();

    // Boolean which changes when spinner selects investment
    public MutableLiveData<Boolean> isInvestment = new MutableLiveData<>();

    // Boolean to know when to load UI components
    private final MutableLiveData<Boolean> dataLoading = new MutableLiveData<>();

    @Nullable
    private Integer assetId;

    private boolean isNewAsset;

    private boolean isDataLoaded = false;

    public AddEditAssetViewModel(DataRepository repository) {
        this.repository = repository;
    }

    public void start(Integer assetId) {
        if (dataLoading.getValue() != null && dataLoading.getValue()) {
            // Already loading, ignore.
            return;
        }

        this.assetId = assetId;

        if (assetId == null) {
            // No need to populate, it's a new task
            isNewAsset = true;
            return;
        }
        if (isDataLoaded) {
            // No need to populate, already have data.
            return;
        }
        isNewAsset = false;
        dataLoading.setValue(true);

        repository.getAssetById(assetId, this);
    }

    void saveAsset() {

        //Insert new Asset
        if (isNewAsset || assetId == null) {
            Asset asset = new Asset(name.getValue(), category.getValue(), balance.getValue(), annualReturn.getValue());
            createAsset(asset);
            //Update existing Asset
        } else {
            updateAsset();
        }
    }

    public void createAsset(Asset asset) {
        repository.instertAsset(asset);
    }

    public void updateAsset() {
        if (isNewAsset) {
            throw new RuntimeException("updateAsset() was called but asset is new.");
        } else {
            Asset asset = new Asset(assetId, name.getValue(), category.getValue(), balance.getValue(), annualReturn.getValue());
            repository.updateAsset(asset);
        }
    }

    public void deleteAsset() {
        repository.deleteAsset(assetId);
    }

    /**
     *  This is a callback method. When asset is loaded from database
     *  update the ui and set dataLoading to false
     */
    @Override
    public void onAssetLoaded(Asset asset) {
        name.setValue(asset.getName());
        category.setValue(asset.getCategory());
        balance.setValue(asset.getBalance());
        annualReturn.setValue(asset.getAnnualReturn());
        dataLoading.setValue(false);
        isDataLoaded = true;
    }

    public LiveData<Boolean> getDataLoading() {
        return dataLoading;
    }

}
