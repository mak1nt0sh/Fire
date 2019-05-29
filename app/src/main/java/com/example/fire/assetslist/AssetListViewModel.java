package com.example.fire.assetslist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.fire.data.Asset;
import com.example.fire.data.DataRepository;

import java.util.List;

public class AssetListViewModel extends ViewModel {

    private final DataRepository repository;
    private final LiveData<List<Asset>> assets;

    public AssetListViewModel(DataRepository repository) {
        this.repository = repository;
        this.assets = repository.getAllAssets();
    }

    public LiveData<List<Asset>> getAssets() {
        return assets;
    }

}
