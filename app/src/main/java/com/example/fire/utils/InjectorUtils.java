package com.example.fire.utils;

import android.content.Context;

import com.example.fire.AppExecutors;
import com.example.fire.addeditasset.AddEditAssetViewModelFactory;
import com.example.fire.assetslist.AssetListViewModelFactory;
import com.example.fire.data.DataRepository;
import com.example.fire.data.database.AppDatabase;

/**
 * Provides static methods to inject the various classes needed.
 */

public class InjectorUtils {

    public static DataRepository provideRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        return DataRepository.getInstance(database.assetDao(), executors);
    }

    public static AssetListViewModelFactory provideAssetListViewModelFactory(Context context) {
        DataRepository repository = provideRepository(context.getApplicationContext());
        return new AssetListViewModelFactory(repository);
    }

    public static AddEditAssetViewModelFactory provideAddEditAssetViewModelFactory(Context context){
        DataRepository repository = provideRepository(context.getApplicationContext());
        return new AddEditAssetViewModelFactory(repository);
    }
}
