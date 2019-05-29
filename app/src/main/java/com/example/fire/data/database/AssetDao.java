package com.example.fire.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fire.data.Asset;

import java.util.List;

@Dao
public interface AssetDao {

    @Insert
    void insert(Asset asset);

    @Update
    void update(Asset asset);

    @Query("DELETE FROM asset_table WHERE id = :assetId")
    void delete(int assetId);

    @Query("DELETE FROM asset_table")
    void deleteAllAssets();

    @Query("SELECT * FROM asset_table ORDER BY id DESC")
    LiveData<List<Asset>> getAllAssets();

    @Query("SELECT * FROM asset_table WHERE id = :assetId")
    Asset getAssetById(int assetId);
}
