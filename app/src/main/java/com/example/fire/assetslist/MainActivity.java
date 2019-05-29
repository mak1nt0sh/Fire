package com.example.fire.assetslist;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fire.R;
import com.example.fire.addeditasset.AddEditAssetFragment;
import com.example.fire.data.Asset;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            // Create a new Fragment to be placed in the activity layout
            AssetListFragment listFragment = new AssetListFragment();
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, listFragment).commit();
        }
    }

    /**
     * Opens fragment to add new asset
     */
    public void addAssetFragment() {
        AddEditAssetFragment addEditAssetFragment = new AddEditAssetFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,
                        addEditAssetFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Opens fragment to edit existing asset
     */
    public void editAssetFragment(Asset asset) {
        int assetId = asset.getId();

        AddEditAssetFragment addEditAssetFragment = new AddEditAssetFragment();
        Bundle args = new Bundle();
        args.putInt("ASSET_ID", assetId);
        addEditAssetFragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,
                        addEditAssetFragment)
                .addToBackStack(null)
                .commit();

    }
}

