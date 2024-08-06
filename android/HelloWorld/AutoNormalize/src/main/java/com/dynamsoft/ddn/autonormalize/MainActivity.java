package com.dynamsoft.ddn.autonormalize;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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
    private AppBarConfiguration appBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null) {
            // Initialize the license.
            // The license string here is a trial license. Note that network connection is required for this license to work.
            // You can request an extension via the following link: https://www.dynamsoft.com/customer/license/trialLicense?product=ddn&utm_source=samples&package=android
            LicenseManager.initLicense("DLS2eyJvcmdhbml6YXRpb25JRCI6IjIwMDAwMSJ9", this, (isSuccess, error) -> {
                if (!isSuccess) {
                    error.printStackTrace();
                    runOnUiThread(()-> ((TextView) findViewById(R.id.tv_license_error)).setText("License initialization failed: "+error.getMessage()));
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