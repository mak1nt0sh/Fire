package com.example.fire.data;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.fire.AppExecutors;
import com.example.fire.data.database.AssetDao;

import java.util.List;

/**
 * Repository handling the work with assets.
 */
public class DataRepository {
    public static final String LOG_TAG = DataRepository.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static DataRepository instance;
    private final AssetDao assetDao;
    private final AppExecutors executors;
    private boolean initialized = false;

    private DataRepository(AssetDao assetDao, AppExecutors executors) {
        this.assetDao = assetDao;
        this.executors = executors;
    }

    public synchronized static DataRepository getInstance(AssetDao assetDao, AppExecutors executors) {
        Log.d(LOG_TAG, "Getting the repository");
        if (instance == null) {
            synchronized (LOCK) {
                instance = new DataRepository(assetDao, executors);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return instance;
    }

    /**
     * Database related operations
     **/

    public LiveData<List<Asset>> getAllAssets() {
        initializeData();
        return assetDao.getAllAssets();
    }

    public void getAssetById(int assetId, final LoadAssetCallback callback) {
        Runnable runnable = new Runnable() {;
            @Override
            public void run() {
                final Asset asset = assetDao.getAssetById(assetId);
                executors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onAssetLoaded(asset);
                    }
                });
            }
        };

        executors.diskIO().execute(runnable);
    }

    public void instertAsset(Asset asset) {
        executors.diskIO().execute(() -> assetDao.insert(asset));
    }

    public void updateAsset(Asset asset) {
        executors.diskIO().execute(() -> assetDao.update(asset));
    }

    public void deleteAsset(int assetId) {
        executors.diskIO().execute(() -> assetDao.delete(assetId));
    }

    private synchronized void initializeData() {

        // Only perform initialization once per app lifetime. If initialization has already been
        // performed, we have nothing to do in this method.
        if (initialized) return;
        initialized = true;
    }
}

