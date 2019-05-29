package com.example.fire.assetslist;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.fire.data.DataRepository;

public class AssetListViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final DataRepository repository;

    public AssetListViewModelFactory(DataRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AssetListViewModel(repository);
    }
}
