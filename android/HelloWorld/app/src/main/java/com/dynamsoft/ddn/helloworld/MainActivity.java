package com.dynamsoft.ddn.helloworld;

import android.os.Bundle;

import com.dyanmsoft.license.LicenseManager;
import com.dyanmsoft.license.LicenseVerificationListener;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.dynamsoft.ddn.helloworld.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String LICENSE =
            "DLS2eyJoYW5kc2hha2VDb2RlIjoiMTAwMjU2NDAxLTEwMDgwMDUzOSIsIm1haW5TZXJ2ZXJVUkwiOiJodHRwczovL210cGwuZHluYW1zb2Z0LmNvbS8iLCJvcmdhbml6YXRpb25JRCI6IjEwMDI1NjQwMSIsInN0YW5kYnlTZXJ2ZXJVUkwiOiJodHRwczovL210cGxyZXMuZHluYW1zb2Z0LmNvbS8iLCJjaGVja0NvZGUiOjEwOTU5NDgyMzR9";
    private AppBarConfiguration appBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LicenseManager.initLicense(LICENSE, this, (isSuccess, error) -> {
            runOnUiThread(()->{
                if(!isSuccess) {
                    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Init license successful!", Toast.LENGTH_SHORT).show();
                }
            });
        });

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}