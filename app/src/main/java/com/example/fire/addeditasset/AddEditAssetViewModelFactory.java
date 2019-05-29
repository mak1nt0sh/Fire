package com.example.fire.addeditasset;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.fire.data.DataRepository;

public class AddEditAssetViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final DataRepository repository;

    public AddEditAssetViewModelFactory(DataRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddEditAssetViewModel(repository);

    }
}
