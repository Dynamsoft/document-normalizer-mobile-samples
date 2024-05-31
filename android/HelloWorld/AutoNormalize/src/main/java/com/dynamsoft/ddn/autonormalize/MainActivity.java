package com.dynamsoft.ddn.autonormalize;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.dynamsoft.dce.utils.PermissionUtil;
import com.dynamsoft.ddn.autonormalize.databinding.ActivityMainBinding;
import com.dynamsoft.license.LicenseManager;

public class MainActivity extends AppCompatActivity {
    // Initialize license for Dynamsoft Document Normalizer.
    // The license string here is a time-limited trial license. Note that network connection is required for this license to work.
    // You can also request a 30-day trial license via the Request a Trial License link: https://www.dynamsoft.com/customer/license/trialLicense?product=ddn&utm_source=github&package=android
    private static final String LICENSE = "DLS2eyJvcmdhbml6YXRpb25JRCI6IjIwMDAwMSJ9";
    private AppBarConfiguration appBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null) {
            LicenseManager.initLicense(LICENSE, this, (isSuccess, error) -> {
                if (!isSuccess) {
                    error.printStackTrace();
                }
            });
        }
        PermissionUtil.requestCameraPermission(this);

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